package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreCraftingFood extends FoodRecipe {

    @SuppressWarnings("unchecked")
    public ShapelessOreCraftingFood(ShapelessOreRecipe recipe) {
        this.outputItem = recipe.getRecipeOutput();
        this.craftMatrix = Lists.newArrayList();
        for(int i = 0; i < recipe.getInput().size(); i++) {
            Object input = recipe.getInput().get(i);
            if(input instanceof ItemStack) {
                boolean isToolItem = CookingRegistry.isToolItem((ItemStack) input);
                craftMatrix.add(new FoodIngredient(((ItemStack) input), isToolItem));
            } else if(input instanceof NonNullList) {
                NonNullList<ItemStack> stackList = (NonNullList<ItemStack>) input;
                boolean toolFound = false;
                for (ItemStack itemStack : stackList) {
                    if (CookingRegistry.isToolItem(itemStack)) {
                        toolFound = true;
                    }
                }
                craftMatrix.add(new FoodIngredient(stackList, toolFound));
            }
        }
    }

}
