package net.blay09.mods.cookingbook.food.ingredient;

import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class OreDictIngredient implements IFoodIngredient {

    private final List<ItemStack> validItemList;
    private int stackSize;

    public OreDictIngredient(List<ItemStack> validItemList) {
        this(validItemList, 1);
    }

    private OreDictIngredient(List<ItemStack> validItemList, int stackSize) {
        this.validItemList = validItemList;
        this.stackSize = stackSize;
    }

    @Override
    public boolean isValidItem(ItemStack itemStack) {
        for(ItemStack oreStack : validItemList) {
            if(oreStack.getHasSubtypes() ? oreStack.isItemEqual(itemStack) : oreStack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getStackSize() {
        return stackSize;
    }

    @Override
    public void increaseStackSize(int stackSize) {
        this.stackSize += stackSize;
    }

    @Override
    public IFoodIngredient copy() {
        return new OreDictIngredient(validItemList, stackSize);
    }

    @Override
    public ItemStack[] getItemStacks() {
        return validItemList.toArray(new ItemStack[validItemList.size()]);
    }

    @Override
    public boolean isOptional() {
        return false;
    }
}
