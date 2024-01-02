package net.blay09.mods.cookingforblockheads.menu.slot;

import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotOvenFuel extends Slot {

    public SlotOvenFuel(Container container, int i, int x, int y) {
        super(container, i, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return OvenBlockEntity.isItemFuel(itemStack);
    }

}
