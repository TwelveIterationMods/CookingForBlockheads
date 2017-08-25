package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.item.ItemStack;

@Deprecated
public interface ToastOutputHandler extends ToastHandler {
    ItemStack getToasterOutput(ItemStack itemStack);
}
