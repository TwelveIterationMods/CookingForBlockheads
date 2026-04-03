package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeProvider;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.mixin.RecipeManagerAccessor;
import net.blay09.mods.cookingforblockheads.recipe.KitchenProvidedRecipe;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class KitchenImpl implements Kitchen {

    private final Level level;
    /**
     * @deprecated Not fully happy with this. I think we should try to merge RecipeHandlers and ItemProcessors,
     *             and detangle them to have them provide the assembling as well as a more customizable preview
     *             configuration. Then a kitchen doesn't need this state separately, it would just know from either
     *             having a crafting processor or not.
     */
    @Deprecated
    private final boolean allowCrafting;
    private final Set<BlockPos> checkedPos = new HashSet<>();
    private final List<KitchenItemProvider> itemProviderList = new ArrayList<>();
    private final List<KitchenRecipeProvider> recipeProviderList = new ArrayList<>();
    private final List<KitchenItemProcessor> itemProcessorList = new ArrayList<>();

    public KitchenImpl(Level level, boolean allowCrafting) {
        this.level = level;
        this.allowCrafting = allowCrafting;
    }

    public KitchenImpl(Level level, BlockPos pos) {
        this.level = level;
        this.allowCrafting = true;
        findNeighbourCraftingBlocks(level, pos, true);
    }

    private void findNeighbourCraftingBlocks(Level level, BlockPos pos, boolean extendedUpSearch) {
        for (Direction direction : Direction.values()) {
            int upSearch = (extendedUpSearch && direction == Direction.UP) ? 2 : 1;
            for (int n = 1; n <= upSearch; n++) {
                BlockPos position = pos.relative(direction, n);
                if (!checkedPos.contains(position)) {
                    checkedPos.add(position);

                    BlockState state = level.getBlockState(position);
                    BlockEntity blockEntity = level.getBlockEntity(position);
                    if (blockEntity != null) {
                        var itemProvider = Balm.capabilities().getCapability(blockEntity, ModCapabilities.KITCHEN_ITEM_PROVIDER);
                        if (itemProvider != null) {
                            itemProviderList.add(itemProvider);
                        }

                        final var recipeProvider = Balm.capabilities().getCapability(blockEntity, ModCapabilities.KITCHEN_RECIPE_PROVIDER);
                        if (recipeProvider != null) {
                            recipeProviderList.add(recipeProvider);
                        }

                        final var itemProcessor = Balm.capabilities().getCapability(blockEntity, ModCapabilities.KITCHEN_ITEM_PROCESSOR);
                        if (itemProcessor != null) {
                            itemProcessorList.add(itemProcessor);
                        }

                        if (itemProvider != null || recipeProvider != null || itemProcessor != null || state.is(ModBlockTags.KITCHEN_CONNECTORS)) {
                            findNeighbourCraftingBlocks(level, position, true);
                        }
                    } else if (state.is(ModBlockTags.KITCHEN_CONNECTORS)) {
                        findNeighbourCraftingBlocks(level, position, false);
                    }
                }
            }
        }
    }

    @Override
    public CraftingContext createCraftingContext(@Nullable Player player) {
        final var itemProviders = new ArrayList<>(itemProviderList);
        if (player != null) {
            itemProviders.addFirst(new ContainerKitchenItemProvider(player.getInventory()));
        }
        return new CraftingContext(itemProviders, itemProcessorList, allowCrafting);
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
        for (final var craftableProvider : recipeProviderList) {
            result.addAll(craftableProvider.getKitchenRecipeSources());
        }
        return result;
    }
}
