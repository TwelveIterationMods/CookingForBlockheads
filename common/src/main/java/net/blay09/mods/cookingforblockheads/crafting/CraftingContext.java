package net.blay09.mods.cookingforblockheads.crafting;

import it.unimi.dsi.fastutil.ints.IntList;
import net.blay09.mods.cookingforblockheads.api.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CraftingContext {

    private final List<KitchenItemProvider> itemProviders;
    private final List<KitchenItemProcessor> itemProcessors;
    private final Map<IntList, Integer> cachedProviderIndexByIngredient = new HashMap<>();
    private final Map<CraftingOperation.IngredientTokenKey, CacheHint> cacheHintsByIngredient = new HashMap<>();
    private final List<Consumer<KitchenOperation>> listeners = new ArrayList<>();

    public CraftingContext(final Kitchen kitchen, final @Nullable Player player) {
        itemProviders = kitchen.getItemProviders(player);
        itemProcessors = kitchen.getItemProcessors();
    }

    public CraftingOperation createOperation(RecipeHolder<Recipe<?>> recipe) {
        return new CraftingOperation(this, recipe);
    }

    public List<KitchenItemProvider> getItemProviders() {
        return itemProviders;
    }

    public List<KitchenItemProcessor> getItemProcessors() {
        return itemProcessors;
    }

    public int getCachedItemProviderIndexFor(Ingredient ingredient) {
        return cachedProviderIndexByIngredient.getOrDefault(ingredient.getStackingIds(), -1);
    }

    public CacheHint getCacheHintFor(CraftingOperation.IngredientTokenKey ingredientTokenKey) {
        return cacheHintsByIngredient.getOrDefault(ingredientTokenKey, CacheHint.NONE);
    }

    public void cache(CraftingOperation.IngredientTokenKey ingredientTokenKey, int itemProviderIndex, CacheHint cacheHint) {
        cacheHintsByIngredient.put(ingredientTokenKey, cacheHint);
        cachedProviderIndexByIngredient.put(ingredientTokenKey.stackingIds(), itemProviderIndex);
    }

    public void addListener(Consumer<KitchenOperation> listener) {
        listeners.add(listener);
    }

    public void notify(KitchenOperation operation) {
        for (final var listener : listeners) {
            listener.accept(operation);
        }
    }
}
