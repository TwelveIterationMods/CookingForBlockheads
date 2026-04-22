package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.*;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CraftingContext {

    public static final int COUNT_CUTOFF = 999;

    private final List<KitchenItemProvider> itemProviders;
    private final List<KitchenItemProcessor> itemProcessors;
    /**
     * @deprecated See notes in KitchenImpl
     */
    @Deprecated
    private final boolean allowCrafting;
    private final Map<Ingredient, Integer> cachedProviderIndexByIngredient = new HashMap<>();
    private final Map<CraftingOperation.IngredientTokenKey, CacheHint> cacheHintsByIngredient = new HashMap<>();
    private final List<Consumer<KitchenOperation>> listeners = new ArrayList<>();

    public CraftingContext(List<KitchenItemProvider> itemProviders, List<KitchenItemProcessor> itemProcessors, boolean allowCrafting) {
        this.itemProviders = itemProviders;
        this.itemProcessors = itemProcessors;
        this.allowCrafting = allowCrafting;
    }

    public CraftingOperation createOperation(RecipeHolder<?> recipe) {
        return new CraftingOperation(this, recipe );
    }

    public List<KitchenItemProvider> getItemProviders() {
        return itemProviders;
    }

    public List<KitchenItemProcessor> getItemProcessors() {
        return itemProcessors;
    }

    public int getCachedItemProviderIndexFor(Ingredient ingredient) {
        return cachedProviderIndexByIngredient.getOrDefault(ingredient, -1);
    }

    public CacheHint getCacheHintFor(CraftingOperation.IngredientTokenKey ingredientTokenKey) {
        return cacheHintsByIngredient.getOrDefault(ingredientTokenKey, CacheHint.NONE);
    }

    public void cache(CraftingOperation.IngredientTokenKey ingredientTokenKey, int itemProviderIndex, CacheHint cacheHint) {
        cacheHintsByIngredient.put(ingredientTokenKey, cacheHint);
        cachedProviderIndexByIngredient.put(ingredientTokenKey.ingredient(), itemProviderIndex);
    }

    public CraftingContext addListener(Consumer<KitchenOperation> listener) {
        listeners.add(listener);
        return this;
    }

    public KitchenOperation notify(KitchenOperation operation) {
        for (final var listener : listeners) {
            listener.accept(operation);
        }
        return operation;
    }

    public int countAvailable(ItemStack itemStack) {
        int amount = 0;
        for (final var itemProvider : itemProviders) {
            final var ingredientTokens = new ArrayList<IngredientToken>();
            CacheHint cacheHint = CacheHint.NONE;
            while (amount < COUNT_CUTOFF) {
                final var ingredientToken = itemProvider.findIngredient(itemStack, ingredientTokens, cacheHint, true);
                if (ingredientToken == null) {
                    break;
                }

                ingredientTokens.add(ingredientToken);
                cacheHint = itemProvider.getCacheHint(ingredientToken);
                amount += ingredientToken.reservedCount();
            }

            if (amount >= COUNT_CUTOFF) {
                return COUNT_CUTOFF;
            }
        }

        return amount;
    }

    public boolean canCraft(RecipeHolder<?> recipe) {
        final var recipeType = recipe.value().getType();
        if (recipeType == ModRecipes.kitchenRecipes.type()) {
            return true;
        }

        if (recipeType == RecipeType.CRAFTING) {
            return allowCrafting;
        }

        return itemProcessors.stream().anyMatch(it -> it.canProcess(recipeType));
    }

    /**
     * @deprecated Not pretty yet. We should probably create a general Craftability object
     *             which can store all information like missing ingredients, missing utensils, error etc.
     */
    @Deprecated
    public Optional<Component> reasonIfUncraftable(RecipeHolder<?> recipe) {
        if (!canCraft(recipe)) {
            final var recipeType = recipe.value().getType();
            if (recipeType == RecipeType.SMELTING || recipeType == RecipeType.SMOKING || recipeType == RecipeType.CAMPFIRE_COOKING) {
                return Optional.of(Component.translatable("tooltip.cookingforblockheads.missing_oven"));
            }
            final var processorRecipeType = CookingForBlockheadsRegistry.getProcessorRecipeType(recipe.value().getType()).orElse(null);
            if (processorRecipeType != null) {
                return Optional.of(processorRecipeType.missingProcessorComponent());
            }
        }

        return Optional.empty();
    }
}
