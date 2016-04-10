package net.blay09.mods.cookingforblockheads.addon;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class InfiniteItemProvider implements IKitchenItemProvider {
    private final List<ItemStack> itemStacks = Lists.newArrayList();

    public InfiniteItemProvider(ItemStack... providedStacks) {
        for (ItemStack providedStack : providedStacks) {
            itemStacks.add(ItemHandlerHelper.copyStackWithSize(providedStack, 64));
        }
    }

    @Override
    public void resetSimulation() {
    }

    @Override
    public ItemStack useItemStack(int slot, int amount, boolean simulate) {
        return ItemHandlerHelper.copyStackWithSize(getStackInSlot(slot), amount);
    }

    @Override
    public ItemStack returnItemStack(ItemStack itemStack) {
        return null;
    }

    @Override
    public int getSlots() {
        return itemStacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemStacks.get(slot);
    }
}
