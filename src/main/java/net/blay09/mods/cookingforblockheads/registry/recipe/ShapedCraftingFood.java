package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedCraftingFood extends FoodRecipe {

    public ShapedCraftingFood(int id, ShapedRecipes recipe) {
        super(id);
        this.outputItem = recipe.getRecipeOutput();
        craftMatrix = Lists.newArrayList();

        for(int i = 0; i < recipe.recipeItems.length; i++) {
            if(recipe.recipeItems[i] != null) {
                boolean isToolItem = CookingRegistry.isToolItem(recipe.recipeItems[i]);
                craftMatrix.add(new FoodIngredient(recipe.recipeItems[i].copy(), isToolItem));
            } else {
                craftMatrix.add(null);
            }
        }
    }

}
