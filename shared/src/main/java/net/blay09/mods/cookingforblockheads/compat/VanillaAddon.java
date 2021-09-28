package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.FoodStatsProvider;
import net.blay09.mods.cookingforblockheads.api.SinkHandler;
import net.blay09.mods.cookingforblockheads.api.ToasterHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Optional;

public class VanillaAddon implements FoodStatsProvider {

    public VanillaAddon() {
        /*SinkHandler simpleHandler = itemStack -> {
            ItemStack result = itemStack.copy();
            result.setCount(1);
            result.setItemDamage(0);
            return result;
        };
        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Blocks.WOOL), simpleHandler);
        CookingForBlockheadsAPI.addSinkHandler(new ItemStack(Blocks.CARPET), simpleHandler);*/

        SinkHandler armorHandler = itemStack -> {
            if (itemStack.getItem() instanceof IDyeableArmorItem) {
                ((IDyeableArmorItem) itemStack.getItem()).removeColor(itemStack);
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
    public float getSaturation(ItemStack itemStack, PlayerEntity entityPlayer) {
        return Optional.ofNullable(itemStack.getItem().getFood()).map(Food::getSaturation).orElse(0f);
    }

    @Override
    public int getFoodLevel(ItemStack itemStack, PlayerEntity entityPlayer) {
        return Optional.ofNullable(itemStack.getItem().getFood()).map(Food::getHealing).orElse(0);
    }

}
