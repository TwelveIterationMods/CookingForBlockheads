package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.List;

public class KitchenSmeltingRecipeHandler implements KitchenRecipeHandler<SmeltingRecipe> {
    @Override
    public int mapToMatrixSlot(SmeltingRecipe recipe, int ingredientIndex) {
        return 4;
    }

    @Override
    public ItemStack assemble(CraftingContext context, SmeltingRecipe recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess) {
        for (final var itemProcessor : context.getItemProcessors()) {
            if (itemProcessor.canProcess(recipe.getType())) {
                itemProcessor.processRecipe(recipe, ingredientTokens);
                return ItemStack.EMPTY;
            }
        }

        return ItemStack.EMPTY;
    }
}
