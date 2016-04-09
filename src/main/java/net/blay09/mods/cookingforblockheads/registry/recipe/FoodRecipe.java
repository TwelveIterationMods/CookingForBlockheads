package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class FoodRecipe {

    private final int id;
    protected List<FoodIngredient> craftMatrix;
    protected ItemStack outputItem;

    public FoodRecipe(int id) {
        this.id = id;
    }

    public List<FoodIngredient> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public RecipeType getType() {
        return RecipeType.CRAFTING;
    }

    public int getId() {
        return id;
    }
}