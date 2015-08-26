package net.blay09.mods.cookingbook.food;

import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class FoodRecipe {

    protected List<FoodIngredient> craftMatrix;
    protected ItemStack outputItem;

    public List<FoodIngredient> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public boolean isSmeltingRecipe() {
        return false;
    }

}