package net.blay09.mods.cookingforblockheads.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOvenResult extends SlotItemHandler {

    public SlotOvenResult(IItemHandler itemHandler, int i, int x, int y) {
        super(itemHandler, i, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

}
