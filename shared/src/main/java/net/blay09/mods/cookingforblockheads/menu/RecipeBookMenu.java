package net.blay09.mods.cookingforblockheads.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.comparator.ComparatorName;
import net.blay09.mods.cookingforblockheads.menu.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.RecipeFakeSlot;
import net.blay09.mods.cookingforblockheads.network.message.CraftRecipeMessage;
import net.blay09.mods.cookingforblockheads.network.message.ItemListMessage;
import net.blay09.mods.cookingforblockheads.network.message.RecipesMessage;
import net.blay09.mods.cookingforblockheads.network.message.RequestRecipesMessage;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeType;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RecipeBookMenu extends AbstractContainerMenu {

    private final Player player;

    private final List<RecipeFakeSlot> recipeSlots = Lists.newArrayList();
    private final List<CraftMatrixFakeSlot> matrixSlots = Lists.newArrayList();

    private final InventoryCraftBook craftBook = new InventoryCraftBook(this);

    private boolean noFilter;
    private boolean allowCrafting;
    private KitchenMultiBlock multiBlock;
    private boolean isDirty = true;

    private ItemStack lastOutputItem = ItemStack.EMPTY;

    private final List<FoodRecipeWithStatus> itemList = Lists.newArrayList();
    private Comparator<FoodRecipeWithStatus> currentSorting = new ComparatorName();
    private final List<FoodRecipeWithStatus> filteredItems = Lists.newArrayList();

    private boolean slotWasClicked;
    private String currentSearch;
    private boolean isDirtyClient;
    private boolean hasOven;
    private int scrollOffset;
    private FoodRecipeWithStatus selectedRecipe;
    private List<FoodRecipeWithIngredients> selectedRecipeList;
    private int selectedRecipeIndex;

    private boolean isInNoFilterPreview;

    public RecipeBookMenu(MenuType<RecipeBookMenu> containerType, int windowId, Player player) {
        super(containerType, windowId);

        this.player = player;

        Container fakeInventory = new DefaultContainer(4*3+3*3);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                RecipeFakeSlot slot = new RecipeFakeSlot(fakeInventory, j + i * 3, 102 + j * 18, 11 + i * 18);
                recipeSlots.add(slot);
                addSlot(slot);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                CraftMatrixFakeSlot slot = new CraftMatrixFakeSlot(fakeInventory, j + i * 3, 24 + j * 18, 20 + i * 18);
                matrixSlots.add(slot);
                addSlot(slot);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(player.getInventory(), i, 8 + i * 18, 150));
        }
    }

    @Override
    public void clicked(int slotNumber, int dragType, ClickType clickType, Player player) {
        slotWasClicked = true;

        if (slotNumber >= 0 && slotNumber < slots.size()) {
            Slot slot = slots.get(slotNumber);
            if (player.level.isClientSide) {
                if (slot instanceof RecipeFakeSlot recipeSlot) {
                    if (selectedRecipe != null && recipeSlot.getRecipe() == selectedRecipe) {
                        if (allowCrafting && (clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP)) {
                            FoodRecipeWithIngredients recipe = getSelection();
                            if (recipe != null) {
                                NonNullList<ItemStack> craftMatrix = NonNullList.create();
                                if (recipe.getRecipeType() == FoodRecipeType.CRAFTING) {
                                    for (CraftMatrixFakeSlot matrixSlot : matrixSlots) {
                                        craftMatrix.add(matrixSlot.getItem());
                                    }
                                } else if (recipe.getRecipeType() == FoodRecipeType.SMELTING) {
                                    craftMatrix.add(matrixSlots.get(4).getItem());
                                }
                                Balm.getNetworking().sendToServer(new CraftRecipeMessage(recipe.getOutputItem(), recipe.getRecipeType(), craftMatrix, clickType == ClickType.QUICK_MOVE));
                            }
                        }
                    } else {
                        setSelectedRecipe(recipeSlot.getRecipe(), false);
                    }
                }
            }
        }

        super.clicked(slotNumber, dragType, clickType, player);
    }

    public void setSelectedRecipe(@Nullable FoodRecipeWithStatus recipe, boolean forceNoFilter) {
        selectedRecipe = recipe;

        if (selectedRecipe != null) {
            Balm.getNetworking().sendToServer(new RequestRecipesMessage(selectedRecipe.getOutputItem(), forceNoFilter));
        }

        this.isInNoFilterPreview = forceNoFilter;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (!player.level.isClientSide) {
            if (isDirty || slotWasClicked) {
                findAndSendItemList();
                if (!lastOutputItem.isEmpty()) {
                    findAndSendRecipes(lastOutputItem, isInNoFilterPreview);
                }
                slotWasClicked = false;
                isDirty = false;
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void slotsChanged(Container inventory) {
        // NOP, we don't want detectAndSendChanges called here, otherwise it will spam on crafting a stack of items
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotIndex >= 48 && slotIndex < 57) {
                if (!moveItemStackTo(slotStack, 21, 48, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 21 && slotIndex < 48) {
                if (!moveItemStackTo(slotStack, 48, 57, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    public RecipeBookMenu setNoFilter() {
        this.noFilter = true;
        return this;
    }

    public RecipeBookMenu allowCrafting() {
        this.allowCrafting = true;
        return this;
    }

    public RecipeBookMenu setKitchenMultiBlock(KitchenMultiBlock kitchenMultiBlock) {
        this.multiBlock = kitchenMultiBlock;
        return this;
    }

    public void findAndSendItemList() {
        Map<ResourceLocation, FoodRecipeWithStatus> statusMap = Maps.newHashMap();
        List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.getInventory());
        keyLoop:
        for (ResourceLocation key : CookingRegistry.getFoodRecipes().keySet()) {
            RecipeStatus bestStatus = null;
            for (FoodRecipe recipe : CookingRegistry.getFoodRecipes().get(key)) {
                RecipeStatus thisStatus = CookingRegistry.getRecipeStatus(recipe, inventories, multiBlock != null && multiBlock.hasSmeltingProvider());
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

        Balm.getNetworking().sendTo(player, new ItemListMessage(statusMap.values(), multiBlock != null && multiBlock.hasSmeltingProvider()));
    }

    public void findAndSendRecipes(ItemStack outputItem, boolean forceNoFilter) {
        lastOutputItem = outputItem;
        isInNoFilterPreview = forceNoFilter;

        List<FoodRecipeWithIngredients> resultList = Lists.newArrayList();
        List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.getInventory());
        outerLoop:
        for (FoodRecipe recipe : CookingRegistry.getFoodRecipes(outputItem)) {
            for (IKitchenItemProvider itemProvider : inventories) {
                itemProvider.resetSimulation();
            }

            List<FoodIngredient> ingredients = recipe.getCraftMatrix();
            List<NonNullList<ItemStack>> craftMatrix = Lists.newArrayListWithCapacity(ingredients.size());
            boolean requireBucket = CookingRegistry.doesItemRequireBucketForCrafting(recipe.getOutputItem());
            int availabilityMap = 0;
            for (int i = 0; i < ingredients.size(); i++) {
                FoodIngredient ingredient = ingredients.get(i);
                NonNullList<ItemStack> stackList = NonNullList.create();
                if (ingredient != null) {
                    List<SourceItem> sourceList = CookingRegistry.findSourceCandidates(ingredient, inventories, requireBucket, noFilter || forceNoFilter);
                    if (sourceList.isEmpty()) {
                        continue outerLoop;
                    }

                    if (sourceList.stream().anyMatch(it -> it.getSourceProvider() != null)) {
                        int origX = i % 3;
                        int origY = i / 3;
                        int targetIdx = origY * 3 + origX;
                        availabilityMap |= 1 << targetIdx;
                    }

                    for (SourceItem source : sourceList) {
                        ItemStack foundStack = source.getSourceStack();
                        stackList.add(foundStack.getCount() > 127 ? ContainerUtils.copyStackWithSize(foundStack, 127) : foundStack);
                    }
                }

                craftMatrix.add(stackList);
            }

            RecipeStatus recipeStatus = CookingRegistry.getRecipeStatus(recipe, inventories, multiBlock != null && multiBlock.hasSmeltingProvider());
            resultList.add(FoodRecipeWithIngredients.fromFoodRecipe(recipe, recipeStatus, craftMatrix, availabilityMap));
        }

        resultList.sort((o1, o2) -> o2.getRecipeStatus().ordinal() - o1.getRecipeStatus().ordinal());

        Balm.getNetworking().sendTo(player, new RecipesMessage(outputItem, resultList));
    }

    public void tryCraft(ItemStack outputItem, FoodRecipeType recipeType, NonNullList<ItemStack> craftMatrix, boolean stack) {
        if (outputItem.isEmpty() || craftMatrix.size() == 0) {
            return;
        }

        if (allowCrafting) {
            if (recipeType == FoodRecipeType.CRAFTING) {
                int craftCount = stack ? outputItem.getMaxStackSize() / outputItem.getCount() : 1;
                for (int i = 0; i < craftCount; i++) {
                    ItemStack itemStack = craftBook.tryCraft(outputItem, craftMatrix, player, multiBlock);
                    if (!itemStack.isEmpty()) {
                        if (!player.getInventory().add(itemStack)) {
                            player.drop(itemStack, false);
                        }
                    } else {
                        break;
                    }
                }
                isDirty = true;
                broadcastChanges();
            } else if (recipeType == FoodRecipeType.SMELTING) {
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

    public void setItemList(Collection<FoodRecipeWithStatus> recipeList) {
        this.itemList.clear();
        this.itemList.addAll(recipeList);

        int previousSelectionIndex = selectedRecipe != null ? filteredItems.indexOf(selectedRecipe) : -1;

        // Re-apply the search to populate filteredItems
        search(currentSearch);

        // Re-apply the sorting
        filteredItems.sort(currentSorting);

        // Make sure the recipe stays on the same slot, even if others moved
        if (selectedRecipe != null && previousSelectionIndex != -1) {
            Iterator<FoodRecipeWithStatus> it = filteredItems.iterator();
            FoodRecipeWithStatus found = null;
            while (it.hasNext()) {
                FoodRecipeWithStatus recipe = it.next();
                if (recipe.getOutputItem().sameItem(selectedRecipe.getOutputItem())) {
                    found = recipe;
                    it.remove();
                    break;
                }
            }
            while (previousSelectionIndex > filteredItems.size()) {
                filteredItems.add(null);
            }
            filteredItems.add(previousSelectionIndex, found);
            selectedRecipe = found;
        }

        // Updates the items inside the recipe slots
        populateRecipeSlots();

        setDirty(true);
    }

    public void populateRecipeSlots() {
        int i = scrollOffset * 3;
        for (RecipeFakeSlot slot : recipeSlots) {
            if (i < filteredItems.size()) {
                slot.setFoodRecipe(filteredItems.get(i));
                i++;
            } else {
                slot.setFoodRecipe(null);
            }
        }
    }

    private void populateMatrixSlots() {
        FoodRecipeWithIngredients recipe = selectedRecipeList != null ? selectedRecipeList.get(selectedRecipeIndex) : null;
        populateMatrixSlots(recipe);
    }

    @SuppressWarnings("unchecked")
    public void populateMatrixSlots(@Nullable FoodRecipeWithIngredients recipe) {
        if (recipe == null) {
            for (CraftMatrixFakeSlot matrixSlot : matrixSlots) {
                matrixSlot.setIngredient(null);
                matrixSlot.setAvailable(true);
            }
            return;
        }

        if (recipe.getRecipeType() == FoodRecipeType.SMELTING) {
            for (int i = 0; i < matrixSlots.size(); i++) {
                matrixSlots.get(i).setIngredient(i == 4 ? recipe.getCraftMatrix().get(0) : null);
                matrixSlots.get(i).setAvailable((recipe.getAvailabilityMap() & 1) == 1);
            }
        } else {
            NonNullList[] matrix = new NonNullList[9];
            for (int i = 0; i < recipe.getCraftMatrix().size(); i++) {
                int origX = i % 3;
                int origY = i / 3;
                int targetIdx = origY * 3 + origX;
                matrix[targetIdx] = recipe.getCraftMatrix().get(i);
            }

            for (int i = 0; i < matrix.length; i++) {
                matrixSlots.get(i).setIngredient(matrix[i]);
                matrixSlots.get(i).setAvailable((recipe.getAvailabilityMap() & (1 << i)) == (1 << i));
            }
        }
    }

    public void setSortComparator(Comparator<FoodRecipeWithStatus> comparator) {
        this.currentSorting = comparator;
        // When re-sorting, make sure to remove all null slots that were added to preserve layout
        filteredItems.removeIf(Objects::isNull);
        filteredItems.sort(comparator);
        populateRecipeSlots();
    }

    public int getItemListCount() {
        return filteredItems.size();
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        populateRecipeSlots();
    }

    public void search(@Nullable String term) {
        this.scrollOffset = 0;
        this.currentSearch = term;
        filteredItems.clear();
        if (term == null || term.trim().isEmpty()) {
            filteredItems.addAll(itemList);
        } else {
            for (FoodRecipeWithStatus recipe : itemList) {
                if (recipe.getOutputItem().getDisplayName().getString().toLowerCase(Locale.ENGLISH).contains(term.toLowerCase())) {
                    filteredItems.add(recipe);
                } else {
                    List<Component> tooltips = recipe.getOutputItem().getTooltipLines(player, TooltipFlag.Default.NORMAL);
                    for (Component tooltip : tooltips) {
                        if (tooltip.getString().toLowerCase(Locale.ENGLISH).contains(term.toLowerCase())) {
                            filteredItems.add(recipe);
                            break;
                        }
                    }
                }
            }
        }
        filteredItems.sort(currentSorting);
    }

    @Nullable
    public FoodRecipeWithIngredients getSelection() {
        return selectedRecipeList != null ? selectedRecipeList.get(selectedRecipeIndex) : null;
    }

    public boolean isSelectedSlot(RecipeFakeSlot slot) {
        return slot.getRecipe() == selectedRecipe;
    }

    public boolean isDirty() {
        return isDirtyClient;
    }

    public void setDirty(boolean dirty) {
        isDirtyClient = dirty;
    }

    public void setHasOven(boolean hasOven) {
        this.hasOven = hasOven;
    }

    public boolean hasOven() {
        return hasOven;
    }

    public void setRecipeList(ItemStack outputItem, List<FoodRecipeWithIngredients> recipeList) {
        selectedRecipeList = recipeList.size() > 0 ? recipeList : null;
        if (lastOutputItem.isEmpty() || lastOutputItem.getItem() != outputItem.getItem() || selectedRecipeList == null || selectedRecipeIndex >= selectedRecipeList.size()) {
            selectedRecipeIndex = 0;
        }

        populateMatrixSlots();
        lastOutputItem = outputItem;
    }

    public void nextSubRecipe(int i) {
        if (selectedRecipeList != null) {
            selectedRecipeIndex = Math.max(0, Math.min(selectedRecipeList.size() - 1, selectedRecipeIndex + i));
            populateMatrixSlots();
        }
    }

    public boolean hasVariants() {
        return selectedRecipeList != null && selectedRecipeList.size() > 1;
    }

    public void updateSlots(float partialTicks) {
        for (CraftMatrixFakeSlot slot : matrixSlots) {
            slot.updateSlot(partialTicks);
        }
    }

    public int getSelectionIndex() {
        return selectedRecipeIndex;
    }

    public int getRecipeCount() {
        return selectedRecipeList != null ? selectedRecipeList.size() : 0;
    }

    public List<CraftMatrixFakeSlot> getCraftingMatrixSlots() {
        return matrixSlots;
    }

    @Nullable
    public FoodRecipeWithStatus findAvailableRecipe(ItemStack itemStack) {
        return itemList.stream().filter(it -> ItemStack.isSame(it.getOutputItem(), itemStack)).findAny().orElse(null);
    }

    public int getSelectedRecipeIndex() {
        return filteredItems.indexOf(selectedRecipe);
    }
}
