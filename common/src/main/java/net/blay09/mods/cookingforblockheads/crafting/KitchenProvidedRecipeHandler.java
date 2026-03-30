package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.blay09.mods.cookingforblockheads.recipe.KitchenProvidedRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;

import java.util.List;
import java.util.Optional;

public class KitchenProvidedRecipeHandler implements KitchenRecipeHandler<SingleRecipeInput, KitchenProvidedRecipe> {
    @Override
    public int mapToMatrixSlot(KitchenProvidedRecipe recipe, int ingredientIndex) {
        return 0;
    }

    @Override
    public List<Optional<Ingredient>> getIngredients(KitchenProvidedRecipe recipe) {
        return List.of();
    }

    @Override
    public ItemStackTemplate predictResultItem(KitchenProvidedRecipe recipe) {
        return recipe.resultItem();
    }

    @Override
    public ItemStack assemble(CraftingContext context, KitchenProvidedRecipe recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess) {
        return recipe.resultItem().create();
    }
}
