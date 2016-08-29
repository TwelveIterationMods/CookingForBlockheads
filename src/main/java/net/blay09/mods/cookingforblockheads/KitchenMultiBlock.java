package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;
import java.util.Set;

public class KitchenMultiBlock {

    private final Set<BlockPos> checkedPos = Sets.newHashSet();
    private final List<IKitchenItemProvider> itemProviderList = Lists.newArrayList();
    private final List<IKitchenSmeltingProvider> smeltingProviderList = Lists.newArrayList();

    public KitchenMultiBlock(World world, BlockPos pos) {
        findNeighbourKitchenBlocks(world, pos);
    }

    private void findNeighbourKitchenBlocks(World world, BlockPos pos) {
        for (int i = 0; i <= 5; i++) {
            EnumFacing dir = EnumFacing.getFront(i);
			BlockPos position = pos.offset(dir);
            if (!checkedPos.contains(position)) {
                checkedPos.add(position);
                TileEntity tileEntity = world.getTileEntity(position);
                if(tileEntity != null) {
                    IKitchenItemProvider itemProvider = tileEntity.getCapability(CapabilityKitchenItemProvider.CAPABILITY, null);
                    if (itemProvider != null) { // Forge needs to update their patches to include @Nullable annotations
                        itemProviderList.add(itemProvider);
                    }
                    IKitchenSmeltingProvider smeltingProvider = tileEntity.getCapability(CapabilityKitchenSmeltingProvider.CAPABILITY, null);
                    if (smeltingProvider != null) { // Forge needs to update their patches to include @Nullable annotations
                        smeltingProviderList.add(smeltingProvider);
                    }
                    if(itemProvider != null || smeltingProvider != null || tileEntity.hasCapability(CapabilityKitchenConnector.CAPABILITY, null)) { // Forge needs to update their patches to include @Nullable annotations
                        findNeighbourKitchenBlocks(world, position);
                    }
                }
            }
        }
    }

    public List<IKitchenItemProvider> getSourceInventories(InventoryPlayer playerInventory) {
        List<IKitchenItemProvider> sourceInventories = Lists.newArrayList();
        for (IKitchenItemProvider provider : itemProviderList) {
            sourceInventories.add(provider);
        }
        sourceInventories.add(new KitchenItemProvider(new InvWrapper(playerInventory)));
        return sourceInventories;
    }

    public ItemStack smeltItem(ItemStack itemStack, int count) {
        ItemStack restStack = itemStack.copy().splitStack(count);
        for (IKitchenSmeltingProvider provider : smeltingProviderList) {
            restStack = provider.smeltItem(restStack);
            if (restStack == null) {
                break;
            }
        }
        itemStack.stackSize -= (count - (restStack != null ? restStack.stackSize : 0));
        if (itemStack.stackSize <= 0) {
            return null;
        }
        return itemStack;
    }

    public void trySmelt(ItemStack outputItem, ItemStack inputItem, EntityPlayer player, boolean stack) {
        if(inputItem == null) {
            return;
        }
        List<IKitchenItemProvider> inventories = getSourceInventories(player.inventory);
        for(IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
            for(int i = 0; i < itemProvider.getSlots(); i++) {
                ItemStack itemStack = itemProvider.getStackInSlot(i);
                if(itemStack != null) {
                    if(ItemUtils.areItemStacksEqualWithWildcard(itemStack, inputItem)) {
                        int smeltCount = Math.min(itemStack.stackSize, stack ? inputItem.getMaxStackSize() : 1);
                        ItemStack restStack = itemProvider.useItemStack(i, smeltCount, false, inventories);
                        if(restStack != null) {
                            restStack = smeltItem(restStack, smeltCount);
                            if(restStack != null) {
                                restStack = itemProvider.returnItemStack(restStack);
                                if(!player.inventory.addItemStackToInventory(restStack)) {
                                    player.dropItem(restStack, false);
                                }
                            }
                            player.openContainer.detectAndSendChanges();
                            return;
                        }
                    }
                }
            }
        }

    }

    public boolean hasSmeltingProvider() {
        return smeltingProviderList.size() > 0;
    }

}
