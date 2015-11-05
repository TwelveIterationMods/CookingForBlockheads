package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.api.CookingAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class ComparatorSaturation implements Comparator<ItemStack> {

    private final ComparatorName fallback = new ComparatorName();
    private final EntityPlayer entityPlayer;

    public ComparatorSaturation(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        boolean isFirstFood = o1.getItem() instanceof ItemFood;
        boolean isSecondFood = o2.getItem() instanceof ItemFood;
        if(!isFirstFood && !isSecondFood) {
            return 0;
        } else if(!isFirstFood) {
            return 1;
        } else if(!isSecondFood) {
            return -1;
        }
        int result = (int) (CookingAPI.getFoodStatsProvider().getSaturation(o2, entityPlayer) * 100 - CookingAPI.getFoodStatsProvider().getSaturation(o1, entityPlayer) * 100);
        if(result == 0) {
            return fallback.compare(o1, o2);
        }
        return result;
    }

}
