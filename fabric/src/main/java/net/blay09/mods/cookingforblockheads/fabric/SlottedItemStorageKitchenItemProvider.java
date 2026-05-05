package net.blay09.mods.cookingforblockheads.fabric;

import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;

public record SlottedItemStorageKitchenItemProvider(SlottedStorage<ItemVariant> storage) implements KitchenItemProvider {
    @Override
    public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        final var greedy = false;
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotStorage = storage.getSlot(itemHandlerIngredientToken.slot);
            final var slotResource = slotStorage.getResource();
            final var slotStack = slotResource.toStack((int) slotStorage.getAmount());
            final var usesLeft = getUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens);
            if (ingredient.test(slotStack) && usesLeft > 0) {
                return greedy ? new ItemHandlerIngredientToken(itemHandlerIngredientToken.slot, usesLeft) : itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < storage.getSlotCount(); i++) {
            final var slotStorage = storage.getSlot(i);
            final var slotResource = slotStorage.getResource();
            final var slotStack = slotResource.toStack((int) slotStorage.getAmount());
            final var usesLeft = getUsesLeft(i, slotStack, ingredientTokens);
            if (ingredient.test(slotStack) && usesLeft > 0) {
                return new ItemHandlerIngredientToken(i, greedy ? usesLeft : 1);
            }
        }
        return null;
    }

    @Override
    public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        final var greedy = false;
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotResource = storage.getSlot(itemHandlerIngredientToken.slot).getResource();
            final var slotStack = slotResource.toStack();
            final var usesLeft = getUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens);
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && hasUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens)) {
                return greedy ? new ItemHandlerIngredientToken(itemHandlerIngredientToken.slot, usesLeft) : itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < storage.getSlotCount(); i++) {
            final var slotResource = storage.getSlot(i).getResource();
            final var slotStack = slotResource.toStack();
            final var usesLeft = getUsesLeft(i, slotStack, ingredientTokens);
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && usesLeft > 0) {
                return new ItemHandlerIngredientToken(i, greedy ? usesLeft : 1);
            }
        }
        return null;
    }

    private boolean hasUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        return getUsesLeft(slot, slotStack, ingredientTokens) > 0;
    }

    private int getUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        var usesLeft = slotStack.getCount();
        for (IngredientToken ingredientToken : ingredientTokens) {
            if (ingredientToken instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
                if (itemHandlerIngredientToken.slot == slot) {
                    usesLeft -= itemHandlerIngredientToken.reservedCount();
                }
            }
        }

        return usesLeft;
    }

    @Override
    public CacheHint getCacheHint(IngredientToken ingredientToken) {
        return ingredientToken instanceof ItemHandlerIngredientToken itemHandlerIngredientToken ? itemHandlerIngredientToken : CacheHint.NONE;
    }

    public class ItemHandlerIngredientToken implements IngredientToken, CacheHint {
        private final int slot;
        private final int count;

        public ItemHandlerIngredientToken(int slot, int count) {
            this.slot = slot;
            this.count = count;
        }

        @Override
        public ItemStack peek() {
            return storage.getSlot(slot).getResource().toStack();
        }

        @Override
        public ItemStack consume() {
            final var slotResource = storage.getSlot(slot).getResource();
            try (final var transaction = Transaction.openOuter()) {
                final var count = storage.getSlot(slot).extract(slotResource, 1, transaction);
                transaction.commit();
                return slotResource.toStack((int) count);
            }
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            try (final var transaction = Transaction.openOuter()) {
                var restCount = itemStack.getCount();
                restCount -= (int) storage.getSlot(slot).insert(ItemVariant.of(itemStack), itemStack.getCount(), transaction);
                if (restCount > 0) {
                    restCount -= (int) storage.insert(ItemVariant.of(itemStack), restCount, transaction);
                }
                transaction.commit();

                return restCount > 0 ? itemStack.copyWithCount(restCount) : ItemStack.EMPTY;
            }
        }

        public int reservedCount() {
            return count;
        }
    }
}
