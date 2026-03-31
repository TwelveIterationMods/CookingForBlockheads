package net.blay09.mods.cookingforblockheads.neoforge;

import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public record ResourceHandlerKitchenItemProvider(
        ResourceHandler<ItemResource> itemHandler) implements KitchenItemProvider {

    @Override
    public @Nullable IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotResource = itemHandler.getResource(itemHandlerIngredientToken.slot);
            final var slotStack = slotResource.toStack();
            final var usesLeft = getUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens);
            if (ingredient.test(slotStack) && usesLeft > 0) {
                return greedy ? new ItemHandlerIngredientToken(itemHandlerIngredientToken.slot, usesLeft) : itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < itemHandler.size(); i++) {
            final var slotResource = itemHandler.getResource(i);
            final var slotStack = slotResource.toStack();
            final var usesLeft = getUsesLeft(i, slotStack, ingredientTokens);
            if (ingredient.test(slotStack) && usesLeft > 0) {
                return new ItemHandlerIngredientToken(i, greedy ? usesLeft : 1);
            }
        }
        return null;
    }

    @Override
    public @Nullable IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotResource = itemHandler.getResource(itemHandlerIngredientToken.slot);
            final var slotStack = slotResource.toStack();
            final var usesLeft = getUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens);
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && hasUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens)) {
                return greedy ? new ItemHandlerIngredientToken(itemHandlerIngredientToken.slot, usesLeft) : itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < itemHandler.size(); i++) {
            final var slotResource = itemHandler.getResource(i);
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
            return itemHandler.getResource(slot).toStack();
        }

        @Override
        public ItemStack consume() {
            final var slotResource = itemHandler.getResource(slot);
            try (final var transaction = Transaction.open(null)) {
                int count = itemHandler.extract(slot, slotResource, 1, transaction);
                transaction.commit();
                return slotResource.toStack(count);
            }
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            try (final var transaction = Transaction.open(null)) {
                var restCount = itemHandler.insert(slot, ItemResource.of(itemStack), itemStack.getCount(), transaction);
                if (restCount > 0) {
                    restCount = itemHandler.insert(ItemResource.of(itemStack), itemStack.getCount(), transaction);
                }
                transaction.commit();

                return restCount > 0 ? itemStack.copyWithCount(restCount) : ItemStack.EMPTY;
            }
        }

        @Override
        public int reservedCount() {
            return count;
        }
    }
}
