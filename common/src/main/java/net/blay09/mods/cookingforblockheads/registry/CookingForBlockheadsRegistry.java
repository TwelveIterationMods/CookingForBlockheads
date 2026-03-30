package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.platform.event.callback.ServerLifecycleCallback;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeGroup;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.blay09.mods.cookingforblockheads.mixin.RecipeManagerAccessor;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class CookingForBlockheadsRegistry {

    private static final Multimap<Identifier, RecipeHolder<?>> recipesByItemId = ArrayListMultimap.create();
    private static final Multimap<Identifier, RecipeHolder<?>> recipesByGroup = ArrayListMultimap.create();
    private static final List<ISortButton> sortButtons = new ArrayList<>();
    private static final Map<ItemStack, Integer> ovenFuelItems = new HashMap<>();
    private static final Map<Class<? extends Recipe<?>>, KitchenRecipeHandler<?, ?>> kitchenRecipeHandlers = new HashMap<>();

    public static void initialize() {
        ServerLifecycleCallback.Reloaded.EVENT.register(server -> reload(server.getRecipeManager()));
        ServerLifecycleCallback.Started.EVENT.register(server -> reload(server.getRecipeManager()));
    }

    private static void reload(RecipeManager recipeManager) {
        recipesByItemId.clear();
        loadRecipesByType(recipeManager, ModRecipes.ovenRecipes.type());
        loadRecipesByType(recipeManager, RecipeType.CRAFTING);
        loadRecipesByType(recipeManager, RecipeType.CAMPFIRE_COOKING);
        loadRecipesByType(recipeManager, RecipeType.SMOKING);
        loadRecipesByType(recipeManager, RecipeType.SMELTING);
    }

    @SuppressWarnings("unchecked")
    private static <C extends RecipeInput, T extends Recipe<C>> void loadRecipesByType(RecipeManager recipeManager, RecipeType<T> recipeType) {
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        for (final var recipeHolder : recipeMap.byType(recipeType)) {
            if (!isEligibleRecipe(recipeHolder)) {
                continue;
            }

            final var recipe = recipeHolder.value();
            final var recipeHandler = getKitchenRecipeHandler(recipe);
            if (recipeHandler == null) {
                continue;
            }

            final var resultItem = recipeHandler.predictResultItem(recipe).create();
            if (isEligibleResultItem(resultItem)) {
                if (recipe instanceof AbstractCookingRecipe cookingRecipe && isOvenDuplicate(cookingRecipe, (KitchenRecipeHandler<?, AbstractCookingRecipe>) recipeHandler, resultItem)) {
                    continue;
                }
                recipesByItemId.put(getRecipeItemId(resultItem), recipeHolder);

                final var groups = getGroups();
                for (final var group : groups) {
                    for (final var ingredient : group.getChildren()) {
                        if (ingredient.test(resultItem)) {
                            final var groupItemId = getRecipeItemId(group.getParentItem());
                            recipesByGroup.put(groupItemId, recipeHolder);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static <T extends Recipe<?>> boolean isOvenDuplicate(T recipe, KitchenRecipeHandler<?, T> recipeHandler, ItemStack resultItem) {
        final var itemId = getRecipeItemId(resultItem);
        final var existing = recipesByItemId.get(itemId);
        final var ingredients = recipeHandler.getIngredients(recipe);
        for (final var recipeHolder : existing) {
            final var otherRecipeHandler = getKitchenRecipeHandler(recipeHolder.value());
            final var otherIngredients = otherRecipeHandler.getIngredients(recipeHolder);
            final var otherResultItem = otherRecipeHandler.predictResultItem(recipeHolder).create();
            if (ItemStack.isSameItemSameComponents(resultItem, otherResultItem) && ingredients.equals(otherIngredients)) {
                return true;
            }
        }

        return false;
    }

    public static List<KitchenRecipeGroup> getGroups() {
        return List.of();
    }

    private static boolean isEligibleResultItem(ItemStack itemStack) {
        if (itemStack.is(ModItemTags.EXCLUDED)) {
            return false;
        }

        return itemStack.has(DataComponents.FOOD) || itemStack.is(ModItemTags.FOODS) || itemStack.is(ModItemTags.INGREDIENTS);
    }

    private static <T extends RecipeInput> boolean isEligibleRecipe(RecipeHolder<? extends Recipe<T>> recipe) {
        return !CookingForBlockheadsConfig.getActive().excludedRecipes.contains(recipe.id().identifier());
    }

    public static <C extends RecipeInput, T extends Recipe<C>> void registerKitchenRecipeHandler(Class<? extends T> recipeType, KitchenRecipeHandler<C, T> handler) {
        kitchenRecipeHandlers.put(recipeType, handler);
    }

    @SuppressWarnings("unchecked")
    public static <C extends RecipeInput, T extends Recipe<C>, V extends KitchenRecipeHandler<C, T>> @Nullable V getKitchenRecipeHandler(T recipe) {
        for (Class<? extends Recipe<?>> handlerClass : kitchenRecipeHandlers.keySet()) {
            if (handlerClass.isAssignableFrom(recipe.getClass())) {
                return (V) kitchenRecipeHandlers.get(handlerClass);
            }
        }

        return (V) kitchenRecipeHandlers.get(recipe.getClass());
    }

    public static void addSortButton(ISortButton nameSortButton) {
        sortButtons.add(nameSortButton);
    }

    public static Collection<ISortButton> getSortButtons() {
        return sortButtons;
    }

    public static void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        ovenFuelItems.put(fuelItem, fuelTime);
    }

    public static int getOvenFuelTime(ItemStack itemStack) {
        for (final var entry : ovenFuelItems.entrySet()) {
            if (ItemStack.isSameItem(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public static Collection<RecipeHolder<?>> getRecipesFor(ItemStack resultItem) {
        final var itemId = getRecipeItemId(resultItem);
        return recipesByItemId.get(itemId);
    }

    public static Collection<? extends RecipeHolder<?>> getRecipesInGroup(ItemStack resultItem) {
        final var itemId = getRecipeItemId(resultItem);
        return recipesByGroup.get(itemId);
    }

    public static Multimap<Identifier, RecipeHolder<?>> getRecipesByItemId() {
        return recipesByItemId;
    }

    public static Identifier getRecipeItemId(ItemStack itemStack) {
        return getRecipeItemId(itemStack.getItem());
    }

    public static Identifier getRecipeItemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }
}
