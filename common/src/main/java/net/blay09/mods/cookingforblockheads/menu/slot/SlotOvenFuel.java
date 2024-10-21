package net.blay09.mods.cookingforblockheads.menu.slot;

import net.blay09.mods.cookingforblockheads.menu.OvenMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotOvenFuel extends Slot {

    private final OvenMenu menu;

    public SlotOvenFuel(OvenMenu menu, Container container, int i, int x, int y) {
        super(container, i, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return menu.isFuel(itemStack);
    }

}
