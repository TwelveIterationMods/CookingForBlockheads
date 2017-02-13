package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOvenInput extends SlotItemHandler {

    public SlotOvenInput(IItemHandler itemHandler, int i, int x, int y) {
        super(itemHandler, i, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !TileOven.getSmeltingResult(stack).isEmpty();
    }

}
