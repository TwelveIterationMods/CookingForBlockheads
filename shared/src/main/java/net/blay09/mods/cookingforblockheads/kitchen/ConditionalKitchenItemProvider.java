package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public record ConditionalKitchenItemProvider<T extends KitchenItemProvider>(Supplier<Boolean> condition, T delegate,
                                             @Nullable T fallback) implements KitchenItemProvider {

    public ConditionalKitchenItemProvider(Supplier<Boolean> condition, T delegate) {
        this(condition, delegate, null);
    }

    @Override
    public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        if (!condition.get()) {
            return fallback != null ? fallback.findIngredient(ingredient, ingredientTokens, cacheHint) : null;
        }

        return delegate.findIngredient(ingredient, ingredientTokens, cacheHint);
    }

    @Override
    public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        if (!condition.get()) {
            return fallback != null ? fallback.findIngredient(itemStack, ingredientTokens, cacheHint) : null;
        }

        return delegate.findIngredient(itemStack, ingredientTokens, cacheHint);
    }

    @Override
    public CacheHint getCacheHint(IngredientToken ingredientToken) {
        if (!condition.get()) {
            return fallback != null ? fallback.getCacheHint(ingredientToken) : CacheHint.NONE;
        }

        return delegate.getCacheHint(ingredientToken);
    }
}
