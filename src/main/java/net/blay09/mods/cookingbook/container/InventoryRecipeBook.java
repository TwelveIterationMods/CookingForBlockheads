package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryRecipeBook implements IInventory {

    private final IFoodRecipe[] inventory = new IFoodRecipe[12];

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory[i] != null ? inventory[i].getOutputItem() : null;
    }

    @Override
    public ItemStack decrStackSize(int i, int stackSize) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {}

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
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }

    public void setFoodItem(int i, IFoodRecipe foodItem) {
        inventory[i] = foodItem;
    }

    public IFoodRecipe getFoodItem(int i) {
        return inventory[i];
    }
}
