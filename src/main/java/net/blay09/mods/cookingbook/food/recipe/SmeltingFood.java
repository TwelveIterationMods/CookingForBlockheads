package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class SmeltingFood extends FoodRecipe {

    public SmeltingFood(ItemStack outputItem, ItemStack sourceStack) {
        this.outputItem = outputItem;
        this.craftMatrix = new ArrayList<>();
        this.craftMatrix.add(new FoodIngredient(sourceStack, false));
    }

    @Override
    public boolean isSmeltingRecipe() {
        return true;
    }

}
