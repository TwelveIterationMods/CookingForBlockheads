package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.block.TileEntityCookingOven;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SlotOvenInput extends Slot {

    public SlotOvenInput(IInventory inventory, int i, int x, int y) {
        super(inventory, i, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        ItemStack smeltingResult = TileEntityCookingOven.getSmeltingResult(stack);
        return smeltingResult != null && smeltingResult.getItem() instanceof ItemFood;
    }

}
