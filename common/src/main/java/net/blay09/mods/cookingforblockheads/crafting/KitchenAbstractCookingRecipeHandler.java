package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.blay09.mods.cookingforblockheads.mixin.SingleItemRecipeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;

import java.util.List;
import java.util.Optional;

public class KitchenAbstractCookingRecipeHandler implements KitchenRecipeHandler<SingleRecipeInput, AbstractCookingRecipe> {
    @Override
    public int mapToMatrixSlot(AbstractCookingRecipe recipe, int ingredientIndex) {
        return 4;
    }

    @Override
    public List<Optional<Ingredient>> getIngredients(AbstractCookingRecipe recipe) {
        return List.of(Optional.of(recipe.input()));
    }

    @Override
    public ItemStack assemble(CraftingContext context, AbstractCookingRecipe recipe, List<IngredientToken> ingredientTokens, RegistryAccess registryAccess) {
        for (final var itemProcessor : context.getItemProcessors()) {
            if (itemProcessor.canProcess(recipe.getType())) {
                final var operation = context.notify(itemProcessor.processRecipe(recipe, ingredientTokens));
                return operation.getImmediateResult();
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStackTemplate predictResultItem(AbstractCookingRecipe recipe) {
        return ((SingleItemRecipeAccessor) recipe).getResult();
    }
}
