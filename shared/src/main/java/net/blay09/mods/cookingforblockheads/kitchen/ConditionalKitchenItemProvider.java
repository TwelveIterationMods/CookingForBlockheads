package net.blay09.mods.cookingforblockheads.kitchen;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public record ConditionalKitchenItemProvider(Supplier<Boolean> condition, KitchenItemProvider delegate,
                                             @Nullable KitchenItemProvider fallback) implements KitchenItemProvider {

    public ConditionalKitchenItemProvider(Supplier<Boolean> condition, KitchenItemProvider delegate) {
        this(condition, delegate, null);
    }

    @Override
    public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens) {
        if (!condition.get()) {
            return fallback != null ? fallback.findIngredient(ingredient, ingredientTokens) : null;
        }

        return delegate.findIngredient(ingredient, ingredientTokens);
    }

    @Override
    public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens) {
        if (!condition.get()) {
            return fallback != null ? fallback.findIngredient(itemStack, ingredientTokens) : null;
        }

        return delegate.findIngredient(itemStack, ingredientTokens);
    }
}
