package net.blay09.mods.cookingforblockheads.block.entity.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class TransferableContainer {
    private final NonNullList<ItemStack> items;

    private TransferableContainer(Container container) {
        items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.set(i, container.getItem(i));
        }
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void applyTo(Container container) {
        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }
    }

    public static TransferableContainer copyAndClear(Container container) {
        final var data = new TransferableContainer(container);
        container.clearContent();
        return data;
    }
}
