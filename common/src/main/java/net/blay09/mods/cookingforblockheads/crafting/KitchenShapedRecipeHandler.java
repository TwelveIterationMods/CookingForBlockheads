package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.mixin.ShapedRecipeAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Optional<Ingredient>> getIngredients(ShapedRecipe recipe) {
        return recipe.getIngredients();
    }

    @Override
    public ItemStack predictResultItem(ShapedRecipe recipe) {
        return ((ShapedRecipeAccessor) recipe).getResult();
    }
}
