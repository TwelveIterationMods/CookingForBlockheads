package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class FoodRecipe {

    protected List<FoodIngredient> craftMatrix;
    protected ItemStack outputItem;
    protected int recipeWidth = 3;

    public List<FoodIngredient> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public RecipeType getType() {
        return RecipeType.CRAFTING;
    }

    public int getRecipeWidth() {
        return recipeWidth;
    }

    public ResourceLocation getRegistryName() {
        return outputItem.getItem().getRegistryName();
    }
}