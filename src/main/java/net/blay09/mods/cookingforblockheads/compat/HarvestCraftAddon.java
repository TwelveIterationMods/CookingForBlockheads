package net.blay09.mods.cookingforblockheads.compat;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

public class HarvestCraftAddon {

    public static boolean isWeirdConversionRecipe(IRecipe recipe) {
        if(recipe.getIngredients().size() == 2 && recipe.getRecipeOutput().getCount() == 2) {
            Ingredient first = recipe.getIngredients().get(0);
            Ingredient second = recipe.getIngredients().get(1);
            if(first.apply(recipe.getRecipeOutput()) && second.apply(recipe.getRecipeOutput())) {
                return true;
            }
        }
        return false;
    }

}
