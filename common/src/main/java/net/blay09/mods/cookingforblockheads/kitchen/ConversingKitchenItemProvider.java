package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class ConversingKitchenItemProvider extends ContainerKitchenItemProvider {
    public ConversingKitchenItemProvider(Container container) {
        super(container);
    }

    @Override
    protected int getUsesLeft(int slot, ItemStack slotStack, Collection<IngredientToken> ingredientTokens) {
        return super.getUsesLeft(slot, slotStack, ingredientTokens) - 1;
    }
}
