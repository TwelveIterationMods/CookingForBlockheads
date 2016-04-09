package net.blay09.mods.cookingforblockheads.container.inventory;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.container.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.Collections;
import java.util.List;

public class InventoryCraftBook extends InventoryCrafting {

	public InventoryCraftBook(Container container) {
		super(container, 3, 3);
	}

	public ItemStack tryCraft(FoodRecipeWithStatus recipe, EntityPlayer player, KitchenMultiBlock multiBlock) {
		int[] sourceInventories = new int[9];
		int[] sourceInventorySlots = new int[9];
		List<? extends IItemHandler> inventories = multiBlock != null ? multiBlock.getSourceInventories(player.inventory) : Collections.singletonList(new InvWrapper(player.inventory));
		List<FoodIngredient> craftMatrix = recipe.getCraftMatrix();
		int[][] usedStackSize = new int[inventories.size()][];
        for(int i = 0; i < usedStackSize.length; i++) {
            usedStackSize[i] = new int[inventories.get(i).getSlots()];
        }

		for(int i = 0; i < getSizeInventory(); i++) {
            setInventorySlotContents(i, null);
            sourceInventories[i] = -1;
            sourceInventorySlots[i] = -1;
        }

		matrixLoop:for(int i = 0; i < craftMatrix.size(); i++) {
			FoodIngredient ingredient = craftMatrix.get(i);
            if(ingredient != null) {
                for(int j = 0; j < inventories.size(); j++) {
					IItemHandler itemHandler = inventories.get(j);
                    for (int k = 0; k < itemHandler.getSlots(); k++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(k);
                        if (itemStack != null && ingredient.isValidItem(itemStack) && itemStack.stackSize - usedStackSize[j][k] > 0) {
                            usedStackSize[j][k]++;
                            setInventorySlotContents(i, itemStack);
                            sourceInventories[i] = j;
                            sourceInventorySlots[i] = k;
                            continue matrixLoop;
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
			if(result.getItem() == Items.bread) {
				player.addStat(AchievementList.makeBread, 1);
			} else if(result.getItem() == Items.cake) {
				player.addStat(AchievementList.bakeCake, 1);
			}
			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack itemStack = getStackInSlot(i);
				if(itemStack != null) {
					if(sourceInventories[i] != -1) {
						ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
						IItemHandler sourceInventory = inventories.get(sourceInventories[i]);
						if(sourceInventorySlots[i] != -1) {
							sourceInventory.extractItem(sourceInventorySlots[i], 1, false);
						}
						if(containerItem != null) {
							ItemStack restStack = ItemHandlerHelper.insertItemStacked(sourceInventory, containerItem, false);
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
