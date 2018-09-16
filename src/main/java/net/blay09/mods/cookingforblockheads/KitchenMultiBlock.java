package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.blay09.mods.cookingforblockheads.api.IKitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        for (int i = 0; i <= 5; i++) {
            EnumFacing dir = EnumFacing.getFront(i);
            int upSearch = (extendedUpSearch && dir == EnumFacing.UP) ? 2 : 1;
            for (int n = 1; n <= upSearch; n++) {
                BlockPos position = pos.offset(dir, n);
                if (!checkedPos.contains(position)) {
                    checkedPos.add(position);
                    TileEntity tileEntity = world.getTileEntity(position);
                    if (tileEntity != null) {
                        IKitchenItemProvider itemProvider = tileEntity.getCapability(CapabilityKitchenItemProvider.CAPABILITY, null);
                        if (itemProvider != null) {
                            itemProviderList.add(itemProvider);
                        }

                        IKitchenSmeltingProvider smeltingProvider = tileEntity.getCapability(CapabilityKitchenSmeltingProvider.CAPABILITY, null);
                        if (smeltingProvider != null) {
                            smeltingProviderList.add(smeltingProvider);
                        }

                        if (itemProvider != null || smeltingProvider != null || tileEntity.hasCapability(CapabilityKitchenConnector.CAPABILITY, null)) {
                            findNeighbourKitchenBlocks(world, position, true);
                        }
                    } else {
                        IBlockState state = world.getBlockState(position);
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
    public List<IKitchenItemProvider> getItemProviders(InventoryPlayer playerInventory) {
        List<IKitchenItemProvider> sourceInventories = Lists.newArrayList();
        sourceInventories.addAll(itemProviderList);
        sourceInventories.add(new KitchenItemProvider(new InvWrapper(playerInventory)));
        return sourceInventories;
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack, int count) {
        ItemStack restStack = itemStack.copy().splitStack(count);
        for (IKitchenSmeltingProvider provider : smeltingProviderList) {
            restStack = provider.smeltItem(restStack);
            if (restStack.isEmpty()) {
                break;
            }
        }

        itemStack.shrink(count - (!restStack.isEmpty() ? restStack.getCount() : 0));
        return itemStack;
    }

    public void trySmelt(ItemStack outputItem, ItemStack inputItem, EntityPlayer player, boolean stack) {
        if (inputItem.isEmpty()) {
            return;
        }

        boolean requireBucket = CookingRegistry.doesItemRequireBucketForCrafting(outputItem);
        List<IKitchenItemProvider> inventories = getItemProviders(player.inventory);
        for (IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
            ItemStack found = itemProvider.findAndMarkAsUsed((it, count) -> ItemUtils.areItemStacksEqualWithWildcard(it, inputItem) && count > 0, stack ? inputItem.getMaxStackSize() : 1, inventories, requireBucket, false);
            if (!found.isEmpty()) {
                int amount = Math.min(found.getCount(), stack ? inputItem.getMaxStackSize() : 1);
                ItemStack restStack = smeltItem(found, amount);
                if (!restStack.isEmpty()) {
                    restStack = itemProvider.returnItemStack(restStack);
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
