package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface FoodStatsProvider {
    float getSaturation(ItemStack itemStack, EntityPlayer entityPlayer);
    int getFoodLevel(ItemStack itemStack, EntityPlayer entityPlayer);
}
