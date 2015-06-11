package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.ingredient.DefaultIngredient;
import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.blay09.mods.cookingbook.food.ingredient.ToolIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class ShapelessCraftingFood implements IFoodRecipe {

    private final ShapelessRecipes recipe;
    private final IFoodIngredient[] craftMatrix;

    public ShapelessCraftingFood(ShapelessRecipes recipe) {
        this.recipe = recipe;
        craftMatrix = new IFoodIngredient[recipe.getRecipeSize()];
        for(int i = 0; i < recipe.recipeItems.size(); i++) {
            if(PamsHarvestcraft.isToolItem((ItemStack) recipe.recipeItems.get(i))) {
                craftMatrix[i] = new ToolIngredient(((ItemStack) recipe.recipeItems.get(i)).copy());
            } else {
                craftMatrix[i] = new DefaultIngredient(((ItemStack) recipe.recipeItems.get(i)).copy());
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

}
