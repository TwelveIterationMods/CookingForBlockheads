package net.blay09.mods.cookingbook.food;

import net.minecraft.item.ItemStack;

public abstract class FoodRecipe {

    protected FoodIngredient[] craftMatrix;
    protected ItemStack outputItem;

    public FoodIngredient[] getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public boolean isSmeltingRecipe() {
        return false;
    }

}