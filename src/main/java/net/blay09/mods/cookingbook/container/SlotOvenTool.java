package net.blay09.mods.cookingbook.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotOvenTool extends Slot {

    public SlotOvenTool(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
