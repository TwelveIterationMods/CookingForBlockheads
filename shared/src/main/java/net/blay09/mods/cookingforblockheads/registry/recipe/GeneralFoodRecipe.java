package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class GeneralFoodRecipe extends FoodRecipe {

    public GeneralFoodRecipe(Recipe<?> recipe, ItemStack outputItem) {
        this.outputItem = outputItem;
        if (recipe instanceof ShapedRecipe) {
            this.recipeWidth = ((ShapedRecipe) recipe).getWidth();
        } else {
            this.recipeWidth = recipe.canCraftInDimensions(1, 1) ? 1 : recipe.canCraftInDimensions(2, 2) ? 2 : 3;
        }

        fillCraftMatrix(recipe);
    }

}
