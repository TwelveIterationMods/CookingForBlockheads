package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface FoodStatsProvider {
    float getSaturation(ItemStack itemStack, PlayerEntity entityPlayer);

    int getFoodLevel(ItemStack itemStack, PlayerEntity entityPlayer);
}
