package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeProvider;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KitchenMultiblockScanner {
    private final Set<BlockPos> checkedPos = new HashSet<>();
    private final List<KitchenItemProvider> itemProviders = new ArrayList<>();
    private final List<KitchenRecipeProvider> recipeProviders = new ArrayList<>();
    private final List<KitchenItemProcessor> itemProcessors = new ArrayList<>();

    public void findNeighbourCraftingBlocks(Level level, BlockPos pos) {
        findNeighbourCraftingBlocks(level, pos, true);
    }

    public void findNeighbourCraftingBlocks(Level level, BlockPos pos, boolean extendedUpSearch) {
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
                            findNeighbourCraftingBlocks(level, position, true);
                        }
                    } else if (state.is(ModBlockTags.KITCHEN_CONNECTORS)) {
                        findNeighbourCraftingBlocks(level, position, false);
                    }
                }
            }
        }
    }

    public Kitchen createKitchen(Level level, boolean allowCrafting) {
        return new KitchenImpl(level, allowCrafting, itemProviders, recipeProviders, itemProcessors);
    }
}
