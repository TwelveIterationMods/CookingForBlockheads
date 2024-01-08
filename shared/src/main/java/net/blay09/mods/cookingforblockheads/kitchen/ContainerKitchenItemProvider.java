package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;

public class ContainerKitchenItemProvider implements KitchenItemProvider {

    private final Container container;

    public ContainerKitchenItemProvider(Container container) {
        this.container = container;
    }

    @Override
    public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            final var slotStack = container.getItem(i);
            if (ingredient.test(slotStack) && hasUsesLeft(i, slotStack, ingredientTokens)) {
                return new ContainerIngredientToken(i);
            }
        }
        return null;
    }

    @Override
    public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            final var slotStack = container.getItem(i);
            if (ItemStack.isSameItemSameTags(slotStack, itemStack) && hasUsesLeft(i, slotStack, ingredientTokens)) {
                return new ContainerIngredientToken(i);
            }
        }
        return null;
    }

    private boolean hasUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        var usesLeft = slotStack.getCount();
        for (IngredientToken ingredientToken : ingredientTokens) {
            if (ingredientToken instanceof ContainerIngredientToken containerIngredientToken) {
                if (containerIngredientToken.slot == slot) {
                    usesLeft--;
                }
            }
        }

        return usesLeft > 0;
    }

    public class ContainerIngredientToken implements IngredientToken {
        private final int slot;

        public ContainerIngredientToken(int slot) {
            this.slot = slot;
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
    }
}
