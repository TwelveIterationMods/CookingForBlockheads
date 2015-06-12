package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryRecipeBook implements IInventory {

    private final List[] recipes = new List[12];
    private final ItemStack[] outputItem = new ItemStack[12];

    @Override
    public int getSizeInventory() {
        return recipes.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return outputItem[i];
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
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }

    public void setFoodItem(int i, List<IFoodRecipe> recipes) {
        this.recipes[i] = recipes;
        if(recipes != null) {
            outputItem[i] = recipes.get(0).getOutputItem();
        } else {
            outputItem[i] = null;
        }
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

    public List<IFoodRecipe> getFoodList(int slotIndex) {
        return (List<IFoodRecipe>) recipes[slotIndex];
    }
}
