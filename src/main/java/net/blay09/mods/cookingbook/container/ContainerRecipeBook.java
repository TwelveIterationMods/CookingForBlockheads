package net.blay09.mods.cookingbook.container;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.blay09.mods.cookingbook.network.MessageClickRecipe;
import net.blay09.mods.cookingbook.network.MessageSyncList;
import net.blay09.mods.cookingbook.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContainerRecipeBook extends Container {

	private final EntityPlayer player;
	private final IInventory sourceInventory;
	private final boolean allowCrafting;
	private final boolean isClientSide;
	private final InventoryRecipeBook recipeBook;
	private final InventoryRecipeBookMatrix craftMatrix;
	private final SlotPreview[] previewSlots = new SlotPreview[9];
	private final SlotRecipe[] recipeSlots = new SlotRecipe[12];
	private final ArrayListMultimap<String, FoodRecipe> availableRecipes = ArrayListMultimap.create();
	private final List<ItemStack> sortedRecipes = new ArrayList<ItemStack>();

	private final InventoryCraftBook craftBook;

	private boolean isDirty;
	private boolean furnaceMode;
	private int scrollOffset;
	private String currentRecipeKey;
	private List<FoodRecipe> currentRecipeList;
	private int currentRecipeIdx;
	private boolean isMissingTools;
	private Comparator<ItemStack> currentSort = new ComparatorName();

	public ContainerRecipeBook(EntityPlayer player, IInventory sourceInventory, boolean allowCrafting, boolean isClientSide) {
		this.player = player;
		this.sourceInventory = sourceInventory;
		this.allowCrafting = allowCrafting;
		this.isClientSide = isClientSide;

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
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 150));
		}

		if(!isClientSide) {
			findAvailableRecipes();
			sortRecipes(currentSort);
		}
		updateRecipeList();

		craftBook = new InventoryCraftBook(this, sourceInventory);
	}

	public void setCraftMatrix(FoodRecipe recipe) {
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
		if(noRecipes) {
			setCraftMatrix(null);
			currentRecipeKey = null;
			currentRecipeList = null;
			currentRecipeIdx = 0;
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if(isDirty && !isClientSide) {
			NetworkHandler.instance.sendTo(new MessageSyncList(sortedRecipes, availableRecipes, currentRecipeIdx), (EntityPlayerMP) player);
			isDirty = false;
		}

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
		if(isClientSide && (mode == 0 || mode == 1)) {
			clickRecipe(slotIdx, mode == 1);
			NetworkHandler.instance.sendToServer(new MessageClickRecipe(slotIdx, scrollOffset, mode == 1));
		}
		return super.slotClick(slotIdx, button, mode, player);
	}

	public void clickRecipe(int slotIdx, boolean shiftClick) {
		if(slotIdx > 0 && slotIdx < inventorySlots.size() && inventorySlots.get(slotIdx) instanceof SlotRecipe) {
			SlotRecipe slot = (SlotRecipe) inventorySlots.get(slotIdx);
			if (slot.getStack() != null) {
				if (allowCrafting && currentRecipeKey != null && slot.getStack().toString().equals(currentRecipeKey)) {
					tryCraft(player, currentRecipeList, currentRecipeIdx, shiftClick);
					return;
				}
				currentRecipeKey = slot.getStack().toString();
				currentRecipeList = recipeBook.getFoodList(slot.getSlotIndex());
				currentRecipeIdx = 0;
				if (currentRecipeList != null) {
					FoodRecipe recipe = currentRecipeList.get(currentRecipeIdx);
					setCraftMatrix(recipe);
					if (!recipe.isSmeltingRecipe()) {
						craftBook.prepareRecipe(player, sourceInventory, recipe);
						isMissingTools = !craftBook.matches(player.worldObj);
					}
				}
			}
		}
	}

	private void tryCraft(EntityPlayer player, List<FoodRecipe> recipeList, int recipeIdx, boolean isShiftDown) {
		FoodRecipe recipe = recipeList.get(recipeIdx);
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
		if(!isClientSide) {
			findAvailableRecipes();
			sortRecipes(currentSort);
			updateRecipeList();
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

	public boolean hasSelection() {
		return currentRecipeList != null;
	}

	public boolean canClickCraft(int slotIndex) {
		return allowCrafting && currentRecipeKey != null && recipeBook.getStackInSlot(slotIndex).toString().equals(currentRecipeKey) && !currentRecipeList.get(currentRecipeIdx).isSmeltingRecipe();
	}

	public boolean isMissingTools() {
		return isMissingTools;
	}

	// Server Only

	public void findAvailableRecipes() {
		availableRecipes.clear();
		sortedRecipes.clear();
		for(FoodRecipe foodRecipe : FoodRegistry.getFoodRecipes()) {
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
		currentRecipeList = currentRecipeKey != null ? availableRecipes.get(currentRecipeKey) : null;
		if(currentRecipeList == null || currentRecipeList.isEmpty()) {
			currentRecipeIdx = 0;
			currentRecipeList = null;
			currentRecipeKey = null;
			setCraftMatrix(null);
		} else if(currentRecipeIdx >= currentRecipeList.size()) {
			currentRecipeIdx = 0;
		}
		isDirty = true;
	}

	public void sortRecipes(Comparator<ItemStack> comparator) {
		currentSort = comparator;
		Collections.sort(sortedRecipes, comparator);
		updateRecipeList();
		isDirty = true;
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

	// Client Only

	public void setAvailableItems(List<ItemStack> sortedRecipes, ArrayListMultimap<String, FoodRecipe> availableRecipes, int currentRecipeIdx) {
		this.sortedRecipes.clear();
		this.sortedRecipes.addAll(sortedRecipes);
		this.availableRecipes.clear();
		this.availableRecipes.putAll(availableRecipes);
		updateRecipeList();
		this.currentRecipeIdx = currentRecipeIdx;
		markDirty(true);
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void markDirty(boolean dirty) {
		this.isDirty = dirty;
	}
}
