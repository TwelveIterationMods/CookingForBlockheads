package net.blay09.mods.cookingbook.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotOvenFuel extends Slot {

    public SlotOvenFuel(IInventory inventory, int i, int x, int y) {
        super(inventory, i, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return TileEntityFurnace.isItemFuel(itemStack);
    }

}
