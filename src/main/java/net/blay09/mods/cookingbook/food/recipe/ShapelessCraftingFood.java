package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class ShapelessCraftingFood extends FoodRecipe {

    public ShapelessCraftingFood(ShapelessRecipes recipe) {
        this.outputItem = recipe.getRecipeOutput();
        this.craftMatrix = new FoodIngredient[recipe.getRecipeSize()];
        for(int i = 0; i < recipe.recipeItems.size(); i++) {
            boolean isToolItem = PamsHarvestcraft.isToolItem((ItemStack) recipe.recipeItems.get(i));
            craftMatrix[i] = new FoodIngredient(((ItemStack) recipe.recipeItems.get(i)).copy(), isToolItem);
        }
    }

}
