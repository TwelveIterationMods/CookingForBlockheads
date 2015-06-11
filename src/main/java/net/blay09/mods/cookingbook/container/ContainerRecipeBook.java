package net.blay09.mods.cookingbook.container;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerRecipeBook extends Container {

	private final InventoryPlayer sourceInventory;
	private final InventoryRecipeBook recipeBook;
	private final InventoryRecipeBookMatrix craftMatrix;
	private final SlotPreview[] previewSlots = new SlotPreview[9];
	private final ArrayListMultimap<String, IFoodRecipe> availableRecipes = ArrayListMultimap.create();
	private final List<ItemStack> sortedRecipes = new ArrayList<ItemStack>();
	private boolean furnaceMode;

	public ContainerRecipeBook(InventoryPlayer inventory) {
		this.sourceInventory = inventory;

		craftMatrix = new InventoryRecipeBookMatrix();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				previewSlots[j + i * 3] = new SlotPreview(craftMatrix, j + i * 3, 23 + j * 18, 20 + i * 18);
				previewSlots[j + i * 3].setSourceInventory(sourceInventory);
				addSlotToContainer(previewSlots[j + i * 3]);
			}
		}

		recipeBook = new InventoryRecipeBook();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				addSlotToContainer(new SlotRecipe(recipeBook, j + i * 3, 102 + j * 18, 11 + i * 18));
			}
		}

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 150));
		}

		for(IFoodRecipe foodRecipe : FoodRegistry.getFoodRecipes()) {
			ItemStack foodStack = foodRecipe.getOutputItem();
			if(foodStack != null) {
				if(FoodRegistry.isAvailableFor(foodRecipe.getCraftMatrix(), inventory)) {
					String foodStackString = foodStack.toString();
					if(!availableRecipes.containsKey(foodStackString)) {
						sortedRecipes.add(foodStack);
					}
					availableRecipes.put(foodStackString, foodRecipe);
				}
			}
		}

		setScrollOffset(0);
	}

	public void setScrollOffset(int scrollOffset) {
		for(int i = 0; i < recipeBook.getSizeInventory(); i++) {
			int recipeIdx = i + scrollOffset * 3;
			if(recipeIdx < sortedRecipes.size()) {
				recipeBook.setFoodItem(i, availableRecipes.get(sortedRecipes.get(recipeIdx).toString()).get(0));
			} else {
				recipeBook.setFoodItem(i, null);
			}
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for(SlotPreview previewSlot : previewSlots) {
			previewSlot.update();
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack slotClick(int slotIdx, int button, int mode, EntityPlayer player) {
		if(mode == 0 && slotIdx > 0 && inventorySlots.get(slotIdx) instanceof SlotRecipe) {
			SlotRecipe slot = (SlotRecipe) inventorySlots.get(slotIdx);
			IFoodRecipe foodItem = recipeBook.getFoodItem(slot.getSlotIndex());
			if(foodItem != null) {
				furnaceMode = foodItem.isSmeltingRecipe();
				if(furnaceMode) {
					for(SlotPreview previewSlot : previewSlots) {
						previewSlot.setIngredient(null);
						previewSlot.setEnabled(false);
					}
					previewSlots[4].setIngredient(foodItem.getCraftMatrix()[0]);
					previewSlots[4].setEnabled(true);
				} else {
					int offset = 0;
					if (foodItem.getCraftMatrix().length <= 3) {
						offset += 3;
					}
					for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
						int recipeIdx = i - offset;
						if (recipeIdx >= 0 && recipeIdx < foodItem.getCraftMatrix().length) {
							previewSlots[i].setIngredient(foodItem.getCraftMatrix()[recipeIdx]);
						} else {
							previewSlots[i].setIngredient(null);
						}
						previewSlots[i].setEnabled(true);
					}
				}
			}
			return null;
		}
		return super.slotClick(slotIdx, button, mode, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();
			if (i >= 48 && i < 57) { // Inventory to Hotbar
				if (!mergeItemStack(slotStack, 21, 48, false)) {
					return null;
				}
			} else if(i >= 21 && i < 48) { // Hotbar to Inventory
				if (!mergeItemStack(slotStack, 48, 57, false)) {
					return null;
				}
			}

			if (slotStack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (slotStack.stackSize == itemStack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, slotStack);
		}
		return itemStack;
	}

	public int getAvailableRecipeCount() {
		return sortedRecipes.size();
	}

	public boolean isFurnaceMode() {
		return furnaceMode;
	}
}
