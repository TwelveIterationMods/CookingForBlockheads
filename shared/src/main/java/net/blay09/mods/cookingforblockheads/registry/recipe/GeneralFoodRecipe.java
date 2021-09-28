package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class GeneralFoodRecipe extends FoodRecipe {

    public GeneralFoodRecipe(IRecipe<?> recipe) {
        this.outputItem = recipe.getRecipeOutput();
        if (recipe instanceof IShapedRecipe<?>) {
            this.recipeWidth = ((IShapedRecipe<?>) recipe).getRecipeWidth();
        } else {
            this.recipeWidth = recipe.canFit(1, 1) ? 1 : recipe.canFit(2, 2) ? 2 : 3;
        }

        fillCraftMatrix(recipe);
    }

}
