package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ShapedOreCraftingFood extends FoodRecipe {

    @SuppressWarnings("unchecked")
    public ShapedOreCraftingFood(int id, ShapedOreRecipe recipe) {
        super(id);
        this.outputItem = recipe.getRecipeOutput();
        this.craftMatrix = Lists.newArrayList();
        for(int i = 0; i < recipe.getInput().length; i++) {
            Object input = recipe.getInput()[i];
            if (input == null) {
                craftMatrix.add(null);
            } else if(input instanceof ItemStack) {
                craftMatrix.add(new FoodIngredient((ItemStack) input, false));
            } else if(input instanceof List) {
                craftMatrix.add(new FoodIngredient(((List<ItemStack>) input).toArray(new ItemStack[((List<ItemStack>) input).size()]), false));
            }
        }
    }

}
