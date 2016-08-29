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

	private static class SourceItem {
		private IKitchenItemProvider sourceProvider;
		private int sourceSlot;
		private ItemStack sourceStack;

		public SourceItem(IKitchenItemProvider sourceProvider, int sourceSlot, ItemStack sourceStack) {
			this.sourceProvider = sourceProvider;
			this.sourceSlot = sourceSlot;
			this.sourceStack = sourceStack;
		}

		public IKitchenItemProvider getSourceProvider() {
			return sourceProvider;
		}

		public int getSourceSlot() {
			return sourceSlot;
		}

		public ItemStack getSourceStack() {
			return sourceStack;
		}
	}

	public InventoryCraftBook(Container container) {
		super(container, 3, 3);
	}

	public ItemStack tryCraft(ItemStack outputItem, List<ItemStack> craftMatrix, EntityPlayer player, KitchenMultiBlock multiBlock) {
		boolean requireContainer = CookingRegistry.doesItemRequireBucketForCrafting(outputItem);

		// Reset the simulation before we start
		List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
		for(IKitchenItemProvider itemProvider : inventories) {
			itemProvider.resetSimulation();
		}

		SourceItem[] sourceItems = new SourceItem[9];

		// Find matching items from source inventories
		matrixLoop:for(int i = 0; i < craftMatrix.size(); i++) {
			ItemStack ingredient = craftMatrix.get(i);
            if(ingredient != null) {
                for(int j = 0; j < inventories.size(); j++) {
					IKitchenItemProvider itemProvider = inventories.get(j);
                    for (int k = 0; k < itemProvider.getSlots(); k++) {
                        ItemStack itemStack = itemProvider.getStackInSlot(k);
                        if (ItemUtils.areItemStacksEqualWithWildcard(itemStack, ingredient)) {
							itemStack = itemProvider.useItemStack(k, 1, true, inventories, requireContainer);
							if(itemStack != null) {
								sourceItems[i] = new SourceItem(inventories.get(j), k, itemStack);
								continue matrixLoop;
							}
                        }
                    }
                }
            }
        }

		// Populate the crafting grid
		for(int i = 0; i < sourceItems.length; i++) {
			setInventorySlotContents(i, sourceItems[i] != null ? sourceItems[i].getSourceStack() : null);
		}

		// Find the matching recipe and make sure it matches what the client expects
		IRecipe craftRecipe = CookingRegistry.findFoodRecipe(this, player.worldObj);
		if(craftRecipe == null || craftRecipe.getRecipeOutput() == null || craftRecipe.getRecipeOutput().getItem() != outputItem.getItem()) {
			return null;
		}

		// Get the final result and remove ingredients
		ItemStack result = craftRecipe.getCraftingResult(this);
		if(result != null) {
			fireEventsAndHandleAchievements(player, result);
			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack itemStack = getStackInSlot(i);
				if(itemStack != null) {
					if(sourceItems[i] != null) {
						// Eat the ingredients
						IKitchenItemProvider sourceProvider = sourceItems[i].getSourceProvider();
						if(sourceItems[i].getSourceSlot() != -1) {
							sourceProvider.resetSimulation();
							sourceProvider.useItemStack(sourceItems[i].getSourceSlot(), 1, false, inventories, requireContainer);
						}

						// Return container items (like empty buckets)
						ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
						if(containerItem != null) {
							System.out.println("Got a container item, returning it to source provider");
							ItemStack restStack = sourceProvider.returnItemStack(containerItem);
							if(restStack != null && restStack.stackSize > 0) {
								System.out.println("apparently the source provider didn't want it back so I'll just drop it in your inventory");
								ItemHandlerHelper.giveItemToPlayer(player, restStack);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private void fireEventsAndHandleAchievements(EntityPlayer player, ItemStack result) {
		FMLCommonHandler.instance().firePlayerCraftingEvent(player, result, this);
		result.onCrafting(player.worldObj, player, 1);
		if(result.getItem() == Items.BREAD) {
			player.addStat(AchievementList.MAKE_BREAD, 1);
		} else if(result.getItem() == Items.CAKE) {
			player.addStat(AchievementList.BAKE_CAKE, 1);
		}
	}


}
