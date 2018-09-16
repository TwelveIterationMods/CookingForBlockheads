package net.blay09.mods.cookingforblockheads.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class NonOccupyingItemHandler implements IItemHandler {

    private final IItemHandler itemHandler;

    public NonOccupyingItemHandler(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (itemHandler.extractItem(slot, 1, true).isEmpty()) {
            return stack;
        }

        return itemHandler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

}
