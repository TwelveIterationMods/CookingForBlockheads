package net.blay09.mods.cookingforblockheads.crafting;

import net.minecraft.world.item.crafting.ShapedRecipe;

public class KitchenShapedRecipeHandler extends AbstractKitchenCraftingRecipeHandler<ShapedRecipe> {
    @Override
    public int mapToMatrixSlot(ShapedRecipe recipe, int ingredientIndex) {
        final int recipeWidth = recipe.getWidth();
        final int origX = ingredientIndex % recipeWidth;
        final int origY = ingredientIndex / recipeWidth;

        // Offset to center the recipe if its width is 1
        final int offsetX = recipeWidth == 1 ? 1 : 0;

        return origY * 3 + origX + offsetX;
    }
}
