package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface ToastOutputHandler extends ToastHandler {
    ItemStack getToasterOutput(ItemStack itemStack);
}
