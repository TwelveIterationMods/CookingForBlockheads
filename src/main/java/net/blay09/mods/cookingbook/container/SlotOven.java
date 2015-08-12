package net.blay09.mods.cookingbook.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOven extends Slot {

    public SlotOven(IInventory inventory, int i, int x, int y) {
        super(inventory, i, x, y);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

}
