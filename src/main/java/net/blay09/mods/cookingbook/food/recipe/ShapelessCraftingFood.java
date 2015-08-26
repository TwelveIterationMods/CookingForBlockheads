package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.util.ArrayList;

public class ShapelessCraftingFood extends FoodRecipe {

    public ShapelessCraftingFood(ShapelessRecipes recipe) {
        this.outputItem = recipe.getRecipeOutput();
        this.craftMatrix = new ArrayList<FoodIngredient>();
        for(int i = 0; i < recipe.recipeItems.size(); i++) {
            if (recipe.recipeItems.get(i) == null)
                continue;

            boolean isToolItem = PamsHarvestcraft.isToolItem((ItemStack) recipe.recipeItems.get(i));
            craftMatrix.add(new FoodIngredient(((ItemStack) recipe.recipeItems.get(i)).copy(), isToolItem));
        }
    }

}
