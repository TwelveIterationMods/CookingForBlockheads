package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.blay09.mods.cookingbook.food.ingredient.DefaultIngredient;
import net.blay09.mods.cookingbook.food.ingredient.OreDictIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ShapedOreCraftingFood implements IFoodRecipe {

    private final ShapedOreRecipe recipe;
    private final IFoodIngredient[] craftMatrix;

    public ShapedOreCraftingFood(ShapedOreRecipe recipe) {
        this.recipe = recipe;
        this.craftMatrix = new IFoodIngredient[recipe.getRecipeSize()];
        for(int i = 0; i < recipe.getInput().length; i++) {
            Object input = recipe.getInput()[i];
            if(input instanceof ItemStack) {
                craftMatrix[i] = new DefaultIngredient(((ItemStack) input));
            } else if(input instanceof List) {
                craftMatrix[i] = new OreDictIngredient((List<ItemStack>) input);
            }
        }
    }

    @Override
    public IFoodIngredient[] getCraftMatrix() {
        return craftMatrix;
    }

    @Override
    public ItemStack getOutputItem() {
        return recipe.getRecipeOutput();
    }

    @Override
    public boolean isSmeltingRecipe() {
        return false;
    }

    @Override
    public IRecipe getCraftingRecipe() {
        return recipe;
    }

}
