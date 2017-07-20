package net.blay09.mods.cookingforblockheads.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.container.comparator.ComparatorName;
import net.blay09.mods.cookingforblockheads.container.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotCraftMatrix;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageCraftRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageItemList;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageRecipes;
import net.blay09.mods.cookingforblockheads.network.message.MessageRequestRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import java.util.*;

public class ContainerRecipeBook extends Container {

	private final EntityPlayer player;

	private final List<FakeSlotRecipe> recipeSlots = Lists.newArrayList();
	private final List<FakeSlotCraftMatrix> matrixSlots = Lists.newArrayList();

	private final InventoryCraftBook craftBook = new InventoryCraftBook(this);

	private boolean noFilter;
	private boolean allowCrafting;
	private KitchenMultiBlock multiBlock;
	private boolean isDirty = true;

	private ItemStack lastOutputItem = ItemStack.EMPTY;

	// These are client-only too, but I don't want a separate client-only constructor and it crashes if initialized here while SideOnly(Side.CLIENT)
	private final List<FoodRecipeWithStatus> itemList = Lists.newArrayList();
	private Comparator<FoodRecipeWithStatus> currentSorting = new ComparatorName();
	private final List<FoodRecipeWithStatus> filteredItems = Lists.newArrayList();

	@SideOnly(Side.CLIENT)
	private String currentSearch;

	@SideOnly(Side.CLIENT)
	private boolean isDirtyClient;

	@SideOnly(Side.CLIENT)
	private boolean hasOven;

	@SideOnly(Side.CLIENT)
	private int scrollOffset;

	@SideOnly(Side.CLIENT)
	private FakeSlotRecipe selectedRecipe;

	@SideOnly(Side.CLIENT)
	private List<FoodRecipeWithIngredients> selectedRecipeList;

	@SideOnly(Side.CLIENT)
	private int selectedRecipeIndex;

