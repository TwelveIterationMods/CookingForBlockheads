package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;

import java.util.Comparator;

public class ComparatorName implements Comparator<FoodRecipeWithStatus> {

    @Override
    public int compare(FoodRecipeWithStatus o1, FoodRecipeWithStatus o2) {
        String s1 = o1.getOutputItem().getDisplayName().getString();
        String s2 = o2.getOutputItem().getDisplayName().getString();
        return s1.compareToIgnoreCase(s2);
    }

}
