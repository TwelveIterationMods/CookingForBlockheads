package net.blay09.mods.cookingbook.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryRecipeBookMatrix implements IInventory {

    private final ItemStack[] inventory = new ItemStack[9];

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int stackSize) {
        if(inventory[i] != null) {
            if(inventory[i].stackSize <= stackSize) {
                ItemStack itemStack = inventory[i];
                inventory[i] = null;
                return itemStack;
            } else {
                ItemStack itemStack = inventory[i].splitStack(stackSize);
                if(inventory[i].stackSize == 0) {
                    inventory[i] = null;
                }
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory[i] = itemStack;
    }

    @Override
    public String getInventoryName() {
        return "cookingbook:recipebook";
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
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }

}
