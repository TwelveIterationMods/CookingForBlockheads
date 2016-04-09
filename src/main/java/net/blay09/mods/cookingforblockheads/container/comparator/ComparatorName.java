package net.blay09.mods.cookingforblockheads.container.comparator;

import net.blay09.mods.cookingforblockheads.container.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;

import java.util.Comparator;

public class ComparatorName implements Comparator<FoodRecipeWithStatus> {

    @Override
    public int compare(FoodRecipeWithStatus o1, FoodRecipeWithStatus o2) {
        String s1 = o1.getOutputItem().getDisplayName();
        String s2 = o2.getOutputItem().getDisplayName();
        return s1.compareToIgnoreCase(s2);
    }

}
