package net.blay09.mods.cookingforblockheads.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.container.comparator.ComparatorName;
import net.blay09.mods.cookingforblockheads.container.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotCraftMatrix;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageItemList;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageRecipes;
import net.blay09.mods.cookingforblockheads.network.message.MessageRequestRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.registry.RecipeStatus;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class ContainerRecipeBook extends Container {

	private final EntityPlayer player;

	private final List<FoodRecipeWithStatus> itemList = Lists.newArrayList();

	private final List<FakeSlotRecipe> recipeSlots = Lists.newArrayList();
	private final List<FakeSlotCraftMatrix> matrixSlots = Lists.newArrayList();

	private final InventoryCraftBook craftBook = new InventoryCraftBook(this);

	private boolean noFilter;
	private boolean allowCrafting;
	private KitchenMultiBlock multiBlock;
	private boolean isDirty = true;

	@SideOnly(Side.CLIENT)
	private Comparator<FoodRecipeWithStatus> currentSorting = new ComparatorName();

	@SideOnly(Side.CLIENT)
	private String currentSearch;

	@SideOnly(Side.CLIENT)
	private final List<FoodRecipeWithStatus> filteredItems = Lists.newArrayList();

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
	public ItemStack slotClick(int slotNumber, int dragType, ClickType clickType, EntityPlayer player) {
		if(slotNumber >= 0 && slotNumber < inventorySlots.size()) {
			Slot slot = inventorySlots.get(slotNumber);
			if(player.worldObj.isRemote) {
				if (slot instanceof FakeSlotRecipe) {
					FakeSlotRecipe slotRecipe = (FakeSlotRecipe) slot;
					if (slotRecipe.getRecipe() != null) {
						if (slotRecipe == selectedRecipe) {
							if (allowCrafting) {
								//NetworkHandler.instance.sendToServer(new MessageCraftRecipe(null, null, null, clickType == ClickType.QUICK_MOVE)); // TODO FIX ME
							}
						} else {
							selectedRecipe = slotRecipe;
							NetworkHandler.instance.sendToServer(new MessageRequestRecipes(selectedRecipe.getRecipe().getOutputItem()));
						}
					}
				} else if(slot instanceof FakeSlotCraftMatrix) {
					((FakeSlotCraftMatrix) slot).setLocked(!((FakeSlotCraftMatrix) slot).isLocked());
				}
			}
		}
		return super.slotClick(slotNumber, dragType, clickType, player);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if(!player.worldObj.isRemote) {
			if (isDirty || player.inventory.inventoryChanged) {
				findAndSendItemList();
				player.inventory.inventoryChanged = false;
				isDirty = false;
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	} // TODO range check?

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemStack = null;
		Slot slot = inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			//noinspection ConstantConditions
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

	// TODO possible optimization: cache FRWI while grabbing the FRWS already, then we don't need to simulate the same thing twice

	public void findAndSendItemList() {
		itemList.clear();
		Map<ResourceLocation, FoodRecipeWithStatus> statusMap = Maps.newHashMap();
		List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
		keyLoop:for(ResourceLocation key : CookingRegistry.getFoodRecipes().keySet()) {
			RecipeStatus bestStatus = null;
			for(FoodRecipe recipe : CookingRegistry.getFoodRecipes().get(key)) {
				RecipeStatus thisStatus = CookingRegistry.getRecipeStatus(recipe, inventories);
				if(noFilter || thisStatus != RecipeStatus.MISSING_INGREDIENTS) {
					if (bestStatus == null || thisStatus.ordinal() > bestStatus.ordinal()) {
						statusMap.put(key, new FoodRecipeWithStatus(recipe.getOutputItem(), thisStatus));
						bestStatus = thisStatus;
					}
				}
				if(bestStatus == RecipeStatus.AVAILABLE) {
					// If we already marked this as available it can't get any better; so go to the next item
					continue keyLoop;
				}
			}
		}
		itemList.addAll(statusMap.values());
		NetworkHandler.instance.sendTo(new MessageItemList(itemList, multiBlock != null && multiBlock.hasSmeltingProvider()), (EntityPlayerMP) player);
	}

	public void findAndSendRecipes(ItemStack outputItem) {
		selectedRecipeList = Lists.newArrayList();
		List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
		for(FoodRecipe recipe : CookingRegistry.getFoodRecipes(outputItem)) {
			RecipeStatus status = CookingRegistry.getRecipeStatus(recipe, inventories);
			if(noFilter || status != RecipeStatus.MISSING_INGREDIENTS) { // TODO this is probably unnecessary because we redo the simulation right after below...
				for (IKitchenItemProvider itemProvider : inventories) {
					itemProvider.resetSimulation();
				}
				List<FoodIngredient> ingredients = recipe.getCraftMatrix();
				List<List<ItemStack>> craftMatrix = Lists.newArrayListWithCapacity(ingredients.size());
				for (FoodIngredient ingredient : ingredients) {
					List<ItemStack> stackList = Lists.newArrayList();
					for (ItemStack checkStack : ingredient.getItemStacks()) {
						ItemStack foundStack = CookingRegistry.findItemStack(checkStack, inventories);
						if (foundStack == null) {
							if (noFilter || ingredient.isToolItem()) {
								foundStack = ingredient.getItemStacks().length > 0 ? ingredient.getItemStacks()[0] : null;
							}
						}
						if (foundStack != null) {
							stackList.add(foundStack);
						}
					}
					craftMatrix.add(stackList);
				}
				selectedRecipeList.add(new FoodRecipeWithIngredients(recipe.getOutputItem(), recipe.getType(), recipe.getRecipeWidth(), craftMatrix));
			}
		}
		NetworkHandler.instance.sendTo(new MessageRecipes(outputItem, selectedRecipeList), (EntityPlayerMP) player);
	}

	public void tryCraft(@Nullable ItemStack outputItem, RecipeType recipeType, List<ItemStack> craftMatrix, boolean stack) {
		if(outputItem == null || craftMatrix.size() == 0) {
			return;
		}
		if(allowCrafting) {
			if(recipeType == RecipeType.CRAFTING) {
				int craftCount = stack ? outputItem.getMaxStackSize() : 1;
				for (int i = 0; i < craftCount; i++) {
					ItemStack itemStack = craftBook.tryCraft(outputItem, craftMatrix, player, multiBlock);
					if (itemStack != null) {
						if (!player.inventory.addItemStackToInventory(itemStack)) {
							player.dropItem(itemStack, false);
						}
					} else {
						break;
					}
				}
				isDirty = true;
				detectAndSendChanges();
			} else if(recipeType == RecipeType.SMELTING) {
				if(multiBlock != null && multiBlock.hasSmeltingProvider()) {
					multiBlock.trySmelt(outputItem, craftMatrix.get(0), player, stack);
					isDirty = true;
				}
				// TODO detectAndSendChanges()?
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
		if(selectedRecipe != null) {
			Iterator<FoodRecipeWithStatus> it = filteredItems.iterator();
			FoodRecipeWithStatus found = null;
			while(it.hasNext()) {
				FoodRecipeWithStatus recipe = it.next();
				if(recipe.getOutputItem() == selectedRecipe.getRecipe().getOutputItem()) {
					found = recipe;
					it.remove();
				}
			}
			int index = scrollOffset + selectedRecipe.getSlotIndex();
			while(index > filteredItems.size()) {
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
		for(FakeSlotRecipe slot : recipeSlots) {
			if(i < filteredItems.size()) {
				slot.setFoodRecipe(filteredItems.get(i));
				i++;
			} else {
				slot.setFoodRecipe(null);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void populateMatrixSlots() {
		FoodRecipeWithIngredients recipe = selectedRecipeList.get(selectedRecipeIndex);
		if(recipe.getRecipeType() == RecipeType.SMELTING) {
			for (int i = 0; i < matrixSlots.size(); i++) {
				matrixSlots.get(i).setIngredient(i == 4 ? recipe.getCraftMatrix().get(0) : null);
			}
		} else {
			int i = 0;
			if(recipe.getCraftMatrix().size() == 1) {
				for (int j = 0; j < matrixSlots.size(); j++) {
					matrixSlots.get(j).setIngredient(j == 4 ? recipe.getCraftMatrix().get(0) : null);
				}
			} else if(recipe.getCraftMatrix().size() == 3) {
				for (int j = 0; j < matrixSlots.size(); j++) {
					if(j >= 3 && j <= 5) {
						matrixSlots.get(j).setIngredient(recipe.getCraftMatrix().get(j - 3));
					} else {
						matrixSlots.get(j).setIngredient(null);
					}
				}
			} else {
				for (FakeSlotCraftMatrix slot : matrixSlots) {
					if (i < recipe.getCraftMatrix().size()) {
						slot.setIngredient(recipe.getCraftMatrix().get(i));
						i++;
					} else {
						slot.setIngredient(null);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void setSortComparator(Comparator<FoodRecipeWithStatus> comparator) {
		this.currentSorting = comparator;
		Collections.sort(filteredItems, comparator);
		populateRecipeSlots();
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeCount() {
		return filteredItems.size();
	}

	@SideOnly(Side.CLIENT)
	public void setScrollOffset(int scrollOffset) {
		this.scrollOffset = scrollOffset;
		populateRecipeSlots();
	}

	@SideOnly(Side.CLIENT)
	public void search(@Nullable String term) {
		this.currentSearch = term;
		filteredItems.clear();
		if(term == null || term.trim().isEmpty()) {
			filteredItems.addAll(itemList);
		} else {
			for(FoodRecipeWithStatus recipe : itemList) {
				if(recipe.getOutputItem().getDisplayName().toLowerCase().contains(term.toLowerCase())) {
					filteredItems.add(recipe);
				}
			}
		}
		Collections.sort(filteredItems, currentSorting);
		populateRecipeSlots();
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
		// TODO check output item I guess
		selectedRecipeList = recipeList;
		selectedRecipeIndex = 0;
		populateMatrixSlots();
	}

	@SideOnly(Side.CLIENT)
	public void nextSubRecipe(int i) {
		if(selectedRecipeList != null) {
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
		for(FakeSlotCraftMatrix slot : matrixSlots) {
			slot.updateSlot(partialTicks);
		}
	}
}
