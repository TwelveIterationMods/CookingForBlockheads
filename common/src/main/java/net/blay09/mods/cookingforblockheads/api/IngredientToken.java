package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.ItemStack;

/**
 * Ingredient tokens are references towards ingredients to be used in a crafting operation.
 * <p>
 * You should return a custom implementation of this interface with properties specific to your context, i.e. a slot index or similar.
 */
public interface IngredientToken {
    /**
     * @return the item stack referenced by this token, but without removing it from its source
     */
    ItemStack peek();

    /**
     * @return the item stack referenced by this token, consuming it from its source. Remember to use a substack or return copy if you're going to shrink() the source.
     */
    ItemStack consume();

    /**
     * This is both used for failed operations (e.g. oven already full), as well as returning remaining items of operations.
     * @param itemStack the item to be returned to the source inventory
     * @return the rest that could not be returned to the source inventory, or ItemStack.EMPTY if all was returned
     */
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
