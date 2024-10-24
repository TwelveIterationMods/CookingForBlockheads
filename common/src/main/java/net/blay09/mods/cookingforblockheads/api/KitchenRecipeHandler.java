package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Optional;

public interface KitchenRecipeHandler<T extends Recipe<?>> {
    int mapToMatrixSlot(T recipe, int ingredientIndex);

    List<Optional<Ingredient>> getIngredients(T recipe);

    ItemStack predictResultItem(T recipe);

    ItemStack assemble(CraftingContext context, T recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess);
}
