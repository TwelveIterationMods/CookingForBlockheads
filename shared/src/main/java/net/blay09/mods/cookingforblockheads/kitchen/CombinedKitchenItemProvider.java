package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;
import java.util.List;

public record CombinedKitchenItemProvider(List<KitchenItemProvider> providers) implements KitchenItemProvider {

    private record IngredientTokenWrapper(int providerIndex, IngredientToken token) implements IngredientToken {
        @Override
        public ItemStack peek() {
            return token.peek();
        }

        @Override
        public ItemStack consume() {
            return token.consume();
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            return token.restore(itemStack);
        }
    }

    @Override
    public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens) {
        for (int i = 0; i < providers.size(); i++) {
            final var provider = providers.get(i);
            final var filteredIngredientTokens = getFilteredIngredientTokens(ingredientTokens, i);
            final var token = provider.findIngredient(ingredient, filteredIngredientTokens);
            if (token != null) {
                return new IngredientTokenWrapper(i, token);
            }
        }
        return null;
    }

    @Override
    public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens) {
        for (int i = 0; i < providers.size(); i++) {
            final var provider = providers.get(i);
            final var filteredIngredientTokens = getFilteredIngredientTokens(ingredientTokens, i);
            final var token = provider.findIngredient(itemStack, filteredIngredientTokens);
            if (token != null) {
                return new IngredientTokenWrapper(i, token);
            }
        }
        return null;
    }

    private Collection<IngredientToken> getFilteredIngredientTokens(Collection<IngredientToken> ingredientTokens, int providerIndex) {
        return ingredientTokens.stream()
                .filter(ingredientToken -> !(ingredientToken instanceof IngredientTokenWrapper wrapper && wrapper.providerIndex == providerIndex))
                .toList();
    }
}
