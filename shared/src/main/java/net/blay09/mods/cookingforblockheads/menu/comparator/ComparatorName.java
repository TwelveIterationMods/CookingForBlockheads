package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;

import java.util.Comparator;

public class ComparatorName implements Comparator<RecipeWithStatus> {

    @Override
    public int compare(RecipeWithStatus o1, RecipeWithStatus o2) {
        String s1 = o1.getOutputItem().getDisplayName().getString();
        String s2 = o2.getOutputItem().getDisplayName().getString();
        return s1.compareToIgnoreCase(s2);
    }

}
