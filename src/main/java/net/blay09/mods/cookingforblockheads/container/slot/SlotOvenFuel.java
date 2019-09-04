package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOvenFuel extends SlotItemHandler {

    public SlotOvenFuel(IItemHandler itemHandler, int i, int x, int y) {
        super(itemHandler, i, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return OvenTileEntity.isItemFuel(itemStack);
    }

}
