package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class KitchenShapedRecipeHandler implements KitchenRecipeHandler<ShapedRecipe> {
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
    public ItemStack assemble(ShapedRecipe recipe, CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return recipe.assemble(craftingContainer, registryAccess);
    }
}
