package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public record ConditionalKitchenItemProvider<T extends KitchenItemProvider>(Supplier<Boolean> condition, T delegate,
                                             @Nullable T fallback) implements KitchenItemProvider {

    public ConditionalKitchenItemProvider(Supplier<Boolean> condition, T delegate) {
        this(condition, delegate, null);
    }

    @Override
    public @Nullable IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (!condition.get()) {
            return fallback != null ? fallback.findIngredient(ingredient, ingredientTokens, cacheHint, greedy) : null;
        }

        return delegate.findIngredient(ingredient, ingredientTokens, cacheHint, greedy);
    }

    @Override
    public @Nullable IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
        if (!condition.get()) {
            return fallback != null ? fallback.findIngredient(itemStack, ingredientTokens, cacheHint, greedy) : null;
        }

        return delegate.findIngredient(itemStack, ingredientTokens, cacheHint, greedy);
    }

    @Override
    public CacheHint getCacheHint(IngredientToken ingredientToken) {
        if (!condition.get()) {
            return fallback != null ? fallback.getCacheHint(ingredientToken) : CacheHint.NONE;
        }

        return delegate.getCacheHint(ingredientToken);
    }
}
