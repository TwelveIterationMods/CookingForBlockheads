package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.*;
import net.blay09.mods.cookingforblockheads.menu.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Deprecated(forRemoval = true)
public class CookingRegistry {

    private static final List<Recipe<Container>> recipeList = Lists.newArrayList();
    private static final ArrayListMultimap<ResourceLocation, FoodRecipe> foodItems = ArrayListMultimap.create();
    private static final Map<ItemStack, Integer> ovenFuelItems = Maps.newHashMap();
    private static final List<ISortButton> customSortButtons = Lists.newArrayList();

    private static ItemStack toast(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        boolean alreadyToasted = tag != null && tag.getBoolean("CookingForBlockheadsToasted");
        if (alreadyToasted) {
            if (CookingForBlockheadsConfig.getActive().allowVeryToastedBread) {
                ItemStack veryToasted = new ItemStack(Items.CHARCOAL);
                veryToasted.setHoverName(Component.translatable("tooltip.cookingforblockheads.very_toasted"));
                return veryToasted;
            } else {
                return itemStack;
            }
        } else {
            ItemStack toasted = itemStack.copy();
            toasted.setHoverName(Component.translatable("tooltip.cookingforblockheads.toasted", itemStack.getHoverName()));
            toasted.getOrCreateTag().putBoolean("CookingForBlockheadsToasted", true);
            return toasted;
        }
    }

    public static void initFoodRegistry(RecipeManager recipeManager, RegistryAccess registryAccess) {
        recipeList.clear();
        foodItems.clear();

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
                    addFoodRecipe(recipe, registryAccess);
                } else {
                    if (output.is(ModItemTags.IS_CRAFTABLE)) {
                        addFoodRecipe(recipe, registryAccess);
                        break;
                    }
                }
            }
        }
    }

    private static boolean isValidRecipeType(Recipe<?> recipe) {
        return recipe.getType() == RecipeType.CRAFTING || recipe.getType() == RecipeType.SMELTING;
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

    public static void addSortButton(ISortButton button) {
        customSortButtons.add(button);
    }

    public static List<ISortButton> getSortButtons() {
        return customSortButtons;
    }

}
