package net.blay09.mods.cookingbook.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryFridge implements IInventory {

    private ItemStack[] inventory = new ItemStack[27];

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int count) {
        if(inventory[i] != null) {
            ItemStack itemStack;
            if (inventory[i].stackSize <= count) {
                itemStack = inventory[i];
                inventory[i] = null;
                return itemStack;
            } else {
                itemStack = inventory[i].splitStack(count);
                if (inventory[i].stackSize == 0) {
                    inventory[i] = null;
                }
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (inventory[i] != null) {
            ItemStack itemstack = inventory[i];
            inventory[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory[i] = itemStack;
    }

    @Override
    public String getInventoryName() {
        return "container.cookingbook:fridge";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slotId, ItemStack itemStack) {
        return true;
    }
}
