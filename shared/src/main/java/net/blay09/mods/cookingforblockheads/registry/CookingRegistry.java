package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.*;
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

}
