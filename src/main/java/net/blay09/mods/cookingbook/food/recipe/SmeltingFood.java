package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.food.ingredient.DefaultIngredient;
import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class SmeltingFood implements IFoodRecipe {

    private IFoodIngredient[] craftMatrix;
    private ItemStack sourceStack;
    private ItemStack resultStack;

    public SmeltingFood(ItemStack resultStack, ItemStack sourceStack) {
        this.sourceStack = sourceStack;
        this.resultStack = resultStack;
        craftMatrix = new IFoodIngredient[] { new DefaultIngredient(sourceStack) };
    }

    @Override
    public IFoodIngredient[] getCraftMatrix() {
        return craftMatrix;
    }

    @Override
    public ItemStack getOutputItem() {
        return resultStack;
    }

    @Override
    public boolean isSmeltingRecipe() {
        return true;
    }

    @Override
    public IRecipe getCraftingRecipe() {
        return null;
    }

}