	public ContainerRecipeBook(EntityPlayer player) {
		this.player = player;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				FakeSlotRecipe slot = new FakeSlotRecipe(j + i * 3, 102 + j * 18, 11 + i * 18);
				recipeSlots.add(slot);
				addSlotToContainer(slot);
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				FakeSlotCraftMatrix slot = new FakeSlotCraftMatrix(j + i * 3, 24 + j * 18, 20 + i * 18);
				matrixSlots.add(slot);
				addSlotToContainer(slot);
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 150));
		}
	}

	@Override
	public ItemStack slotClick(int slotNumber, int dragType, ClickType clickType, EntityPlayer player) {
		if (slotNumber >= 0 && slotNumber < inventorySlots.size()) {
			Slot slot = inventorySlots.get(slotNumber);
			if (player.world.isRemote) {
				if (slot instanceof FakeSlotRecipe) {
					FakeSlotRecipe slotRecipe = (FakeSlotRecipe) slot;
					if (slotRecipe.getRecipe() != null) {
						if (slotRecipe == selectedRecipe) {
							if (allowCrafting && (clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP)) {
								FoodRecipeWithIngredients recipe = getSelection();
								if (recipe != null) {
									NonNullList<ItemStack> craftMatrix = NonNullList.create();
									if (recipe.getRecipeType() == RecipeType.CRAFTING) {
										for (FakeSlotCraftMatrix matrixSlot : matrixSlots) {
											craftMatrix.add(matrixSlot.getStack());
										}
									} else if (recipe.getRecipeType() == RecipeType.SMELTING) {
										craftMatrix.add(matrixSlots.get(4).getStack());
									}
									NetworkHandler.instance.sendToServer(new MessageCraftRecipe(recipe.getOutputItem(), recipe.getRecipeType(), craftMatrix, clickType == ClickType.QUICK_MOVE));
								}
							}
						} else {
							selectedRecipe = slotRecipe;
							if(selectedRecipe.getRecipe() != null) {
								NetworkHandler.instance.sendToServer(new MessageRequestRecipes(selectedRecipe.getRecipe().getOutputItem()));
							}
						}
					}
				} else if (slot instanceof FakeSlotCraftMatrix) {
					((FakeSlotCraftMatrix) slot).setLocked(!((FakeSlotCraftMatrix) slot).isLocked());
				}
			}
		}
		return super.slotClick(slotNumber, dragType, clickType, player);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (!player.world.isRemote) {
			if (isDirty || player.inventory.inventoryChanged) {
				findAndSendItemList();
				if (!lastOutputItem.isEmpty()) {
					findAndSendRecipes(lastOutputItem);
				}
				player.inventory.inventoryChanged = false;
				isDirty = false;
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		// NOP, we don't want detectAndSendChanges called here, otherwise it will spam on crafting a stack of items
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();
			if (slotIndex >= 48 && slotIndex < 57) {
				if (!mergeItemStack(slotStack, 21, 48, true)) {
					return ItemStack.EMPTY;
				}
			} else if (slotIndex >= 21 && slotIndex < 48) {
				if (!mergeItemStack(slotStack, 48, 57, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
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

	public void findAndSendItemList() {
		Map<CookingRegistry.ItemID, FoodRecipeWithStatus> statusMap = Maps.newHashMap();
		List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
		keyLoop:
		for (CookingRegistry.ItemID key : CookingRegistry.getFoodRecipes().keySet()) {
			RecipeStatus bestStatus = null;
			for (FoodRecipe recipe : CookingRegistry.getFoodRecipes(key)) {
				RecipeStatus thisStatus = CookingRegistry.getRecipeStatus(recipe, inventories);
				if (noFilter || thisStatus != RecipeStatus.MISSING_INGREDIENTS) {
					if (bestStatus == null || thisStatus.ordinal() > bestStatus.ordinal()) {
						statusMap.put(key, new FoodRecipeWithStatus(recipe.getOutputItem(), thisStatus));
						bestStatus = thisStatus;
					}
				}
				if (bestStatus == RecipeStatus.AVAILABLE) {
					// If we already marked this as available it can't get any better; so go to the next item
					continue keyLoop;
				}
			}
		}
		NetworkHandler.instance.sendTo(new MessageItemList(statusMap.values(), multiBlock != null && multiBlock.hasSmeltingProvider()), (EntityPlayerMP) player);
	}

	public void findAndSendRecipes(ItemStack outputItem) {
		lastOutputItem = outputItem;
		List<FoodRecipeWithIngredients> resultList = Lists.newArrayList();
		List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
		outerLoop:for (FoodRecipe recipe : CookingRegistry.getFoodRecipes(outputItem)) {
			for (IKitchenItemProvider itemProvider : inventories) {
				itemProvider.resetSimulation();
			}
			List<FoodIngredient> ingredients = recipe.getCraftMatrix();
			List<NonNullList<ItemStack>> craftMatrix = Lists.newArrayListWithCapacity(ingredients.size());
			boolean requireBucket = CookingRegistry.doesItemRequireBucketForCrafting(recipe.getOutputItem());
			for (FoodIngredient ingredient : ingredients) {
				NonNullList<ItemStack> stackList = NonNullList.create();
				if (ingredient != null) {
					for (ItemStack checkStack : ingredient.getItemStacks()) {
						ItemStack foundStack = CookingRegistry.findAnyItemStack(checkStack, inventories, requireBucket);
						if (foundStack.isEmpty()) {
							if (noFilter || ingredient.isToolItem()) {
								foundStack = !ingredient.getItemStacks().isEmpty() ? ingredient.getItemStacks().get(0) : ItemStack.EMPTY;
							}
						}
						if (!foundStack.isEmpty()) {
							stackList.add(foundStack);
						}
					}
					if(stackList.isEmpty()) {
						continue outerLoop;
					}
				}
				craftMatrix.add(stackList);
			}
			resultList.add(new FoodRecipeWithIngredients(recipe.getOutputItem(), recipe.getType(), recipe.getRecipeWidth(), craftMatrix));
		}
		NetworkHandler.instance.sendTo(new MessageRecipes(outputItem, resultList), (EntityPlayerMP) player);
	}

	public void tryCraft(ItemStack outputItem, RecipeType recipeType, NonNullList<ItemStack> craftMatrix, boolean stack) {
		if (outputItem.isEmpty() || craftMatrix.size() == 0) {
			return;
		}
		if (allowCrafting) {
			if (recipeType == RecipeType.CRAFTING) {
				int craftCount = stack ? outputItem.getMaxStackSize() : 1;
				for (int i = 0; i < craftCount; i++) {
					ItemStack itemStack = craftBook.tryCraft(outputItem, craftMatrix, player, multiBlock);
					if (!itemStack.isEmpty()) {
						if (!player.inventory.addItemStackToInventory(itemStack)) {
							player.dropItem(itemStack, false);
						}
					} else {
						break;
					}
				}
				isDirty = true;
				detectAndSendChanges();
			} else if (recipeType == RecipeType.SMELTING) {
				if (multiBlock != null && multiBlock.hasSmeltingProvider()) {
					multiBlock.trySmelt(outputItem, craftMatrix.get(0), player, stack);
					isDirty = true;
				}
			}
		}
	}

	public boolean isAllowCrafting() {
		return allowCrafting;
	}

	@SideOnly(Side.CLIENT)
	public void setItemList(Collection<FoodRecipeWithStatus> recipeList) {
		this.itemList.clear();
		this.itemList.addAll(recipeList);

		// Re-apply the search to populate filteredItems
		search(currentSearch);

		// Re-apply the sorting
		Collections.sort(filteredItems, currentSorting);

		// Make sure the recipe stays on the same slot, even if others moved
		if (selectedRecipe != null && selectedRecipe.getRecipe() != null) {
			Iterator<FoodRecipeWithStatus> it = filteredItems.iterator();
			FoodRecipeWithStatus found = null;
			while (it.hasNext()) {
				FoodRecipeWithStatus recipe = it.next();
				if (recipe.getOutputItem().getItem() == selectedRecipe.getRecipe().getOutputItem().getItem()) {
					found = recipe;
					it.remove();
				}
			}
			int index = scrollOffset + selectedRecipe.getSlotIndex();
			while (index > filteredItems.size()) {
				filteredItems.add(null);
			}
			filteredItems.add(index, found);
		}

		// Updates the items inside the recipe slots
		populateRecipeSlots();

		setDirty(true);
	}

	@SideOnly(Side.CLIENT)
	public void populateRecipeSlots() {
		int i = scrollOffset * 3;
		for (FakeSlotRecipe slot : recipeSlots) {
			if (i < filteredItems.size()) {
				slot.setFoodRecipe(filteredItems.get(i));
				i++;
			} else {
				slot.setFoodRecipe(null);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void populateMatrixSlots() {
		if (selectedRecipeList == null) {
			for (FakeSlotCraftMatrix matrixSlot : matrixSlots) {
				matrixSlot.setIngredient(null);
			}
			return;
		}
		FoodRecipeWithIngredients recipe = selectedRecipeList.get(selectedRecipeIndex);
		if (recipe.getRecipeType() == RecipeType.SMELTING) {
			for (int i = 0; i < matrixSlots.size(); i++) {
				matrixSlots.get(i).setIngredient(i == 4 ? recipe.getCraftMatrix().get(0) : null);
			}
		} else {
			for (FakeSlotCraftMatrix matrixSlot : matrixSlots) {
				matrixSlot.setIngredient(null);
			}
			for (int i = 0; i < recipe.getCraftMatrix().size(); i++) {
				int origX = i % recipe.getRecipeWidth();
				int origY = i / recipe.getRecipeWidth();
				int targetIdx = origY * 3 + origX;
				matrixSlots.get(targetIdx).setIngredient(recipe.getCraftMatrix().get(i));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void setSortComparator(Comparator<FoodRecipeWithStatus> comparator) {
		this.currentSorting = comparator;
		// When re-sorting, make sure to remove all null slots that were added to preserve layout
		Iterator<FoodRecipeWithStatus> it = filteredItems.iterator();
		while (it.hasNext()) {
			if (it.next() == null) {
				it.remove();
			}
		}
		Collections.sort(filteredItems, comparator);
		populateRecipeSlots();
	}

	@SideOnly(Side.CLIENT)
	public int getItemListCount() {
		return filteredItems.size();
	}

	@SideOnly(Side.CLIENT)
	public void setScrollOffset(int scrollOffset) {
		this.scrollOffset = scrollOffset;
		populateRecipeSlots();
	}

	@SideOnly(Side.CLIENT)
	public void search(@Nullable String term) {
		this.scrollOffset = 0;
		this.currentSearch = term;
		filteredItems.clear();
		if (term == null || term.trim().isEmpty()) {
			filteredItems.addAll(itemList);
		} else {
			for (FoodRecipeWithStatus recipe : itemList) {
				if (recipe.getOutputItem().getDisplayName().toLowerCase().contains(term.toLowerCase())) {
					filteredItems.add(recipe);
				}
			}
		}
		Collections.sort(filteredItems, currentSorting);
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public FoodRecipeWithIngredients getSelection() {
		return selectedRecipeList != null ? selectedRecipeList.get(selectedRecipeIndex) : null;
	}

	@SideOnly(Side.CLIENT)
	public boolean isSelectedSlot(FakeSlotRecipe slot) {
		return slot == selectedRecipe;
	}

	@SideOnly(Side.CLIENT)
	public boolean isDirty() {
		return isDirtyClient;
	}

	@SideOnly(Side.CLIENT)
	public void setDirty(boolean dirty) {
		isDirtyClient = dirty;
	}

	@SideOnly(Side.CLIENT)
	public void setHasOven(boolean hasOven) {
		this.hasOven = hasOven;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasOven() {
		return hasOven;
	}

	@SideOnly(Side.CLIENT)
	public void setRecipeList(ItemStack outputItem, List<FoodRecipeWithIngredients> recipeList) {
		selectedRecipeList = recipeList.size() > 0 ? recipeList : null;
		if (lastOutputItem.isEmpty() || lastOutputItem.getItem() != outputItem.getItem() || selectedRecipeList == null || selectedRecipeIndex >= selectedRecipeList.size()) {
			selectedRecipeIndex = 0;
		}
		populateMatrixSlots();
		lastOutputItem = outputItem;
	}

	@SideOnly(Side.CLIENT)
	public void nextSubRecipe(int i) {
		if (selectedRecipeList != null) {
			selectedRecipeIndex = Math.max(0, Math.min(selectedRecipeList.size() - 1, selectedRecipeIndex + i));
			populateMatrixSlots();
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean hasVariants() {
		return selectedRecipeList != null && selectedRecipeList.size() > 1;
	}

	@SideOnly(Side.CLIENT)
	public void updateSlots(float partialTicks) {
		for (FakeSlotCraftMatrix slot : matrixSlots) {
			slot.updateSlot(partialTicks);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getSelectionIndex() {
		return selectedRecipeIndex;
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeCount() {
		return selectedRecipeList != null ? selectedRecipeList.size() : 0;
	}

	public List<FakeSlotCraftMatrix> getCraftingMatrixSlots() {
		return matrixSlots;
	}
}
