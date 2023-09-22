package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.*;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.menu.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.blay09.mods.cookingforblockheads.registry.recipe.GeneralFoodRecipe;
import net.blay09.mods.cookingforblockheads.registry.recipe.SmeltingFood;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CookingRegistry {

    private static final List<Recipe<Container>> recipeList = Lists.newArrayList();
    private static final ArrayListMultimap<ResourceLocation, FoodRecipe> foodItems = ArrayListMultimap.create();
    private static final NonNullList<ItemStack> tools = NonNullList.create();
    private static final Map<ItemStack, Integer> ovenFuelItems = Maps.newHashMap();
    private static final Map<ItemStack, ItemStack> ovenRecipes = Maps.newHashMap();
    private static final Map<ItemStack, SinkHandler> sinkHandlers = Maps.newHashMap();
    private static final Map<ItemStack, ToasterHandler> toastHandlers = Maps.newHashMap();
    private static final NonNullList<ItemStack> waterItems = NonNullList.create();
    private static final NonNullList<ItemStack> milkItems = NonNullList.create();
    private static final List<ISortButton> customSortButtons = Lists.newArrayList();

    private static Collection<ItemStack> nonFoodRecipes = Collections.emptyList();

    private static final ToasterHandler dummyBreadToasterHandler = itemStack -> {
        CompoundTag tag = itemStack.getTag();
        boolean alreadyToasted = tag != null && tag.getBoolean("CookingForBlockheadsToasted");
        if (alreadyToasted) {
            if (CookingForBlockheadsConfig.getActive().allowVeryToastedBread) {
                ItemStack veryToasted = new ItemStack(Items.CHARCOAL);
                veryToasted.setHoverName(Component.translatable("tooltip.cookingforblockheads:very_toasted"));
                return veryToasted;
            } else {
                return itemStack;
            }
        } else {
            ItemStack toasted = itemStack.copy();
            toasted.setHoverName(Component.translatable("tooltip.cookingforblockheads:toasted", itemStack.getHoverName()));
            toasted.getOrCreateTag().putBoolean("CookingForBlockheadsToasted", true);
            return toasted;
        }
    };

    public static void initFoodRegistry(RecipeManager recipeManager, RegistryAccess registryAccess) {
        recipeList.clear();
        foodItems.clear();

        FoodRegistryInitEvent init = new FoodRegistryInitEvent();
        Balm.getEvents().fireEvent(init);

        nonFoodRecipes = init.getNonFoodRecipes();

        // Crafting Recipes of Food Items
        for (RecipeHolder<?> recipeHolder : recipeManager.getRecipes()) {
            var recipe = recipeHolder.value();
            // Restrict the search to crafting and smelting recipes to prevent duplicates from smoking and campfire cooking, as well as issues with other mod custom recipes
            if (!isValidRecipeType(recipe)) {
                continue;
            }

            ItemStack output = recipe.getResultItem(registryAccess);

            //noinspection ConstantConditions
            if (output == null) {
                CookingForBlockheads.logger.warn("Recipe " + recipeHolder.id() + " returned a null ItemStack in getRecipeOutput - this is bad! The developer of " + recipeHolder.id().getNamespace() + " should return an empty ItemStack instead to avoid problems.");
                continue;
            }

            if (!output.isEmpty()) {
                if (output.getItem().isEdible()) {
                    if (!isWeirdConversionRecipe(recipe, registryAccess)) {
                        addFoodRecipe(recipe, registryAccess);
                    }
                } else {
                    // TODO Make nonFoodRecipes a map to improve lookup performance
                    for (ItemStack itemStack : nonFoodRecipes) {
                        if (ItemStack.isSameItem(recipe.getResultItem(registryAccess), itemStack)) {
                            addFoodRecipe(recipe, registryAccess);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean isValidRecipeType(Recipe<?> recipe) {
        return recipe.getType() == RecipeType.CRAFTING || recipe.getType() == RecipeType.SMELTING;
    }

    /**
     * This was added to prevent Harvestcraft's old oredict conversion recipes from showing up, might not be needed anymore?
     */
    public static boolean isWeirdConversionRecipe(Recipe<?> recipe, RegistryAccess registryAccess) {
        if (recipe.getIngredients().size() == 2 && recipe.getResultItem(registryAccess).getCount() == 2) {
            Ingredient first = recipe.getIngredients().get(0);
            Ingredient second = recipe.getIngredients().get(1);
            return first.test(recipe.getResultItem(registryAccess)) && second.test(recipe.getResultItem(registryAccess));
        }

        return false;
    }

    public static boolean isNonFoodRecipe(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        for (ItemStack nonFoodStack : nonFoodRecipes) {
            if (ItemStack.isSameItem(itemStack, nonFoodStack)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void addFoodRecipe(Recipe<? extends Container> recipe, RegistryAccess registryAccess) {
        ItemStack output = recipe.getResultItem(registryAccess);
        if (!output.isEmpty() && !recipe.getIngredients().isEmpty()) {
            FoodRecipe foodRecipe;
            if (recipe instanceof AbstractCookingRecipe) {
                foodRecipe = new SmeltingFood(recipe, recipe.getResultItem(registryAccess));
            } else if (recipe instanceof CraftingRecipe) {
                foodRecipe = new GeneralFoodRecipe(recipe, recipe.getResultItem(registryAccess));
            } else {
                return;
            }

            recipeList.add((Recipe<Container>) recipe);
            foodItems.put(Balm.getRegistries().getKey(output.getItem()), foodRecipe);
        }
    }

    public static Multimap<ResourceLocation, FoodRecipe> getFoodRecipes() {
        return foodItems;
    }

    public static Collection<FoodRecipe> getFoodRecipes(ItemStack outputItem) {
        return foodItems.get(Balm.getRegistries().getKey(outputItem.getItem()));
    }

    public static Collection<FoodRecipe> getFoodRecipes(ResourceLocation outputItem) {
        return foodItems.get(outputItem);
    }

    public static void addToolItem(ItemStack toolItem) {
        tools.add(toolItem);
    }

    public static boolean isToolItem(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        for (ItemStack toolItem : tools) {
            if (ItemStack.isSameItem(toolItem, itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isToolItem(Ingredient ingredient) {
        for (ItemStack itemStack : ingredient.getItems()) {
            if (isToolItem(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static void addOvenFuel(ItemStack itemStack, int fuelTime) {
        ovenFuelItems.put(itemStack, fuelTime);
    }

    public static int getOvenFuelTime(ItemStack itemStack) {
        for (Map.Entry<ItemStack, Integer> entry : ovenFuelItems.entrySet()) {
            if (ItemStack.isSameItem(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public static void addSmeltingItem(ItemStack source, ItemStack result) {
        ovenRecipes.put(source, result);
    }

    public static ItemStack getSmeltingResult(ItemStack itemStack) {
        for (Map.Entry<ItemStack, ItemStack> entry : ovenRecipes.entrySet()) {
            if (ItemStack.isSameItem(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    public static void addToasterHandler(ItemStack itemStack, ToasterHandler toastHandler) {
        toastHandlers.put(itemStack, toastHandler);
    }

    @Nullable
    public static ToasterHandler getToasterHandler(ItemStack itemStack) {
        for (Map.Entry<ItemStack, ToasterHandler> entry : toastHandlers.entrySet()) {
            if (ItemStack.isSameItem(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }

        if (itemStack.getItem() == Items.BREAD) {
            return dummyBreadToasterHandler;
        }

        return null;
    }

    public static void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        sinkHandlers.put(itemStack, sinkHandler);
    }

    public static ItemStack getSinkOutput(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        for (Map.Entry<ItemStack, SinkHandler> entry : sinkHandlers.entrySet()) {
            if (ItemStack.isSameItem(entry.getKey(), itemStack)) {
                return entry.getValue().getSinkOutput(itemStack);
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    private static SourceItem findAnyItemStack(ItemStack checkStack, List<IKitchenItemProvider> inventories, boolean requireBucket) {
        if (checkStack.isEmpty()) {
            return null;
        }

        IngredientPredicate predicate = IngredientPredicateWithCacheImpl.of((it, count) -> ItemStack.isSameItem(it, checkStack) && count > 0, checkStack);
        for (int i = 0; i < inventories.size(); i++) {
            IKitchenItemProvider itemProvider = inventories.get(i);
            SourceItem found = itemProvider.findSource(predicate, 1, inventories, requireBucket, true);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    public static List<SourceItem> findSourceCandidates(FoodIngredient ingredient, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean isNoFilter) {
        List<SourceItem> sourceList = new ArrayList<>();

        ItemStack[] variants = ingredient.getItemStacks();
        for (ItemStack checkStack : variants) {
            SourceItem sourceItem = CookingRegistry.findAnyItemStack(checkStack, inventories, requireBucket);
            ItemStack foundStack = sourceItem != null ? sourceItem.getSourceStack() : ItemStack.EMPTY;
            if (foundStack.isEmpty()) {
                if (isNoFilter || ingredient.isToolItem()) {
                    sourceItem = new SourceItem(null, -1, checkStack);
                }
            }

            if (sourceItem != null) {
                sourceList.add(sourceItem);
            }
        }

        SourceItem sourceItem = !sourceList.isEmpty() ? sourceList.get(0) : null;
        if (sourceItem != null && sourceItem.getSourceProvider() != null) {
            sourceItem.getSourceProvider().markAsUsed(sourceItem, 1, inventories, requireBucket);
        }

        return sourceList;
    }

    public static boolean consumeBucket(List<IKitchenItemProvider> inventories, boolean simulate) {
        ItemStack bucketStack = new ItemStack(Items.BUCKET);
        IngredientPredicate predicate = IngredientPredicateWithCacheImpl.of((it, count) -> ItemStack.isSameItem(it, bucketStack) && count > 0, bucketStack);
        for (int i = 0; i < inventories.size(); i++) {
            IKitchenItemProvider itemProvider = inventories.get(i);
            SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed(predicate, 1, inventories, false, simulate);
            if (sourceItem != null) {
                return true;
            }
        }

        return false;
    }

    public static RecipeStatus getRecipeStatus(FoodRecipe recipe, List<IKitchenItemProvider> inventories, boolean hasOven) {
        boolean requireBucket = doesItemRequireBucketForCrafting(recipe.getOutputItem());
        for (IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
        }

        List<FoodIngredient> craftMatrix = recipe.getCraftMatrix();
        boolean missingTools = false;
        for (FoodIngredient ingredient : craftMatrix) {
            if (ingredient != null) {
                List<SourceItem> sourceList = findSourceCandidates(ingredient, inventories, requireBucket, false);
                if (sourceList.isEmpty()) {
                    return RecipeStatus.MISSING_INGREDIENTS;
                }

                if (sourceList.stream().allMatch(it -> it.getSourceProvider() == null)) {
                    missingTools = true;
                }
            }
        }

        // Do not mark smeltable recipes as available unless an oven is present.
        if (recipe.getType() == FoodRecipeType.SMELTING && !hasOven) {
            return RecipeStatus.MISSING_TOOLS;
        }

        return missingTools ? RecipeStatus.MISSING_TOOLS : RecipeStatus.AVAILABLE;
    }

    public static List<IKitchenItemProvider> getItemProviders(@Nullable KitchenMultiBlock multiBlock, Inventory inventory) {
        return multiBlock != null ? multiBlock.getItemProviders(inventory) : Lists.newArrayList(new DefaultKitchenItemProvider(inventory));
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> T findFoodRecipe(InventoryCraftBook craftMatrix, Level level, RecipeType<T> recipeType, Item expectedItem) {
        for (Recipe<Container> recipe : recipeList) {
            if (recipe.getType() == recipeType && recipe.matches(craftMatrix, level) && recipe.getResultItem(level.registryAccess()).getItem() == expectedItem) {
                return (T) recipe;
            }
        }

        return null;
    }

    public static void addWaterItem(ItemStack waterItem) {
        waterItems.add(waterItem);
    }

    public static void addMilkItem(ItemStack milkItem) {
        milkItems.add(milkItem);
    }

    public static void addSortButton(ISortButton button) {
        customSortButtons.add(button);
    }

    public static NonNullList<ItemStack> getWaterItems() {
        return waterItems;
    }

    public static NonNullList<ItemStack> getMilkItems() {
        return milkItems;
    }

    public static List<ISortButton> getSortButtons() {
        return customSortButtons;
    }

    public static boolean doesItemRequireBucketForCrafting(ItemStack outputItem) {
        ItemStack containerItem = Balm.getHooks().getCraftingRemainingItem(outputItem);
        if (!containerItem.isEmpty() && containerItem.getItem() == Items.BUCKET) {
            return true;
        }
        ResourceLocation registryName = Balm.getRegistries().getKey(outputItem.getItem());
        return registryName != null && registryName.getPath().contains("bucket");
    }
}
