package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class KitchenItemProvider extends DefaultKitchenItemProvider {

    private IItemHandler itemHandler;
    private int[] usedStackSize;

    public KitchenItemProvider() {
    }

    public KitchenItemProvider(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
        this.usedStackSize = new int[itemHandler.getSlots()];
    }

    public void setItemHandler(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
        this.usedStackSize = new int[itemHandler.getSlots()];
    }

    @Override
    public void resetSimulation() {
        for (int i = 0; i < usedStackSize.length; i++) {
            usedStackSize[i] = 0;
        }
    }

    @Override
    public int getSimulatedUseCount(int slot) {
        return usedStackSize[slot];
    }

    @Override
    public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
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
