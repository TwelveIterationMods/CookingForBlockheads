package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public interface KitchenProcessingProvider {
    boolean isSupportedRecipeType(RecipeType<?> recipeType);

    KitchenOperation processRecipe(RecipeType<?> recipeType, List<IngredientToken> ingredients);
}
