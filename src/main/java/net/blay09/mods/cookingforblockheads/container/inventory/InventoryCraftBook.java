package net.blay09.mods.cookingforblockheads.container.inventory;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class InventoryCraftBook extends InventoryCrafting {

	public InventoryCraftBook(Container container) {
		super(container, 3, 3);
	}

	public ItemStack tryCraft(ItemStack outputItem, List<ItemStack> craftMatrix, EntityPlayer player, KitchenMultiBlock multiBlock) {
		int[] sourceInventories = new int[9];
		int[] sourceInventorySlots = new int[9];
		List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
		for(IKitchenItemProvider itemProvider : inventories) {
			itemProvider.resetSimulation();
		}
		for(int i = 0; i < getSizeInventory(); i++) {
            setInventorySlotContents(i, null);
            sourceInventories[i] = -1;
            sourceInventorySlots[i] = -1;
        }

		matrixLoop:for(int i = 0; i < craftMatrix.size(); i++) {
			ItemStack ingredient = craftMatrix.get(i);
            if(ingredient != null) {
                for(int j = 0; j < inventories.size(); j++) {
					IKitchenItemProvider itemProvider = inventories.get(j);
                    for (int k = 0; k < itemProvider.getSlots(); k++) {
                        ItemStack itemStack = itemProvider.getStackInSlot(k);
                        if (ItemUtils.areItemStacksEqualWithWildcard(itemStack, ingredient)) {
							itemStack = itemProvider.useItemStack(k, 1, true, inventories);
							if(itemStack != null) {
								setInventorySlotContents(i, itemStack);
								sourceInventories[i] = j;
								sourceInventorySlots[i] = k;
								continue matrixLoop;
							}
                        }
                    }
                }
            }
        }
		IRecipe craftRecipe = CookingRegistry.findFoodRecipe(this, player.worldObj);
		if(craftRecipe == null) {
			return null;
		}
		ItemStack result = craftRecipe.getCraftingResult(this);
		if(result != null) {
			FMLCommonHandler.instance().firePlayerCraftingEvent(player, result, this);
			result.onCrafting(player.worldObj, player, 1);
			if(result.getItem() == Items.BREAD) {
				player.addStat(AchievementList.MAKE_BREAD, 1);
			} else if(result.getItem() == Items.CAKE) {
				player.addStat(AchievementList.BAKE_CAKE, 1);
			}
			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack itemStack = getStackInSlot(i);
				if(itemStack != null) {
					if(sourceInventories[i] != -1) {
						ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
						IKitchenItemProvider sourceProvider = inventories.get(sourceInventories[i]);
						if(sourceInventorySlots[i] != -1) {
							sourceProvider.resetSimulation();
							sourceProvider.useItemStack(sourceInventorySlots[i], 1, false, inventories);
						}
						if(containerItem != null) {
							ItemStack restStack = sourceProvider.returnItemStack(containerItem);
							if(restStack != null && restStack.stackSize > 0) {
								ItemHandlerHelper.giveItemToPlayer(player, restStack);
							}
						}
					}
				}
			}
		}
		return result;
	}

}
