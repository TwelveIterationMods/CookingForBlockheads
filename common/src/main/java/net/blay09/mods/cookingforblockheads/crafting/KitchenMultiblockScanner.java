package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeProvider;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KitchenMultiblockScanner {
    static final int MAX_SCANNED_POSITIONS = 4096;

    private final Set<BlockPos> closedList = new HashSet<>();
    private final List<KitchenItemProvider> itemProviders = new ArrayList<>();
    private final List<KitchenRecipeProvider> recipeProviders = new ArrayList<>();
    private final List<KitchenItemProcessor> itemProcessors = new ArrayList<>();

    public void findNeighbourCraftingBlocks(Level level, BlockPos pos) {
        final var openList = new ArrayDeque<BlockPos>();
        gatherNeighboursInto(openList, closedList, pos, true);

        while (!openList.isEmpty()) {
            final var current = openList.removeLast();
            final var state = level.getBlockState(current);
            final var blockEntity = level.getBlockEntity(current);

            boolean continueSearch = false;
            boolean extendUpwards = false;
            if (blockEntity != null) {
                var itemProvider = Balm.capabilities().getCapability(blockEntity, ModCapabilities.KITCHEN_ITEM_PROVIDER);
                if (itemProvider != null) {
                    itemProviders.add(itemProvider);
                }

                final var recipeProvider = Balm.capabilities().getCapability(blockEntity, ModCapabilities.KITCHEN_RECIPE_PROVIDER);
                if (recipeProvider != null) {
                    recipeProviders.add(recipeProvider);
                }

                final var itemProcessor = Balm.capabilities().getCapability(blockEntity, ModCapabilities.KITCHEN_ITEM_PROCESSOR);
                if (itemProcessor != null) {
                    itemProcessors.add(itemProcessor);
                }

                if (itemProvider != null || recipeProvider != null || itemProcessor != null || state.is(ModBlockTags.KITCHEN_CONNECTORS)) {
                    continueSearch = true;
                    extendUpwards = true;
                }
            } else if (state.is(ModBlockTags.KITCHEN_CONNECTORS)) {
                continueSearch = true;
            }

            if (closedList.size() >= MAX_SCANNED_POSITIONS) {
                CookingForBlockheads.logger.warn("Stopping kitchen multiblock search at {}", closedList.size());
                break;
            }

            if (continueSearch) {
                gatherNeighboursInto(openList, closedList, current, extendUpwards);
            }
        }
    }

    private void gatherNeighboursInto(ArrayDeque<BlockPos> openList, Set<BlockPos> closedList, BlockPos pos, boolean extendedUp) {
        for (final var direction : Direction.values()) {
            if (extendedUp && direction == Direction.UP) {
                final var above = pos.above();
                if (closedList.add(above)) {
                    openList.addLast(above);
                }
                final var aboveTwo = pos.above(2);
                if (closedList.add(aboveTwo)) {
                    openList.addLast(aboveTwo);
                }
            } else {
                final var neighbour = pos.relative(direction);
                if (closedList.add(neighbour)) {
                    openList.addLast(neighbour);
                }
            }
        }
    }

    public Kitchen createKitchen(Level level, boolean allowCrafting) {
        return new KitchenImpl(level, allowCrafting, itemProviders, recipeProviders, itemProcessors);
    }
}
