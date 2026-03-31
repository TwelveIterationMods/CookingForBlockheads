package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.balm.world.ContainerUtils;
import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public class ContainerKitchenItemProvider implements KitchenItemProvider {

    private final Container container;

    public ContainerKitchenItemProvider(Container container) {
        this.container = container;
    }

    @Override
    public @Nullable IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (cacheHint instanceof ContainerIngredientToken containerIngredientToken) {
            final var slotStack = container.getItem(containerIngredientToken.slot);
            if (ingredient.test(slotStack) && hasUsesLeft(containerIngredientToken.slot, slotStack, ingredientTokens)) {
                return containerIngredientToken;
            }
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            final var slotStack = container.getItem(i);
            final var usesLeft = getUsesLeft(i, slotStack, ingredientTokens);
            if (ingredient.test(slotStack) && usesLeft > 0) {
                return new ContainerIngredientToken(i, greedy ? usesLeft : 1);
            }
        }
        return null;
    }

    @Override
    public @Nullable IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (cacheHint instanceof ContainerIngredientToken containerIngredientToken) {
            final var slotStack = container.getItem(containerIngredientToken.slot);
            final var usesLeft = getUsesLeft(containerIngredientToken.slot, slotStack, ingredientTokens);
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && usesLeft > 0) {
                return greedy ? new ContainerIngredientToken(containerIngredientToken.slot, usesLeft) : containerIngredientToken;
            }
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            final var slotStack = container.getItem(i);
            final var usesLeft = getUsesLeft(i, slotStack, ingredientTokens);
            if (ItemStack.isSameItemSameComponents(slotStack, itemStack) && usesLeft > 0) {
                return new ContainerIngredientToken(i, greedy ? usesLeft : 1);
            }
        }
        return null;
    }

    protected int getUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        var usesLeft = slotStack.getCount();
        for (IngredientToken ingredientToken : ingredientTokens) {
            if (ingredientToken instanceof ContainerIngredientToken containerIngredientToken) {
                if (containerIngredientToken.slot == slot) {
                    usesLeft -= containerIngredientToken.reservedCount();
                }
            }
        }

        return usesLeft;
    }

    @Override
    public CacheHint getCacheHint(IngredientToken ingredientToken) {
        return ingredientToken instanceof ContainerIngredientToken containerIngredientToken ? containerIngredientToken : CacheHint.NONE;
    }

    private boolean hasUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        return getUsesLeft(slot, slotStack, ingredientTokens) > 0;
    }

    public class ContainerIngredientToken implements IngredientToken, CacheHint {
        private final int slot;
        private final int count;

        public ContainerIngredientToken(int slot, int count) {
            this.slot = slot;
            this.count = count;
        }

        @Override
        public ItemStack peek() {
            return container.getItem(slot);
        }

        @Override
        public ItemStack consume() {
            return ContainerUtils.extractItem(container, slot, 1, false);
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            final var restItem = ContainerUtils.insertItem(container, slot, itemStack, false);
            if (!restItem.isEmpty()) {
                return ContainerUtils.insertItemStacked(container, restItem, false);
            }

            return ItemStack.EMPTY;
        }

        @Override
        public int reservedCount() {
            return count;
        }
    }
}
