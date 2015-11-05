package net.blay09.mods.cookingbook.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface FoodStatsProvider {

    float getSaturation(ItemStack itemStack, EntityPlayer entityPlayer);
    int getFoodLevel(ItemStack itemStack, EntityPlayer entityPlayer);

}
