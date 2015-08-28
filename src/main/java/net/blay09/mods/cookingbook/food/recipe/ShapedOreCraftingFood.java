package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class ShapedOreCraftingFood extends FoodRecipe {

    public ShapedOreCraftingFood(ShapedOreRecipe recipe) {
        this.outputItem = recipe.getRecipeOutput();
        this.craftMatrix = new ArrayList<>();
        for(int i = 0; i < recipe.getInput().length; i++) {
            Object input = recipe.getInput()[i];
            if (input == null) {
                continue;
            }

            if(input instanceof ItemStack) {
                craftMatrix.add(new FoodIngredient((ItemStack) input, false));
            } else if(input instanceof List) {
                craftMatrix.add(new FoodIngredient(((List<ItemStack>) input).toArray(new ItemStack[((List<ItemStack>) input).size()]), false));
            }
        }
    }

}
