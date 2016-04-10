package net.blay09.mods.cookingforblockheads.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.container.comparator.ComparatorName;
import net.blay09.mods.cookingforblockheads.container.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotCraftMatrix;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageCraftRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageRecipeList;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ContainerRecipeBook extends Container {

	private final EntityPlayer player;
	private final Map<Integer, FoodRecipeWithStatus> recipeMap = Maps.newHashMap();
	private final List<FoodRecipeWithStatus> sortedRecipes = Lists.newArrayList();
	private final List<FakeSlotRecipe> recipeSlots = Lists.newArrayList();
	private final List<FakeSlotCraftMatrix> matrixSlots = Lists.newArrayList();
	private final InventoryCraftBook craftBook = new InventoryCraftBook(this);

	private boolean noFilter;
	private boolean allowCrafting;
	private KitchenMultiBlock multiBlock;
	private boolean isDirty;

	private Comparator<FoodRecipeWithStatus> currentSorting = new ComparatorName();
	private String currentSearch;
	private int selectedRecipeId = -1;
	private int scrollOffset;
	private boolean hasOven;

	public ContainerRecipeBook(EntityPlayer player) {
		this.player = player;

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				FakeSlotRecipe slot = new FakeSlotRecipe(j + i * 3, 102 + j * 18, 11 + i * 18);
				recipeSlots.add(slot);
				addSlotToContainer(slot);
			}
		}

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				FakeSlotCraftMatrix slot = new FakeSlotCraftMatrix(j + i * 3, 24 + j * 18, 20 + i * 18);
				matrixSlots.add(slot);
				addSlotToContainer(slot);
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
	}

	@Override
	public ItemStack func_184996_a(int slotNumber, int dragType, ClickType clickType, EntityPlayer player) { // slotClick
		if(slotNumber >= 0 && slotNumber < inventorySlots.size()) {
			Slot slot = inventorySlots.get(slotNumber);
			if (slot instanceof FakeSlotRecipe && player.worldObj.isRemote) {
				FakeSlotRecipe slotRecipe = (FakeSlotRecipe) slot;
				if (slotRecipe.getRecipe() != null) {
					int recipeId = slotRecipe.getRecipe().getId();
					if (recipeId == selectedRecipeId) {
						if(allowCrafting) {
							NetworkHandler.instance.sendToServer(new MessageCraftRecipe(recipeId, clickType == ClickType.QUICK_MOVE));
						}
					} else {
						selectedRecipeId = recipeId;
						updateMatrixSlots();
					}
				}
			}
		}
		return super.func_184996_a(slotNumber, dragType, clickType, player);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if(player.worldObj.isRemote) {
			for (FakeSlotCraftMatrix slot : matrixSlots) {
				slot.updateSlot();
			}
		} else {
			if (player.inventory.inventoryChanged) {
				findAndSendRecipes();
				player.inventory.inventoryChanged = false;
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemStack = null;
		Slot slot = inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();
			if (slotIndex >= 48 && slotIndex < 57) {
				if (!mergeItemStack(slotStack, 21, 48, true)) {
					return null;
				}
			} else if (slotIndex >= 21 && slotIndex < 48) {
				if(!mergeItemStack(slotStack, 48, 57, false)) {
					return null;
				}
			}

			if (slotStack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemStack;
	}

	public ContainerRecipeBook setNoFilter() {
		this.noFilter = true;
		return this;
	}

	public ContainerRecipeBook allowCrafting() {
		this.allowCrafting = true;
		return this;
	}

	public ContainerRecipeBook setKitchenMultiBlock(KitchenMultiBlock kitchenMultiBlock) {
		this.multiBlock = kitchenMultiBlock;
		return this;
	}

	public ContainerRecipeBook findAndSendRecipes() {
		recipeMap.clear();
		if(noFilter) {
			for(FoodRecipe recipe : CookingRegistry.getFoodRecipes()) {
				recipeMap.put(recipe.getId(), new FoodRecipeWithStatus(recipe.getId(), recipe.getOutputItem(), recipe.getCraftMatrix(), recipe.getType(), CookingRegistry.getRecipeStatus(recipe, player.inventory, multiBlock)));
			}
		} else {
			for(FoodRecipeWithStatus recipe : CookingRegistry.findAvailableRecipes(player.inventory, multiBlock)) {
				recipeMap.put(recipe.getId(), recipe);
			}
		}
		NetworkHandler.instance.sendTo(new MessageRecipeList(recipeMap.values(), multiBlock != null && multiBlock.hasSmeltingProvider()), (EntityPlayerMP) player);
		return this;
	}

	public void tryCraft(int id, boolean stack) {
		if(allowCrafting) {
			FoodRecipeWithStatus recipe = recipeMap.get(id);
			if(recipe != null) {
				if(recipe.getType() == RecipeType.CRAFTING) {
					int craftCount = stack ? recipe.getOutputItem().getMaxStackSize() : 1;
					for (int i = 0; i < craftCount; i++) {
						ItemStack itemStack = craftBook.tryCraft(recipe, player, multiBlock);
						if (itemStack != null) {
							if (!player.inventory.addItemStackToInventory(itemStack)) {
								player.dropPlayerItemWithRandomChoice(itemStack, false);
							}
						} else {
							break;
						}
					}
					findAndSendRecipes();
				} else if(recipe.getType() == RecipeType.SMELTING) {
					if(multiBlock != null && multiBlock.hasSmeltingProvider()) {
						multiBlock.trySmelt(player, recipe, stack);
						findAndSendRecipes();
					}
				}
			}
		}
	}

	public void setRecipeList(Collection<FoodRecipeWithStatus> recipeList) {
		recipeMap.clear();
		for(FoodRecipeWithStatus recipe : recipeList) {
			recipeMap.put(recipe.getId(), recipe);
		}
		if(selectedRecipeId != -1 && !recipeMap.containsKey(selectedRecipeId)) {
			selectedRecipeId = -1;
			updateMatrixSlots();
		}
		search(currentSearch);
		Collections.sort(sortedRecipes, currentSorting);
		updateRecipeSlots();
		isDirty = true;
	}

	public void updateRecipeSlots() {
		int i = scrollOffset * 3;
		for(FakeSlotRecipe slot : recipeSlots) {
			if(i < sortedRecipes.size()) {
				slot.setFoodRecipe(sortedRecipes.get(i));
				i++;
			} else {
				slot.setFoodRecipe(null);
			}
		}
	}

	public void updateMatrixSlots() {
		FoodRecipeWithStatus recipe = selectedRecipeId != -1  ? recipeMap.get(selectedRecipeId) : null;
		if(recipe != null && recipe.getType() == RecipeType.SMELTING) {
			for (int i = 0; i < matrixSlots.size(); i++) {
				matrixSlots.get(i).setIngredient(i == 4 ? recipe.getCraftMatrix().get(0) : null);
			}
		} else {
			int i = 0;
			if(recipe != null && recipe.getCraftMatrix().size() == 1) {
				for (int j = 0; j < matrixSlots.size(); j++) {
					matrixSlots.get(j).setIngredient(j == 4 ? recipe.getCraftMatrix().get(0) : null);
				}
			} else if(recipe != null && recipe.getCraftMatrix().size() == 3) {
				for (int j = 0; j < matrixSlots.size(); j++) {
					if(j >= 3 && j <= 5) {
						matrixSlots.get(j).setIngredient(recipe.getCraftMatrix().get(j - 3));
					} else {
						matrixSlots.get(j).setIngredient(null);
					}
				}
			} else {
				for (FakeSlotCraftMatrix slot : matrixSlots) {
					if (recipe != null && i < recipe.getCraftMatrix().size()) {
						slot.setIngredient(recipe.getCraftMatrix().get(i));
						i++;
					} else {
						slot.setIngredient(null);
					}
				}
			}
		}
	}

	public void setSortComparator(Comparator<FoodRecipeWithStatus> comparator) {
		this.currentSorting = comparator;
		Collections.sort(sortedRecipes, comparator);
		updateRecipeSlots();
	}

	public int getRecipeCount() {
		return sortedRecipes.size();
	}

	public void setScrollOffset(int scrollOffset) {
		this.scrollOffset = scrollOffset;
		updateRecipeSlots();
	}

	public void search(String term) {
		this.currentSearch = term;
		sortedRecipes.clear();
		if(term == null || term.trim().isEmpty()) {
			sortedRecipes.addAll(recipeMap.values());
		} else {
			for(FoodRecipeWithStatus recipe : recipeMap.values()) {
				if(recipe.getOutputItem().getDisplayName().toLowerCase().contains(term.toLowerCase())) {
					sortedRecipes.add(recipe);
				}
			}
		}
		Collections.sort(sortedRecipes, currentSorting);
		updateRecipeSlots();
	}

	public boolean hasSelection() {
		return selectedRecipeId != -1 && recipeMap.containsKey(selectedRecipeId);
	}

	public FoodRecipeWithStatus getSelection() {
		return selectedRecipeId != -1 ? recipeMap.get(selectedRecipeId) : null;
	}

	public boolean isSelectedSlot(FakeSlotRecipe slot) {
		return slot.getRecipe().getId() == selectedRecipeId;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
	}

	public boolean isAllowCrafting() {
		return allowCrafting;
	}

	public void setHasOven(boolean hasOven) {
		this.hasOven = hasOven;
	}

	public boolean hasOven() {
		return hasOven;
	}

}
