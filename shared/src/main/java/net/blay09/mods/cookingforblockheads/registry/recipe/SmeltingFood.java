package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;

public class SmeltingFood extends FoodRecipe {

    public SmeltingFood(IRecipe<?> recipe) {
        this.outputItem = recipe.getRecipeOutput();

        fillCraftMatrix(recipe);
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SMELTING;
    }

}
