package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class FoodIngredient {

    private final NonNullList<ItemStack> itemStacks;
    private final boolean isToolItem;

    public FoodIngredient(ItemStack itemStack, boolean isToolItem) {
        this(NonNullList.withSize(1, itemStack), isToolItem);
    }

    public FoodIngredient(NonNullList<ItemStack> itemStacks, boolean isToolItem) {
        this.itemStacks = itemStacks;
        this.isToolItem = isToolItem;
    }

    public boolean isValidItem(ItemStack itemStack) {
        for(ItemStack oreStack : itemStacks) {
            if(ItemUtils.areItemStacksEqualWithWildcard(oreStack, itemStack)) {
                return true;
            }
        }
        return false;
    }

    public NonNullList<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public boolean isToolItem() {
        return isToolItem;
    }

}
