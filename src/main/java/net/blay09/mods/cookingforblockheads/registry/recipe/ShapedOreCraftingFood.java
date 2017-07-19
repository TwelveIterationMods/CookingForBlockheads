package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ShapedOreCraftingFood extends FoodRecipe {

    @SuppressWarnings("unchecked")
    public ShapedOreCraftingFood(ShapedOreRecipe recipe) {
        this.outputItem = recipe.getRecipeOutput();
        this.recipeWidth = recipe.getWidth();
        this.craftMatrix = Lists.newArrayList();
        for(int i = 0; i < recipe.getInput().length; i++) {
            Object input = recipe.getInput()[i];
            if (input == null) {
                craftMatrix.add(null);
            } else if(input instanceof ItemStack) {
                craftMatrix.add(((ItemStack) input).isEmpty() ? null : new FoodIngredient((ItemStack) input, false));
            } else if(input instanceof NonNullList) {
                craftMatrix.add(new FoodIngredient((NonNullList<ItemStack>) input, false));
            }
        }
    }

}
