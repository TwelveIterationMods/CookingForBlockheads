package net.blay09.mods.cookingbook.food;

import net.minecraft.item.ItemStack;

public interface IFoodIngredient {
    boolean isValidItem(ItemStack itemStack);
    int getStackSize();
    void increaseStackSize(int stackSize);
    IFoodIngredient copy();
    ItemStack getItemStack();
    boolean isOptional();
}
