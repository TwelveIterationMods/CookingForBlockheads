package net.blay09.mods.cookingbook.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOvenResult extends Slot {

    public SlotOvenResult(IInventory inventory, int i, int x, int y) {
        super(inventory, i, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

}
