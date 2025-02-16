package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class DefaultKitchenItemProvider extends AbstractKitchenItemProvider {

    private Container container;
    private int[] usedStackSize;

    public DefaultKitchenItemProvider() {
    }

    public DefaultKitchenItemProvider(Container container) {
        this.container = container;
        this.usedStackSize = new int[container.getContainerSize()];
    }

    public void setContainer(Container container) {
        this.container = container;
        this.usedStackSize = new int[container.getContainerSize()];
    }

    @Override
    public void resetSimulation() {
        Arrays.fill(usedStackSize, 0);
    }

    @Override
    public int getSimulatedUseCount(int slot) {
        return usedStackSize[slot];
    }

    @Override
    public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
        // A slot of -1 means it's being infinitely provided by this block or an upgrade.
        if (slot == -1) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = container.getItem(slot);
        if (itemStack.getCount() - (simulate ? usedStackSize[slot] : 0) >= amount) {
            ItemStack result = ContainerUtils.extractItem(container, slot, amount, simulate);
            if (simulate && !result.isEmpty()) {
                usedStackSize[slot] += result.getCount();
            }

            return result;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
        ItemStack restStack = ContainerUtils.insertItem(container, sourceItem.getSourceSlot(), itemStack, false);
        if (!restStack.isEmpty()) {
            restStack = ContainerUtils.insertItemStacked(container, itemStack, false);
        }

        return restStack;
    }

    @Override
    public int getSlots() {
        return container.getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return container.getItem(slot);
    }

}
