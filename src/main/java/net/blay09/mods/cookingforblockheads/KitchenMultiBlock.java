package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.blay09.mods.cookingforblockheads.api.IKitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KitchenMultiBlock implements IKitchenMultiBlock {

    private final static List<Block> blockConnectors = new ArrayList<>();

    private final Set<BlockPos> checkedPos = Sets.newHashSet();
    private final List<IKitchenItemProvider> itemProviderList = Lists.newArrayList();
    private final List<IKitchenSmeltingProvider> smeltingProviderList = Lists.newArrayList();

    public KitchenMultiBlock(World world, BlockPos pos) {
        findNeighbourKitchenBlocks(world, pos, true);
    }

    private void findNeighbourKitchenBlocks(World world, BlockPos pos, boolean extendedUpSearch) {
        for (Direction direction : Direction.values()) {
            int upSearch = (extendedUpSearch && direction == Direction.UP) ? 2 : 1;
            for (int n = 1; n <= upSearch; n++) {
                BlockPos position = pos.offset(direction, n);
                if (!checkedPos.contains(position)) {
                    checkedPos.add(position);
                    TileEntity tileEntity = world.getTileEntity(position);
                    if (tileEntity != null) {
                        LazyOptional<IKitchenItemProvider> itemProviderCap = tileEntity.getCapability(CapabilityKitchenItemProvider.CAPABILITY);
                        itemProviderCap.ifPresent(itemProviderList::add);


                        LazyOptional<IKitchenSmeltingProvider> smeltingProviderCap = tileEntity.getCapability(CapabilityKitchenSmeltingProvider.CAPABILITY);
                        smeltingProviderCap.ifPresent(smeltingProviderList::add);

                        if (itemProviderCap.isPresent() || smeltingProviderCap.isPresent() || tileEntity.getCapability(CapabilityKitchenConnector.CAPABILITY).isPresent()) {
                            findNeighbourKitchenBlocks(world, position, true);
                        }
                    } else {
                        BlockState state = world.getBlockState(position);
                        if (blockConnectors.contains(state.getBlock())) {
                            findNeighbourKitchenBlocks(world, position, false);
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
    public List<IKitchenItemProvider> getItemProviders(PlayerInventory playerInventory) {
        List<IKitchenItemProvider> sourceInventories = Lists.newArrayList();
        sourceInventories.addAll(itemProviderList);
        sourceInventories.add(new KitchenItemProvider(new InvWrapper(playerInventory)));
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

    public void trySmelt(ItemStack outputItem, ItemStack inputItem, PlayerEntity player, boolean stack) {
        if (inputItem.isEmpty()) {
            return;
        }

        boolean requireBucket = CookingRegistry.doesItemRequireBucketForCrafting(outputItem);
        List<IKitchenItemProvider> inventories = getItemProviders(player.inventory);
        for (IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
            IngredientPredicate predicate = (it, count) -> ItemUtils.areItemStacksEqualWithWildcard(it, inputItem) && count > 0;
            int count = stack ? inputItem.getMaxStackSize() : 1;
            SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed(predicate, count, inventories, requireBucket, false);
            if (sourceItem != null) {
                ItemStack sourceStack = sourceItem.getSourceStack();
                int amount = Math.min(sourceStack.getCount(), count);
                ItemStack restStack = smeltItem(sourceStack, amount);
                if (!restStack.isEmpty()) {
                    restStack = itemProvider.returnItemStack(restStack, sourceItem);
                    if (!player.inventory.addItemStackToInventory(restStack)) {
                        player.dropItem(restStack, false);
                    }
                }
                player.openContainer.detectAndSendChanges();
                return;
            }
        }

    }

    @Override
    public boolean hasSmeltingProvider() {
        return smeltingProviderList.size() > 0;
    }

}
