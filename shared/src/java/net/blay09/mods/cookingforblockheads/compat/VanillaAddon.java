package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.FoodStatsProvider;
import net.blay09.mods.cookingforblockheads.api.SinkHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class VanillaAddon implements FoodStatsProvider {

    public VanillaAddon() {
        SinkHandler armorHandler = itemStack -> {
            if (itemStack.getItem() instanceof DyeableLeatherItem) {
                ((DyeableLeatherItem) itemStack.getItem()).clearColor(itemStack);
            }
            return itemStack;
        };
        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Items.LEATHER_BOOTS), armorHandler);
        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Items.LEATHER_CHESTPLATE), armorHandler);
        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Items.LEATHER_HELMET), armorHandler);
        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Items.LEATHER_LEGGINGS), armorHandler);

        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Items.MILK_BUCKET), itemStack -> new ItemStack(Items.BUCKET));

        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Items.POTION), itemStack -> new ItemStack(Items.GLASS_BOTTLE));

        CookingForBlockheadsAPI.setFoodStatsProvider(this);
    }

    @Override
    public float getSaturation(ItemStack itemStack, Player entityPlayer) {
        return Optional.ofNullable(itemStack.getItem().getFoodProperties()).map(FoodProperties::getSaturationModifier).orElse(0f);
    }

    @Override
    public int getFoodLevel(ItemStack itemStack, Player entityPlayer) {
        return Optional.ofNullable(itemStack.getItem().getFoodProperties()).map(FoodProperties::getNutrition).orElse(0);
    }

}
