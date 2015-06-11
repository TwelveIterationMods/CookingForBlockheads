package net.blay09.mods.cookingbook.food;

import net.minecraft.item.ItemStack;

public interface IFoodIngredient {
    boolean isValidItem(ItemStack itemStack);
    ItemStack[] getItemStacks();
    boolean isOptional();
}
