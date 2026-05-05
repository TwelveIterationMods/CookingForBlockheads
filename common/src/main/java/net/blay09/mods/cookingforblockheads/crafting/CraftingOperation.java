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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    private final List<List<ItemStack>> ingredientOptions = new ArrayList<>();

    private List<ItemStack> lockedInputs;
    private int missingIngredientsMask;

    public CraftingOperation(final CraftingContext context, RecipeHolder<Recipe<?>> recipe) {
        this.context = context;
        this.recipe = recipe.value();
    }

    public CraftingOperation withLockedInputs(@Nullable List<ItemStack> lockedInputs) {
        this.lockedInputs = lockedInputs;
        return this;
    }

    public CraftingOperation prepare() {
        tokensByIngredient.clear();
        ingredientTokens.clear();
        missingIngredients.clear();
        ingredientOptions.clear();
        missingIngredientsMask = 0;

        final var ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            final var ingredient = ingredients.get(i);
            if (ingredient.isEmpty()) {
                ingredientOptions.add(List.of());
                ingredientTokens.add(IngredientToken.EMPTY);
                continue;
            }

            // TODO Should do this only if we found one, reusing the found ingredientToken
            ingredientOptions.add(getIngredientOptions(ingredient));
            final var lockedInput = lockedInputs != null && i < lockedInputs.size() ? lockedInputs.get(i) : ItemStack.EMPTY;
            final var ingredientToken = accountForIngredient(ingredient, lockedInput);
            if (ingredientToken != null) {
                if (ingredient.getItems().length > 1) {
                    if (lockedInputs == null || lockedInputs.size() != ingredients.size()) {
                        lockedInputs = NonNullList.withSize(ingredients.size(), ItemStack.EMPTY);
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

    private List<ItemStack> getIngredientOptions(Ingredient ingredient) {
        final var candidateItems = ingredient.getItems();
        if (candidateItems.length == 1) {
            return List.of(candidateItems[0]);
        }

        final var result = new ArrayList<ItemStack>();
        for (final var itemStack : candidateItems) {
            if (itemStack.isEmpty()) {
                continue;
            }

            final var availableToken = findAvailableIngredientToken(ingredient, itemStack);
            if (availableToken != null) {
                result.add(itemStack.copyWithCount(1));
            }
        }
        return result;
    }

    @Nullable
    private IngredientToken findAvailableIngredientToken(Ingredient ingredient, ItemStack lockedInput) {
        final var itemProviders = context.getItemProviders();
        final var cachedProviderIndex = context.getCachedItemProviderIndexFor(ingredient);
        if (cachedProviderIndex != -1) {
            final var itemProvider = itemProviders.get(cachedProviderIndex);
            final var ingredientToken = findAvailableIngredientToken(cachedProviderIndex, itemProvider, ingredient, lockedInput, true);
            if (ingredientToken != null) {
                return ingredientToken;
            }
        }

        for (int j = 0; j < itemProviders.size(); j++) {
            final var itemProvider = itemProviders.get(j);
            IngredientToken ingredientToken = findAvailableIngredientToken(j, itemProvider, ingredient, lockedInput, false);
            if (ingredientToken != null) {
                return ingredientToken;
            }
        }

        return null;
    }

    @Nullable
    private IngredientToken findAvailableIngredientToken(int itemProviderIndex, KitchenItemProvider itemProvider, Ingredient ingredient, ItemStack lockedInput, boolean useCache) {
        final var ingredientTokenKey = new IngredientTokenKey(itemProviderIndex, ingredient.getStackingIds());
        final var scopedIngredientTokens = tokensByIngredient.get(ingredientTokenKey);
        final var cacheHint = useCache ? context.getCacheHintFor(ingredientTokenKey) : CacheHint.NONE;
        return findIngredient(itemProvider, ingredient, lockedInput, scopedIngredientTokens, cacheHint);
    }

    public boolean hasIngredients() {
        return missingIngredients.isEmpty();
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

    public List<ItemStack> getLockedInputs() {
        return lockedInputs;
    }

    public List<Ingredient> getMissingIngredients() {
        return missingIngredients;
    }

    public int getMissingIngredientsMask() {
        return missingIngredientsMask;
    }

    public List<List<ItemStack>> getIngredientOptions() {
        return ingredientOptions;
    }
}
