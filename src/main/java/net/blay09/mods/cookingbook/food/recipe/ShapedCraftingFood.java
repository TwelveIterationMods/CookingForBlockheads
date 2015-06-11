package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.ingredient.DefaultIngredient;
import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.blay09.mods.cookingbook.food.ingredient.ToolIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedCraftingFood implements IFoodRecipe {

    private final IFoodIngredient[] craftMatrix;
    private final ShapedRecipes recipe;

    public ShapedCraftingFood(ShapedRecipes recipe) {
        this.recipe = recipe;
        craftMatrix = new IFoodIngredient[recipe.getRecipeSize()];
        for(int i = 0; i < recipe.recipeItems.length; i++) {
            if(recipe.recipeItems[i] != null) {
                if(PamsHarvestcraft.isToolItem(recipe.recipeItems[i])) {
                    craftMatrix[i] = new ToolIngredient(recipe.recipeItems[i].copy());
                } else {
                    craftMatrix[i] = new DefaultIngredient(recipe.recipeItems[i].copy());
                }
            }
        }
    }

    @Override
    public IFoodIngredient[] getCraftMatrix() {
        return craftMatrix;
    }

    @Override
    public ItemStack getOutputItem() {
        return recipe.getRecipeOutput();
    }

    @Override
    public boolean isSmeltingRecipe() {
        return false;
    }

    @Override
    public IRecipe getCraftingRecipe() {
        return recipe;
    }

}
