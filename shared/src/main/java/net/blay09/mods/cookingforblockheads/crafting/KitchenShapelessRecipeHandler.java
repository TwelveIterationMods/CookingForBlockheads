package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class KitchenShapelessRecipeHandler implements KitchenRecipeHandler<ShapelessRecipe> {
    @Override
    public int mapToMatrixSlot(ShapelessRecipe recipe, int ingredientIndex) {
        return ingredientIndex;
    }

    @Override
    public ItemStack assemble(ShapelessRecipe recipe, CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return recipe.assemble(craftingContainer, registryAccess);
    }
}
