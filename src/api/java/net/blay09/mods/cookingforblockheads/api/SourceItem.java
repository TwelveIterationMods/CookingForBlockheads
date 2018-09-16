package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SourceItem {

    private final IKitchenItemProvider sourceProvider;
    private final int sourceSlot;
    private final ItemStack sourceStack;

    public SourceItem(@Nullable IKitchenItemProvider sourceProvider, int sourceSlot, ItemStack sourceStack) {
        this.sourceProvider = sourceProvider;
        this.sourceSlot = sourceSlot;
        this.sourceStack = sourceStack;
    }

    @Nullable
    public IKitchenItemProvider getSourceProvider() {
        return sourceProvider;
    }

    public int getSourceSlot() {
        return sourceSlot;
    }

    public ItemStack getSourceStack() {
        return sourceStack;
    }

}
