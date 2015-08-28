package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class ShapelessOreCraftingFood extends FoodRecipe {

    public ShapelessOreCraftingFood(ShapelessOreRecipe recipe) {
        this.outputItem = recipe.getRecipeOutput();
        this.craftMatrix = new ArrayList<>();
        for(int i = 0; i < recipe.getInput().size(); i++) {
            Object input = recipe.getInput().get(i);

            if (input == null) {
                continue;
            }

            if(input instanceof ItemStack) {
                boolean isToolItem = PamsHarvestcraft.isToolItem((ItemStack) input);
                craftMatrix.add(new FoodIngredient(((ItemStack) input), isToolItem));
            } else if(input instanceof ArrayList) {
                List<ItemStack> list = (List<ItemStack>) input;
                boolean toolFound = false;
                for(int j = 0; j < list.size(); j++) {
                    if(PamsHarvestcraft.isToolItem(list.get(j))) {
                        toolFound = true;
                    }
                }
                craftMatrix.add(new FoodIngredient(list.toArray(new ItemStack[list.size()]), toolFound));
            }
        }
    }

}
