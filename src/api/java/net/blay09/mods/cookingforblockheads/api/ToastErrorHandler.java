package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/**
 * @deprecated do not use, it will be ignored. to be removed in 1.13
 */
@Deprecated
public interface ToastErrorHandler extends ToastHandler {
    ITextComponent getToasterHint(EntityPlayer player, ItemStack itemStack);
}
