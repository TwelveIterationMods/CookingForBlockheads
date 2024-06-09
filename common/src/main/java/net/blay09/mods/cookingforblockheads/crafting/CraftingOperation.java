package net.blay09.mods.cookingforblockheads.crafting;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CraftingOperation {

    public record IngredientTokenKey(int providerIndex, IntList stackingIds) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IngredientTokenKey that = (IngredientTokenKey) o;
            return providerIndex == that.providerIndex && Objects.equals(stackingIds, that.stackingIds);
        }

        @Override
        public int hashCode() {
            return Objects.hash(providerIndex, stackingIds);
        }
    }

    private final CraftingContext context;
    private final Recipe<?> recipe;

    private final Multimap<IngredientTokenKey, IngredientToken> tokensByIngredient = ArrayListMultimap.create();
    private final List<IngredientToken> ingredientTokens = new ArrayList<>();
    private final List<Ingredient> missingIngredients = new ArrayList<>();

    private NonNullList<ItemStack> lockedInputs;
    private int missingIngredientsMask;

    public CraftingOperation(final CraftingContext context, RecipeHolder<Recipe<?>> recipe) {
        this.context = context;
        this.recipe = recipe.value();
    }

    public CraftingOperation withLockedInputs(@Nullable NonNullList<ItemStack> lockedInputs) {
        this.lockedInputs = lockedInputs;
        return this;
    }

    public CraftingOperation prepare() {
        tokensByIngredient.clear();
        ingredientTokens.clear();
        missingIngredients.clear();
        missingIngredientsMask = 0;

        final var ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            final var ingredient = ingredients.get(i);
            if (ingredient.isEmpty()) {
                ingredientTokens.add(IngredientToken.EMPTY);
                continue;
            }

            final var lockedInput = lockedInputs != null ? lockedInputs.get(i) : ItemStack.EMPTY;
            final var ingredientToken = accountForIngredient(ingredient, lockedInput);
            if (ingredientToken != null) {
                if (ingredient.getItems().length > 1) {
                    if (lockedInputs == null) {
                        lockedInputs = NonNullList.withSize(recipe.getIngredients().size(), ItemStack.EMPTY);
                    }
                    lockedInputs.set(i, ingredientToken.peek());
                }
            } else {
                missingIngredients.add(ingredient);
                missingIngredientsMask |= 1 << i;
            }
        }

        return this;
    }

    @Nullable
    private IngredientToken accountForIngredient(Ingredient ingredient, ItemStack lockedInput) {
        final var itemProviders = context.getItemProviders();
        final var cachedProviderIndex = context.getCachedItemProviderIndexFor(ingredient);
        if (cachedProviderIndex != -1) {
            final var itemProvider = itemProviders.get(cachedProviderIndex);
            final var ingredientToken = accountForIngredient(cachedProviderIndex, itemProvider, ingredient, lockedInput, true);
            if (ingredientToken != null) {
                return ingredientToken;
            }
        }

        for (int j = 0; j < itemProviders.size(); j++) {
            final var itemProvider = itemProviders.get(j);
            IngredientToken ingredientToken = accountForIngredient(j, itemProvider, ingredient, lockedInput, false);
            if (ingredientToken != null) {
                return ingredientToken;
            }
        }

        return null;
    }

    @Nullable
    private IngredientToken accountForIngredient(int itemProviderIndex, KitchenItemProvider itemProvider, Ingredient ingredient, ItemStack lockedInput, boolean useCache) {
        final var ingredientTokenKey = new IngredientTokenKey(itemProviderIndex, ingredient.getStackingIds());
        final var scopedIngredientTokens = tokensByIngredient.get(ingredientTokenKey);
        final var cacheHint = useCache ? context.getCacheHintFor(ingredientTokenKey) : CacheHint.NONE;
        final var ingredientToken = findIngredient(itemProvider, ingredient, lockedInput, scopedIngredientTokens, cacheHint);
        if (ingredientToken != null) {
            tokensByIngredient.put(ingredientTokenKey, ingredientToken);
            context.cache(ingredientTokenKey, itemProviderIndex, itemProvider.getCacheHint(ingredientToken));
            ingredientTokens.add(ingredientToken);
            return ingredientToken;
        }
        return null;
    }

    @Nullable
    private IngredientToken findIngredient(KitchenItemProvider itemProvider, Ingredient ingredient, ItemStack lockedInput, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
        IngredientToken ingredientToken;
        if (lockedInput.isEmpty()) {
            ingredientToken = itemProvider.findIngredient(ingredient, ingredientTokens, cacheHint);
        } else {
            ingredientToken = itemProvider.findIngredient(lockedInput, ingredientTokens, cacheHint);
        }
        return ingredientToken;
    }

    public boolean canCraft() {
        return missingIngredients.isEmpty();
    }

    public ItemStack craft(AbstractContainerMenu menu, RegistryAccess registryAccess) {
        return craft(menu, registryAccess, recipe);
    }

    private <C extends RecipeInput, T extends Recipe<C>> ItemStack craft(AbstractContainerMenu menu, RegistryAccess registryAccess, T recipe) {
        final var recipeTypeHandler = CookingForBlockheadsRegistry.getRecipeWorkshopHandler(recipe);
        if (recipeTypeHandler == null) {
            return ItemStack.EMPTY;
        }

        return recipeTypeHandler.assemble(context, recipe, ingredientTokens, registryAccess);
    }

    public NonNullList<ItemStack> getLockedInputs() {
        return lockedInputs;
    }

    public List<Ingredient> getMissingIngredients() {
        return missingIngredients;
    }

    public int getMissingIngredientsMask() {
        return missingIngredientsMask;
    }
}
