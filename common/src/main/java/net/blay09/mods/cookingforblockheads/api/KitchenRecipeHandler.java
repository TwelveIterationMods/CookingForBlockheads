package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface KitchenRecipeHandler<C extends RecipeInput, T extends Recipe<C>> {
    @SuppressWarnings("unchecked")
    default int mapToMatrixSlot(RecipeHolder<?> recipe, int ingredientIndex) {
        return mapToMatrixSlot((T) recipe.value(), ingredientIndex);
    }

    int mapToMatrixSlot(T recipe, int ingredientIndex);

    @SuppressWarnings("unchecked")
    default List<Optional<Ingredient>> getIngredients(RecipeHolder<?> recipe) {
        return getIngredients((T) recipe.value());
    }

    List<Optional<Ingredient>> getIngredients(T recipe);

    @SuppressWarnings("unchecked")
    default @Nullable ItemStackTemplate predictResultItem(RecipeHolder<?> recipe) {
        return predictResultItem((T) recipe.value());
    }

    @Nullable ItemStackTemplate predictResultItem(T recipe);

    @SuppressWarnings("unchecked")
    default ItemStack assemble(CraftingContext context, RecipeHolder<?> recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess) {
        return assemble(context, (T) recipe.value(), ingredientTokens, registryAccess);
    }

    ItemStack assemble(CraftingContext context, T recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess);
}
