package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;

public class KitchenCookingPotRecipeHandler implements KitchenRecipeHandler<CookingPotRecipe> {
    public static int mapIngredientToSlot(int ingredientIndex) {
        if (ingredientIndex >= 0 && ingredientIndex < 6) {
            return ingredientIndex;
        }

        return 4;
    }

    @Override
    public int mapToMatrixSlot(CookingPotRecipe recipe, int ingredientIndex) {
        return mapIngredientToSlot(ingredientIndex);
    }

    @Override
    public ItemStack assemble(CraftingContext context, CookingPotRecipe recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess) {
        for (final var itemProcessor : context.getItemProcessors()) {
            if (itemProcessor.canProcess(recipe.getType())) {
                context.notify(itemProcessor.processRecipe(recipe, ingredientTokens));
                return ItemStack.EMPTY;
            }
        }

        return ItemStack.EMPTY;
    }
}
