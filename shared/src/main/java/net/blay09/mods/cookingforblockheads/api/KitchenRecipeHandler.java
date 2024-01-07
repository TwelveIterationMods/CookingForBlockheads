package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public interface KitchenRecipeHandler<T extends Recipe<?>> {
    int mapToMatrixSlot(T recipe, int ingredientIndex);

    ItemStack assemble(T recipe, CraftingContainer craftingContainer, RegistryAccess registryAccess);
}
