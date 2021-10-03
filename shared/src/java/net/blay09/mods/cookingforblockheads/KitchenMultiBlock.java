package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.blay09.mods.cookingforblockheads.api.IKitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KitchenMultiBlock implements IKitchenMultiBlock {

    private final static List<Block> blockConnectors = new ArrayList<>();

    private final Set<BlockPos> checkedPos = Sets.newHashSet();
    private final List<IKitchenItemProvider> itemProviderList = Lists.newArrayList();
    private final List<IKitchenSmeltingProvider> smeltingProviderList = Lists.newArrayList();

    private KitchenMultiBlock(Level level, BlockPos pos) {
        findNeighbourKitchenBlocks(level, pos, true);
    }

    public static KitchenMultiBlock buildFromLocation(Level level, BlockPos pos) {
        return new KitchenMultiBlock(level, pos);
    }

    private void findNeighbourKitchenBlocks(Level level, BlockPos pos, boolean extendedUpSearch) {
        for (Direction direction : Direction.values()) {
            int upSearch = (extendedUpSearch && direction == Direction.UP) ? 2 : 1;
            for (int n = 1; n <= upSearch; n++) {
                BlockPos position = pos.relative(direction, n);
                if (!checkedPos.contains(position)) {
                    checkedPos.add(position);
                    BlockEntity tileEntity = level.getBlockEntity(position);
                    if (tileEntity != null) {
                        LazyOptional<IKitchenItemProvider> itemProviderCap = tileEntity.getCapability(CapabilityKitchenItemProvider.CAPABILITY);
                        itemProviderCap.ifPresent(itemProviderList::add);

                        LazyOptional<IKitchenSmeltingProvider> smeltingProviderCap = tileEntity.getCapability(CapabilityKitchenSmeltingProvider.CAPABILITY);
                        smeltingProviderCap.ifPresent(smeltingProviderList::add);

                        if (itemProviderCap.isPresent() || smeltingProviderCap.isPresent() || tileEntity.getCapability(CapabilityKitchenConnector.CAPABILITY).isPresent()) {
                            findNeighbourKitchenBlocks(level, position, true);
                        }
                    } else {
                        BlockState state = level.getBlockState(position);
                        if (!state.isAir() && blockConnectors.contains(state.getBlock())) {
                            findNeighbourKitchenBlocks(level, position, false);
                        }
                    }
                }
            }
        }
    }

    public static void registerConnectorBlock(final Block block) {
        blockConnectors.add(block);
    }

    @Override
    public List<IKitchenItemProvider> getItemProviders(Inventory playerInventory) {
        List<IKitchenItemProvider> sourceInventories = Lists.newArrayList();
        sourceInventories.addAll(itemProviderList);
        sourceInventories.add(new DefaultKitchenItemProvider(playerInventory));
        return sourceInventories;
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack, int count) {
        ItemStack restStack = itemStack.copy().split(count);
        for (IKitchenSmeltingProvider provider : smeltingProviderList) {
            restStack = provider.smeltItem(restStack);
            if (restStack.isEmpty()) {
                break;
            }
        }

        return restStack;
    }

    public void trySmelt(ItemStack outputItem, ItemStack inputItem, Player player, boolean stack) {
        if (inputItem.isEmpty()) {
            return;
        }

        boolean requireBucket = CookingRegistry.doesItemRequireBucketForCrafting(outputItem);
        List<IKitchenItemProvider> inventories = getItemProviders(player.getInventory());
        for (IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
            IngredientPredicate predicate = (it, count) -> it.sameItem(inputItem) && count > 0;
            int count = stack ? inputItem.getMaxStackSize() : 1;
            SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed(predicate, count, inventories, requireBucket, false);
            if (sourceItem != null) {
                ItemStack sourceStack = sourceItem.getSourceStack();
                int amount = Math.min(sourceStack.getCount(), count);
                ItemStack restStack = smeltItem(sourceStack, amount);
                if (!restStack.isEmpty()) {
                    restStack = itemProvider.returnItemStack(restStack, sourceItem);
                    if (!player.getInventory().add(restStack)) {
                        player.drop(restStack, false);
                    }
                }
                player.containerMenu.broadcastChanges();
                return;
            }
        }

    }

    @Override
    public boolean hasSmeltingProvider() {
        return smeltingProviderList.size() > 0;
    }

}
