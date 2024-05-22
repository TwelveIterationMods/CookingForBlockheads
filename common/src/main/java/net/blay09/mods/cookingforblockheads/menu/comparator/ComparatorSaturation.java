package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

public class ComparatorSaturation implements Comparator<RecipeWithStatus> {

    private final ComparatorName fallback = new ComparatorName();
    private final Player entityPlayer;

    public ComparatorSaturation(Player entityPlayer) {
        this.entityPlayer = entityPlayer;
    }

    @Override
    public int compare(RecipeWithStatus o1, RecipeWithStatus o2) {
        boolean isFirstFood = o1.resultItem().has(DataComponents.FOOD);
        boolean isSecondFood = o2.resultItem().has(DataComponents.FOOD);
        if (!isFirstFood && !isSecondFood) {
            return fallback.compare(o1, o2);
        } else if (!isFirstFood) {
            return 1;
        } else if (!isSecondFood) {
            return -1;
        }

        final var foodStatsProvider = CookingForBlockheadsAPI.getFoodStatsProvider();
        int result = (int) (foodStatsProvider.getSaturationModifier(o2.resultItem(), entityPlayer) * 100 - foodStatsProvider.getSaturationModifier(o1.resultItem(), entityPlayer) * 100);
        if (result == 0) {
            return fallback.compare(o1, o2);
        }

        return result;
    }

}
