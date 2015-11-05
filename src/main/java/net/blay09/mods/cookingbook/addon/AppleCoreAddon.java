package net.blay09.mods.cookingbook.addon;

import net.blay09.mods.cookingbook.api.CookingAPI;
import net.blay09.mods.cookingbook.api.FoodStatsProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodValues;

public class AppleCoreAddon implements FoodStatsProvider {

    public AppleCoreAddon() {
        CookingAPI.setFoodStatsProvider(this);
    }

    @Override
    public float getSaturation(ItemStack itemStack, EntityPlayer entityPlayer) {
        return FoodValues.get(itemStack, entityPlayer).getSaturationIncrement();
    }

    @Override
    public int getFoodLevel(ItemStack itemStack, EntityPlayer entityPlayer) {
        return FoodValues.get(itemStack, entityPlayer).hunger;
    }

}
