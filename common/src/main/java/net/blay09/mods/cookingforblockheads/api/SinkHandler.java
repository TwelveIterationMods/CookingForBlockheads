package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.ItemStack;

public interface SinkHandler {
    ItemStack getSinkOutput(ItemStack itemStack);
}
