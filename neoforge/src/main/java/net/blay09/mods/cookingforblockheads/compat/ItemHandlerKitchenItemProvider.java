package net.blay09.mods.cookingforblockheads.compat;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Arrays;
import java.util.List;

public class ItemHandlerKitchenItemProvider extends AbstractKitchenItemProvider {
    private IItemHandler itemHandler;
    private int[] usedStackSize;

    public ItemHandlerKitchenItemProvider() {
    }

    public ItemHandlerKitchenItemProvider(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
        this.usedStackSize = new int[itemHandler.getSlots()];
    }

    public void setItemHandler(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
        this.usedStackSize = new int[itemHandler.getSlots()];
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

        ItemStack itemStack = itemHandler.getStackInSlot(slot);
        if (itemStack.getCount() - (simulate ? usedStackSize[slot] : 0) >= amount) {
            ItemStack result = itemHandler.extractItem(slot, amount, simulate);
            if (simulate && !result.isEmpty()) {
                usedStackSize[slot] += result.getCount();
            }

            return result;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
        ItemStack restStack = itemHandler.insertItem(sourceItem.getSourceSlot(), itemStack, false);
        if (!restStack.isEmpty()) {
            restStack = ItemHandlerHelper.insertItemStacked(itemHandler, itemStack, false);
        }

        return restStack;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }
}
