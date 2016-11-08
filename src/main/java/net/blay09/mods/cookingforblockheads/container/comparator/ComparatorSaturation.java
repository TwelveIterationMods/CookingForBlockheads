package net.blay09.mods.cookingforblockheads.container.comparator;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;

import java.util.Comparator;

public class ComparatorSaturation implements Comparator<FoodRecipeWithStatus> {

    private final ComparatorName fallback = new ComparatorName();
    private final EntityPlayer entityPlayer;

    public ComparatorSaturation(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    @Override
    public int compare(FoodRecipeWithStatus o1, FoodRecipeWithStatus o2) {
        boolean isFirstFood = o1.getOutputItem().getItem() instanceof ItemFood;
        boolean isSecondFood = o2.getOutputItem().getItem() instanceof ItemFood;
        if(!isFirstFood && !isSecondFood) {
            return 0;
        } else if(!isFirstFood) {
            return 1;
        } else if(!isSecondFood) {
            return -1;
        }
        int result = (int) (CookingForBlockheadsAPI.getFoodStatsProvider().getSaturation(o2.getOutputItem(), entityPlayer) * 100 - CookingForBlockheadsAPI.getFoodStatsProvider().getSaturation(o1.getOutputItem(), entityPlayer) * 100);
        if(result == 0) {
            return fallback.compare(o1, o2);
        }
        return result;
    }

}
