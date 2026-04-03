package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.mixin.RecipeManagerAccessor;
import net.blay09.mods.cookingforblockheads.network.message.KitchenFeedbackMessage;
import net.blay09.mods.cookingforblockheads.recipe.KitchenProvidedRecipe;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class KitchenImpl implements Kitchen {

    private final Level level;
    /**
     * @deprecated Not fully happy with this. I think we should try to merge RecipeHandlers and ItemProcessors,
     * and detangle them to have them provide the assembling as well as a more customizable preview
     * configuration. Then a kitchen doesn't need this state separately, it would just know from either
     * having a crafting processor or not.
     */
    @Deprecated
    private final boolean allowCrafting;
    private final List<KitchenItemProvider> itemProviders;
    private final List<KitchenRecipeProvider> recipeProviders;
    private final List<KitchenItemProcessor> itemProcessors;

    public KitchenImpl(Level level, boolean allowCrafting) {
        this(level, allowCrafting, List.of(), List.of(), List.of());
    }

    public KitchenImpl(Level level,
                       boolean allowCrafting,
                       List<KitchenItemProvider> itemProviders,
                       List<KitchenRecipeProvider> recipeProviders,
                       List<KitchenItemProcessor> itemProcessors) {
        this.level = level;
        this.allowCrafting = allowCrafting;
        this.itemProviders = itemProviders;
        this.recipeProviders = recipeProviders;
        this.itemProcessors = itemProcessors;
    }

    @Override
    public CraftingContext createCraftingContext(@Nullable Player player) {
        final var itemProviders = new ArrayList<>(this.itemProviders);
        if (player != null) {
            itemProviders.addFirst(new ContainerKitchenItemProvider(player.getInventory()));
        }
        return new CraftingContext(itemProviders, itemProcessors, allowCrafting).addListener(operation -> {
            if (player != null) {
                final var feedback = operation.getFeedback();
                feedback.ifPresent(component -> Balm.networking().sendTo(player, new KitchenFeedbackMessage(component)));
            }
        });
    }

    @Override
    public Collection<RecipeHolder<?>> getRecipesFor(ItemStack resultItem) {
        final var recipes = new ArrayList<>(CookingForBlockheadsRegistry.getRecipesFor(resultItem));
        recipes.addAll(CookingForBlockheadsRegistry.getRecipesInGroup(resultItem));
        getProvidedRecipes(level).stream()
                .filter(it -> ItemStack.isSameItemSameComponents(it.value().resultItem().create(), resultItem))
                .forEach(recipes::add);
        return recipes;
    }

    @Override
    public Collection<RecipeHolder<?>> getAvailableRecipes() {
        final var recipes = new LinkedHashMap<Identifier, RecipeHolder<?>>();
        final var recipesByItemId = CookingForBlockheadsRegistry.getRecipesByItemId();
        for (final var itemId : recipesByItemId.keySet()) {
            for (final var recipeHolder : recipesByItemId.get(itemId)) {
                recipes.put(recipeHolder.id().identifier(), recipeHolder);
            }
        }

        getProvidedRecipes(level).forEach(recipeHolder -> recipes.put(recipeHolder.id().identifier(), recipeHolder));
        return recipes.values();
    }

    private Collection<RecipeHolder<KitchenProvidedRecipe>> getProvidedRecipes(Level level) {
        final var recipes = new LinkedHashMap<Identifier, RecipeHolder<KitchenProvidedRecipe>>();
        final var providedRecipeSources = getAvailableRecipeSources();
        if (level instanceof ServerLevel serverLevel) {
            final var recipeMap = ((RecipeManagerAccessor) serverLevel.getServer().getRecipeManager()).getRecipes();
            recipeMap.byType(ModRecipes.kitchenRecipes.type()).stream()
                    .filter(it -> providedRecipeSources.contains(it.value().source()))
                    .forEach(recipeHolder -> recipes.put(recipeHolder.id().identifier(), recipeHolder));
        }
        return recipes.values();
    }

    private Set<Identifier> getAvailableRecipeSources() {
        final var result = new HashSet<Identifier>();
        for (final var craftableProvider : recipeProviders) {
            result.addAll(craftableProvider.getKitchenRecipeSources());
        }
        return result;
    }
}
