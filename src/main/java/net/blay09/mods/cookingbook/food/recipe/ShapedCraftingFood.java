package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;

public class ShapedCraftingFood extends FoodRecipe {

    public ShapedCraftingFood(ShapedRecipes recipe) {
        this.outputItem = recipe.getRecipeOutput();
        craftMatrix = new ArrayList<>();

        for(int i = 0; i < recipe.recipeItems.length; i++) {
            if(recipe.recipeItems[i] != null) {
                boolean isToolItem = PamsHarvestcraft.isToolItem(recipe.recipeItems[i]);
                craftMatrix.add(new FoodIngredient(recipe.recipeItems[i].copy(), isToolItem));
            }
        }
    }

}
