package net.blay09.mods.cookingforblockheads.crafting;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.provider.ProviderUtils;
import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KitchenImpl implements Kitchen {

    private final ItemStack activatingItemStack;
    private final BlockState activatingBlockState;
    private final Set<BlockPos> checkedPos = new HashSet<>();
    private final List<KitchenItemProvider> itemProviderList = new ArrayList<>();
    private final Set<Block> providedBlocks = new HashSet<>();
    private final Set<TagKey<Block>> providedBlockTags = new HashSet<>();

    public KitchenImpl(ItemStack itemStack) {
        activatingItemStack = itemStack;
        activatingBlockState = Blocks.AIR.defaultBlockState();
    }

    public KitchenImpl(Level level, BlockPos pos) {
        activatingBlockState = level.getBlockState(pos);
        activatingItemStack = ItemStack.EMPTY;
        providedBlocks.add(activatingBlockState.getBlock());
        providedBlockTags.add(activatingBlockState.getTags().iterator().next());
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
                        final var itemProvider = ProviderUtils.getProvider(blockEntity, KitchenItemProvider.class);
                        if (itemProvider != null) {
                            itemProviderList.add(itemProvider);

                            providedBlocks.add(state.getBlock());
                            state.getTags().forEach(providedBlockTags::add);

                            findNeighbourCraftingBlocks(level, position, true);
                        }
                    } else if (state.is(ModBlockTags.KITCHEN_CONNECTORS)) {
                        providedBlocks.add(state.getBlock());
                        state.getTags().forEach(providedBlockTags::add);

                        findNeighbourCraftingBlocks(level, position, false);
                    }
                }
            }
        }
    }

    @Override
    public List<KitchenItemProvider> getItemProviders(@Nullable Player player) {
        final var  sourceInventories = new ArrayList<KitchenItemProvider>();
        sourceInventories.addAll(itemProviderList);
        if (player != null) {
            sourceInventories.add(new ContainerKitchenItemProvider(player.getInventory()));
        }
        return sourceInventories;
    }

}
