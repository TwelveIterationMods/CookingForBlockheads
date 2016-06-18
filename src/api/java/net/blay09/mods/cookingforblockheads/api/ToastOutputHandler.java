package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public interface ToastOutputHandler extends ToastHandler {

    ItemStack getToasterOutput(ItemStack itemStack);

}
