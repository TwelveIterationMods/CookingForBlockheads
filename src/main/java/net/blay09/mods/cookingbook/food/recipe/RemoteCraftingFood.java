package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.minecraft.item.ItemStack;

public class RemoteCraftingFood extends FoodRecipe {

    private final boolean isSmeltingRecipe;

    public RemoteCraftingFood(ItemStack outputItem, FoodIngredient[] craftMatrix, boolean isSmeltingRecipe) {
        this.outputItem = outputItem;
        this.craftMatrix = craftMatrix;
        this.isSmeltingRecipe = isSmeltingRecipe;
    }

    @Override
    public boolean isSmeltingRecipe() {
        return isSmeltingRecipe;
    }
}
