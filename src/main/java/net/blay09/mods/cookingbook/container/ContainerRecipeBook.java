package net.blay09.mods.cookingbook.container;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.blay09.mods.cookingbook.food.IFoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContainerRecipeBook extends Container {

	private final IInventory sourceInventory;
	private final boolean allowCrafting;
	private final InventoryRecipeBook recipeBook;
	private final InventoryRecipeBookMatrix craftMatrix;
	private final SlotPreview[] previewSlots = new SlotPreview[9];
	private final SlotRecipe[] recipeSlots = new SlotRecipe[12];
	private final ArrayListMultimap<String, IFoodRecipe> availableRecipes = ArrayListMultimap.create();
	private final List<ItemStack> sortedRecipes = new ArrayList<ItemStack>();

	private final InventoryCraftBook craftBook;

	private boolean furnaceMode;
	private int scrollOffset;
	private List<IFoodRecipe> currentRecipeList;
	private int currentRecipeIdx;
	private boolean isMissingTools;

	public ContainerRecipeBook(InventoryPlayer playerInventory, IInventory sourceInventory, boolean allowCrafting) {
		this.sourceInventory = sourceInventory;
		this.allowCrafting = allowCrafting;

		craftMatrix = new InventoryRecipeBookMatrix();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				previewSlots[j + i * 3] = new SlotPreview(craftMatrix, j + i * 3, 24 + j * 18, 20 + i * 18);
				previewSlots[j + i * 3].setSourceInventory(sourceInventory);
				addSlotToContainer(previewSlots[j + i * 3]);
			}
		}

		recipeBook = new InventoryRecipeBook();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				recipeSlots[j + i * 3] = new SlotRecipe(recipeBook, j + i * 3, 102 + j * 18, 11 + i * 18);
				addSlotToContainer(recipeSlots[j + i * 3]);
			}
		}

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 150));
		}

		for(IFoodRecipe foodRecipe : FoodRegistry.getFoodRecipes()) {
			ItemStack foodStack = foodRecipe.getOutputItem();
			if(foodStack != null) {
				if(FoodRegistry.isAvailableFor(foodRecipe.getCraftMatrix(), sourceInventory)) {
					String foodStackString = foodStack.toString();
					if(!availableRecipes.containsKey(foodStackString)) {
						sortedRecipes.add(foodStack);
					}
					availableRecipes.put(foodStackString, foodRecipe);
				}
			}
		}
		sortRecipes(new ComparatorName());
		updateRecipeList();
		setCraftMatrix(null);

		craftBook = new InventoryCraftBook(this, sourceInventory);
	}

	public void setCraftMatrix(IFoodRecipe recipe) {
		if(recipe != null) {
			furnaceMode = recipe.isSmeltingRecipe();
			if(furnaceMode) {
				for(SlotPreview previewSlot : previewSlots) {
					previewSlot.setIngredient(null);
					previewSlot.setEnabled(false);
				}
				previewSlots[4].setIngredient(recipe.getCraftMatrix()[0]);
				previewSlots[4].setEnabled(true);
				previewSlots[4].update();
			} else {
				int offset = 0;
				if (recipe.getCraftMatrix().length <= 3) {
					offset += 3;
				}
				for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
					int recipeIdx = i - offset;
					if (recipeIdx >= 0 && recipeIdx < recipe.getCraftMatrix().length) {
						previewSlots[i].setIngredient(recipe.getCraftMatrix()[recipeIdx]);
					} else {
						previewSlots[i].setIngredient(null);
					}
					previewSlots[i].setEnabled(true);
					previewSlots[i].update();
				}
			}
		} else {
			for(SlotPreview previewSlot : previewSlots) {
				previewSlot.setIngredient(null);
				previewSlot.setEnabled(false);
			}
		}
	}

	public boolean hasVariants() {
		return currentRecipeList != null && currentRecipeList.size() > 1;
	}

	public void prevRecipe() {
		if(currentRecipeList != null) {
			currentRecipeIdx--;
			if (currentRecipeIdx < 0) {
				currentRecipeIdx = currentRecipeList.size() - 1;
			}
			setCraftMatrix(currentRecipeList.get(currentRecipeIdx));
		}
	}

	public void nextRecipe() {
		if(currentRecipeList != null) {
			currentRecipeIdx++;
			if (currentRecipeIdx >= currentRecipeList.size()) {
				currentRecipeIdx = 0;
			}
			setCraftMatrix(currentRecipeList.get(currentRecipeIdx));
		}
	}

	public void setScrollOffset(int scrollOffset) {
		this.scrollOffset = scrollOffset;
		updateRecipeList();
	}

	public void updateRecipeList() {
		boolean noRecipes = getAvailableRecipeCount() == 0;
		for(int i = 0; i < recipeBook.getSizeInventory(); i++) {
			int recipeIdx = i + scrollOffset * 3;
			if(recipeIdx < sortedRecipes.size()) {
				recipeBook.setFoodItem(i, availableRecipes.get(sortedRecipes.get(recipeIdx).toString()));
			} else {
				recipeBook.setFoodItem(i, null);
			}
			recipeSlots[i].putStack(recipeBook.getStackInSlot(i));
			recipeSlots[i].setEnabled(!noRecipes);
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
		if((mode == 0 || mode == 1) && slotIdx > 0 && inventorySlots.get(slotIdx) instanceof SlotRecipe) {
			SlotRecipe slot = (SlotRecipe) inventorySlots.get(slotIdx);
			List<IFoodRecipe> newRecipeList = recipeBook.getFoodList(slot.getSlotIndex());
			if(allowCrafting && currentRecipeList != null && currentRecipeList == newRecipeList) {
				tryCraft(player, currentRecipeList, currentRecipeIdx, mode == 1);
				return null;
			}
			currentRecipeList = newRecipeList;
			currentRecipeIdx = 0;
			if(currentRecipeList != null) {
				IFoodRecipe recipe = currentRecipeList.get(currentRecipeIdx);
				setCraftMatrix(recipe);
				if(!recipe.isSmeltingRecipe()) {
					craftBook.prepareRecipe(sourceInventory, recipe);
					isMissingTools = !recipe.getCraftingRecipe().matches(craftBook, player.worldObj);
				}
			}
			return null;
		}
		return super.slotClick(slotIdx, button, mode, player);
	}

	private void tryCraft(EntityPlayer player, List<IFoodRecipe> recipeList, int recipeIdx, boolean isShiftDown) {
		IFoodRecipe recipe = recipeList.get(recipeIdx);
		if(recipe.isSmeltingRecipe()) {
			return;
		}
		if(!isShiftDown) {
			if(craftBook.canMouseItemHold(player, recipe)) {
				ItemStack craftingResult = craftBook.craft(player, sourceInventory, recipe);
				if(craftingResult != null) {
					ItemStack mouseItem = player.inventory.getItemStack();
					if (mouseItem != null) {
						mouseItem.stackSize += craftingResult.stackSize;
					} else {
						player.inventory.setItemStack(craftingResult);
					}
				}
			}
		} else {
			ItemStack craftingResult;
			while((craftingResult = craftBook.craft(player, sourceInventory, recipe)) != null) {
				if(!player.inventory.addItemStackToInventory(craftingResult)) {
					if (player.inventory.getItemStack() == null) {
						player.inventory.setItemStack(craftingResult);
					} else {
						player.dropPlayerItemWithRandomChoice(craftingResult, false);
					}
					break;
				}
			}
		}
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

	public void sortRecipes(Comparator<ItemStack> comparator) {
		Collections.sort(sortedRecipes, comparator);
		updateRecipeList();
	}

	public boolean hasSelection() {
		return currentRecipeList != null;
	}

	public boolean canClickCraft(int slotIndex) {
		return allowCrafting && currentRecipeList != null && recipeBook.getFoodList(slotIndex) == currentRecipeList && !currentRecipeList.get(currentRecipeIdx).isSmeltingRecipe();
	}

	public boolean isMissingTools() {
		return isMissingTools;
	}
}
