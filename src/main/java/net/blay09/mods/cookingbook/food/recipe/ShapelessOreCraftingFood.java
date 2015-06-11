package net.blay09.mods.cookingbook.food.recipe;

import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.*;
import net.blay09.mods.cookingbook.food.ingredient.DefaultIngredient;
import net.blay09.mods.cookingbook.food.ingredient.OreDictIngredient;
import net.blay09.mods.cookingbook.food.ingredient.OreDictToolIngredient;
import net.blay09.mods.cookingbook.food.ingredient.ToolIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class ShapelessOreCraftingFood implements IFoodRecipe {

    private final ShapelessOreRecipe recipe;
    private final IFoodIngredient[] craftMatrix;

    public ShapelessOreCraftingFood(ShapelessOreRecipe recipe) {
        this.recipe = recipe;
        this.craftMatrix = new IFoodIngredient[recipe.getRecipeSize()];
        for(int i = 0; i < recipe.getInput().size(); i++) {
            Object input = recipe.getInput().get(i);
            if(input instanceof ItemStack) {
                if(PamsHarvestcraft.isToolItem((ItemStack) input)) {
                    craftMatrix[i] = new ToolIngredient(((ItemStack) input));
                } else {
                    craftMatrix[i] = new DefaultIngredient(((ItemStack) input));
                }
            } else if(input instanceof ArrayList) {
                List<ItemStack> list = (List<ItemStack>) input;
                boolean toolFound = false;
                for(int j = 0; j < list.size(); j++) {
                    if(PamsHarvestcraft.isToolItem(list.get(j))) {
                        toolFound = true;
                    }
                }
                if(toolFound) {
                    craftMatrix[i] = new OreDictToolIngredient(list);
                } else {
                    craftMatrix[i] = new OreDictIngredient(list);
                }
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
