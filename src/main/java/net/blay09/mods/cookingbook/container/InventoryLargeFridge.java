package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.block.TileEntityFridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryLargeFridge implements IInventory {

    private final IInventory lowerFridge;
    private final IInventory upperFridge;

    public InventoryLargeFridge(IInventory lowerFridge, IInventory upperFridge) {
        this.lowerFridge = lowerFridge;
        this.upperFridge = upperFridge;
    }

    @Override
    public int getSizeInventory() {
        return lowerFridge.getSizeInventory() + upperFridge.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if(i < lowerFridge.getSizeInventory()) {
            return lowerFridge.getStackInSlot(i);
        } else {
            return upperFridge.getStackInSlot(i - lowerFridge.getSizeInventory());
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int amount) {
        if(i < lowerFridge.getSizeInventory()) {
            return lowerFridge.decrStackSize(i, amount);
        } else {
            return upperFridge.decrStackSize(i - lowerFridge.getSizeInventory(), amount);
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if(i < lowerFridge.getSizeInventory()) {
            return lowerFridge.getStackInSlotOnClosing(i);
        } else {
            return upperFridge.getStackInSlotOnClosing(i - lowerFridge.getSizeInventory());
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if(i < lowerFridge.getSizeInventory()) {
            lowerFridge.setInventorySlotContents(i, itemStack);
        } else {
            upperFridge.setInventorySlotContents(i - lowerFridge.getSizeInventory(), itemStack);
        }
    }

    @Override
    public String getInventoryName() {
        return lowerFridge.getInventoryName();
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
    public void markDirty() {
        lowerFridge.markDirty();
        upperFridge.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return lowerFridge.isUseableByPlayer(player) && upperFridge.isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {
        lowerFridge.openInventory();
        upperFridge.openInventory();
    }

    @Override
    public void closeInventory() {
        lowerFridge.closeInventory();
        upperFridge.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return lowerFridge.isItemValidForSlot(i, itemStack) && upperFridge.isItemValidForSlot(i, itemStack);
    }

    public boolean containsInventory(IInventory inventory) {
        return lowerFridge == inventory || upperFridge == inventory;
    }
}
