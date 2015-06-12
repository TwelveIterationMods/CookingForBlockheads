package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedCraftingFood extends FoodRecipe {

    public ShapedCraftingFood(ShapedRecipes recipe) {
        this.outputItem = recipe.getRecipeOutput();
        craftMatrix = new FoodIngredient[recipe.getRecipeSize()];
        for(int i = 0; i < recipe.recipeItems.length; i++) {
            if(recipe.recipeItems[i] != null) {

                boolean isToolItem = PamsHarvestcraft.isToolItem(recipe.recipeItems[i]);
                craftMatrix[i] = new FoodIngredient(recipe.recipeItems[i].copy(), isToolItem);
            }
        }
    }

}
