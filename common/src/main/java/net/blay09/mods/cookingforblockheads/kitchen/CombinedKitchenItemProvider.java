package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

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

        @Override
        public int reservedCount() {
            return token.reservedCount();
        }
    }

    private record CacheHintWrapper(int providerIndex, CacheHint cacheHint) implements CacheHint {
    }

    @Override
    public @Nullable IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (cacheHint instanceof CacheHintWrapper(int providerIndex, CacheHint hint)) {
            final var provider = providers.get(providerIndex);
            final var filteredIngredientTokens = getFilteredIngredientTokens(ingredientTokens, providerIndex);
            final var token = provider.findIngredient(ingredient, filteredIngredientTokens, hint, greedy);
            if (token != null) {
                return new IngredientTokenWrapper(providerIndex, token);
            }
            return null;
        }

        for (int i = 0; i < providers.size(); i++) {
            final var provider = providers.get(i);
            final var filteredIngredientTokens = getFilteredIngredientTokens(ingredientTokens, i);
            final var unwrappedCacheHint = cacheHint instanceof CacheHintWrapper wrapper ? wrapper.cacheHint : cacheHint;
            final var token = provider.findIngredient(ingredient, filteredIngredientTokens, unwrappedCacheHint, greedy);
            if (token != null) {
                return new IngredientTokenWrapper(i, token);
            }
        }
        return null;
    }

    @Override
    public @Nullable IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (cacheHint instanceof CacheHintWrapper(int providerIndex, CacheHint hint)) {
            final var provider = providers.get(providerIndex);
            final var token = provider.findIngredient(itemStack, ingredientTokens, hint, greedy);
            if (token != null) {
                return new IngredientTokenWrapper(providerIndex, token);
            }
            return null;
        }

        for (int i = 0; i < providers.size(); i++) {
            final var provider = providers.get(i);
            final var filteredIngredientTokens = getFilteredIngredientTokens(ingredientTokens, i);
            final var unwrappedCacheHint = cacheHint instanceof CacheHintWrapper wrapper ? wrapper.cacheHint : cacheHint;
            final var token = provider.findIngredient(itemStack, filteredIngredientTokens, unwrappedCacheHint, greedy);
            if (token != null) {
                return new IngredientTokenWrapper(i, token);
            }
        }
        return null;
    }

    @Override
    public CacheHint getCacheHint(IngredientToken ingredientToken) {
        if (ingredientToken instanceof IngredientTokenWrapper(int providerIndex, IngredientToken token)) {
            final var provider = providers.get(providerIndex);
            final var cacheHint = provider.getCacheHint(token);
            return new CacheHintWrapper(providerIndex, cacheHint);
        }

        return CacheHint.NONE;
    }

    private Collection<IngredientToken> getFilteredIngredientTokens(Collection<IngredientToken> ingredientTokens, int providerIndex) {
        return ingredientTokens.stream()
                .filter(ingredientToken -> (ingredientToken instanceof IngredientTokenWrapper wrapper && wrapper.providerIndex == providerIndex))
                .map(ingredientToken -> ((IngredientTokenWrapper) ingredientToken).token)
                .toList();
    }
}
