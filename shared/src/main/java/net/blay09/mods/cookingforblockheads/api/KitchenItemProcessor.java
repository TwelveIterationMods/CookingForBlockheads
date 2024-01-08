package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public interface KitchenItemProcessor {
    boolean canProcess(RecipeType<?> recipeType);

    KitchenOperation processRecipe(RecipeType<?> recipeType, List<IngredientToken> ingredients);
}
