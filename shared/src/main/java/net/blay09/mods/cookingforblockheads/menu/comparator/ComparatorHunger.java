package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

public class ComparatorHunger implements Comparator<RecipeWithStatus> {

    private final ComparatorName fallback = new ComparatorName();
    private final Player player;

    public ComparatorHunger(Player player) {
        this.player = player;
    }

    @Override
    public int compare(RecipeWithStatus o1, RecipeWithStatus o2) {
        boolean isFirstFood = o1.resultItem().getItem().isEdible();
        boolean isSecondFood = o2.resultItem().getItem().isEdible();
        if (!isFirstFood && !isSecondFood) {
            return fallback.compare(o1, o2);
        } else if (!isFirstFood) {
            return 1;
        } else if (!isSecondFood) {
            return -1;
        }

        int result = CookingForBlockheadsAPI.getFoodStatsProvider().getNutrition(o2.resultItem(), player) - CookingForBlockheadsAPI.getFoodStatsProvider().getNutrition(o1.resultItem(), player);
        if (result == 0) {
            return fallback.compare(o1, o2);
        }

        return result;
    }

}
