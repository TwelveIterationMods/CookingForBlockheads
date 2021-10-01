package net.blay09.mods.cookingforblockheads.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotOven extends Slot {

    public SlotOven(Container container, int i, int x, int y) {
        super(container, i, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

}
