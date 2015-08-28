package net.blay09.mods.cookingbook.registry.food.recipe;

import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.blay09.mods.cookingbook.registry.food.FoodIngredient;
import net.blay09.mods.cookingbook.registry.food.FoodRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;

public class ShapedCraftingFood extends FoodRecipe {

    public ShapedCraftingFood(ShapedRecipes recipe) {
        this.outputItem = recipe.getRecipeOutput();
        craftMatrix = new ArrayList<>();

        for(int i = 0; i < recipe.recipeItems.length; i++) {
            if(recipe.recipeItems[i] != null) {
                boolean isToolItem = CookingRegistry.isToolItem(recipe.recipeItems[i]);
                craftMatrix.add(new FoodIngredient(recipe.recipeItems[i].copy(), isToolItem));
            }
        }
    }

}
