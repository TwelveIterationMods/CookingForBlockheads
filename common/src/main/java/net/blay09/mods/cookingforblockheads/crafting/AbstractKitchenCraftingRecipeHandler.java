package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.List;

public abstract class AbstractKitchenCraftingRecipeHandler<T extends CraftingRecipe> implements KitchenRecipeHandler<T> {
    @Override
    public ItemStack assemble(CraftingContext context, T recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess) {
        final var craftingContainer = new TransientHeadlessCraftingContainer(3, 3);
        for (int i = 0; i < ingredientTokens.size(); i++) {
            final var ingredientToken = ingredientTokens.get(i);
            final var matrixSlot = mapToMatrixSlot(recipe, i);
            craftingContainer.setItem(matrixSlot, ingredientToken.consume());
        }
        final var craftInput = craftingContainer.asCraftInput();
        final var remainingItems = recipe.getRemainingItems(craftInput);
        for (int i = 0; i < remainingItems.size(); i++) {
            final var ingredientToken = i < ingredientTokens.size() ? ingredientTokens.get(i) : IngredientToken.EMPTY;
            final var restItem = ingredientToken.restore(remainingItems.get(i));
            if (!restItem.isEmpty()) {
                context.restore(restItem);
            }
        }
        return recipe.assemble(craftInput, registryAccess);
    }
}
