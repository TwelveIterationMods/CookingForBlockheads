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
    public @Nullable IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotResource = itemHandler.getResource(itemHandlerIngredientToken.slot);
            final var slotStack = slotResource.toStack();
            if (ingredient.test(slotStack) && hasUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens)) {
                return itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < itemHandler.size(); i++) {
            final var slotResource = itemHandler.getResource(i);
            final var slotStack = slotResource.toStack();
            if (ingredient.test(slotStack) && hasUsesLeft(i, slotStack, ingredientTokens)) {
                return new ItemHandlerIngredientToken(i);
            }
        }
        return null;
    }

    @Override
    public @Nullable IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        if (cacheHint instanceof ItemHandlerIngredientToken itemHandlerIngredientToken) {
            final var slotResource = itemHandler.getResource(itemHandlerIngredientToken.slot);
            final var slotStack = slotResource.toStack();
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && hasUsesLeft(itemHandlerIngredientToken.slot, slotStack, ingredientTokens)) {
                return itemHandlerIngredientToken;
            }
        }

        for (int i = 0; i < itemHandler.size(); i++) {
            final var slotResource = itemHandler.getResource(i);
            final var slotStack = slotResource.toStack();
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && hasUsesLeft(i, slotStack, ingredientTokens)) {
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
    }
}
