package net.blay09.mods.cookingforblockheads.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOven extends SlotItemHandler {

    public SlotOven(IItemHandler itemHandler, int i, int x, int y) {
        super(itemHandler, i, x, y);
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
