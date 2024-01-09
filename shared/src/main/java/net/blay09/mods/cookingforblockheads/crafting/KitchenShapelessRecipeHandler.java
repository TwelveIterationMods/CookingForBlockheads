package net.blay09.mods.cookingforblockheads.crafting;

import net.minecraft.world.item.crafting.ShapelessRecipe;

public class KitchenShapelessRecipeHandler extends AbstractKitchenCraftingRecipeHandler<ShapelessRecipe> {
    @Override
    public int mapToMatrixSlot(ShapelessRecipe recipe, int ingredientIndex) {
        return ingredientIndex;
    }
}
