package net.blay09.mods.cookingbook.food.ingredient;

import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.minecraft.item.ItemStack;

public class DefaultIngredient implements IFoodIngredient {

    private final ItemStack itemStack;

    public DefaultIngredient(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean isValidItem(ItemStack itemStack) {
        if(itemStack == null) {
            return false;
        }
        return this.itemStack.getHasSubtypes() ? this.itemStack.isItemEqual(itemStack) : this.itemStack.getItem() == itemStack.getItem();
    }

    @Override
    public ItemStack[] getItemStacks() {
        return new ItemStack[] { itemStack };
    }

    @Override
    public boolean isOptional() {
        return false;
    }
}
