package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

/**
 * Item processors are able to define custom logic for processing recipes.
 * <p>
 * This is used by the oven for example, in order to allow smelting recipes to be moved to the oven when crafted.
 */
public interface KitchenItemProcessor {
    boolean canProcess(RecipeType<?> recipeType);

    KitchenOperation processRecipe(Recipe<?> recipe, List<IngredientToken> ingredientTokens);
}
