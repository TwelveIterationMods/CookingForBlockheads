package net.blay09.mods.cookingbook.food;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IFoodRecipe {

    IFoodIngredient[] getCraftMatrix();
    ItemStack getOutputItem();
    boolean isSmeltingRecipe();
    IRecipe getCraftingRecipe();

}
