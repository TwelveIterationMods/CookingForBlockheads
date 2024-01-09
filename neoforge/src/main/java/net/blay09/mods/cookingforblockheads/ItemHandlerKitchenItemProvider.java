package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Collection;

public class ItemHandlerKitchenItemProvider implements KitchenItemProvider {
    private final IItemHandler itemHandler;

    public ItemHandlerKitchenItemProvider(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotStack = itemHandler.getStackInSlot(itemHandlerIngredientToken.slot);
            if (ingredient.test(slotStack) && hasUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens)) {
                return itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            final var slotStack = itemHandler.getStackInSlot(i);
            if (ingredient.test(slotStack) && hasUsesLeft(i, slotStack, ingredientTokens)) {
                return new ItemHandlerIngredientToken(i);
            }
        }
        return null;
    }

    @Override
    public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotStack = itemHandler.getStackInSlot(itemHandlerIngredientToken.slot);
            if (ItemStack.isSameItemSameTags(slotStack, itemStack) && hasUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens)) {
                return itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            final var slotStack = itemHandler.getStackInSlot(i);
            if (ItemStack.isSameItemSameTags(slotStack, itemStack) && hasUsesLeft(i, slotStack, ingredientTokens)) {
                return new ItemHandlerIngredientToken(i);
            }
        }
        return null;
    }

    private boolean hasUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        var uses = slotStack.getCount();
        for (IngredientToken ingredientToken : ingredientTokens) {
            if (ingredientToken instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
                if (itemHandlerIngredientToken.slot == slot) {
                    uses--;
                }
            }
        }

        return uses > 0;
    }

    @Override
    public CacheHint getCacheHint(IngredientToken ingredientToken) {
        return ingredientToken instanceof ItemHandlerIngredientToken itemHandlerIngredientToken ? itemHandlerIngredientToken : CacheHint.NONE;
    }

    public class ItemHandlerIngredientToken implements IngredientToken, CacheHint {
        private final int slot;

        public ItemHandlerIngredientToken(int slot) {
            this.slot = slot;
        }

        @Override
        public ItemStack peek() {
            return itemHandler.getStackInSlot(slot);
        }

        @Override
        public ItemStack consume() {
            return itemHandler.extractItem(slot, 1, false);
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            final var restItem = itemHandler.insertItem(slot, itemStack, false);
            if (!restItem.isEmpty()) {
                return ItemHandlerHelper.insertItemStacked(itemHandler, restItem, false);
            }

            return ItemStack.EMPTY;
        }
    }
}
