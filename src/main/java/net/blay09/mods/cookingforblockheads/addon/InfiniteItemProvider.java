package net.blay09.mods.cookingforblockheads.addon;

import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class InfiniteItemProvider implements IKitchenItemProvider {
    private final ItemStackHandler itemHandler;

    public InfiniteItemProvider(ItemStack... itemStacks) {
        itemHandler = new ItemStackHandler(itemStacks.length);
        for(int i = 0; i < itemStacks.length; i++) {
            ItemStack itemStack = itemStacks[i].copy();
            itemStack.stackSize = 64;
            itemHandler.setStackInSlot(i, itemStack);
        }
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}
