package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.ItemStack;

public interface IngredientToken {
    ItemStack peek();
    ItemStack consume();
    ItemStack restore(ItemStack itemStack);

    IngredientToken EMPTY = new IngredientToken() {
        @Override
        public ItemStack peek() {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack consume() {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            return itemStack;
        }
    };
}
