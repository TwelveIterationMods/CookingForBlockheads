package net.blay09.mods.cookingbook.food;

import net.minecraft.item.ItemStack;

public interface IFoodRecipe {

    IFoodIngredient[] getCraftMatrix();
    ItemStack getOutputItem();
    boolean isSmeltingRecipe();

}
