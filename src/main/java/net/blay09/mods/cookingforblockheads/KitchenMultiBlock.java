package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.blay09.mods.cookingforblockheads.container.FoodRecipeWithStatus;
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
                    IKitchenItemProvider itemProvider = tileEntity.getCapability(CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY, null);
                    if (itemProvider != null) {
                        itemProviderList.add(itemProvider);
                    }
                    IKitchenSmeltingProvider smeltingProvider = tileEntity.getCapability(CapabilityKitchenSmeltingProvider.KITCHEN_SMELTING_PROVIDER_CAPABILITY, null);
                    if (smeltingProvider != null) {
                        smeltingProviderList.add(smeltingProvider);
                    }
                    if(itemProvider != null || smeltingProvider != null) {
                        findNeighbourKitchenBlocks(world, position);
                    }
                }
            }
        }
    }

    private final List<IKitchenItemProvider> tmpSourceInventories = Lists.newArrayList();
    public List<IKitchenItemProvider> getSourceInventories(InventoryPlayer playerInventory) {
        tmpSourceInventories.clear();
        tmpSourceInventories.add(new KitchenItemProvider(new InvWrapper(playerInventory)));
        for (IKitchenItemProvider provider : itemProviderList) {
            tmpSourceInventories.add(provider);
        }
        return tmpSourceInventories;
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

    public void trySmelt(EntityPlayer player, FoodRecipeWithStatus recipe, boolean stack) {
        for(IKitchenItemProvider itemProvider : getSourceInventories(player.inventory)) {
            itemProvider.resetSimulation();
            for(int i = 0; i < itemProvider.getSlots(); i++) {
                ItemStack itemStack = itemProvider.getStackInSlot(i);
                if(itemStack != null) {
                    for(ItemStack sourceStack : recipe.getCraftMatrix().get(0).getItemStacks()) {
                        if(ItemUtils.areItemStacksEqualWithWildcard(itemStack, sourceStack)) {
                            int smeltCount = Math.min(itemStack.stackSize, stack ? sourceStack.getMaxStackSize() : 1);
                            ItemStack restStack = itemProvider.useItemStack(i, smeltCount, false);
                            if(restStack != null) {
                                restStack = smeltItem(restStack, smeltCount);
                                if(restStack != null) {
                                    restStack = itemProvider.returnItemStack(restStack);
                                    if(!player.inventory.addItemStackToInventory(restStack)) {
                                        player.dropPlayerItemWithRandomChoice(restStack, false);
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

    }

    public boolean hasSmeltingProvider() {
        return smeltingProviderList.size() > 0;
    }

}
