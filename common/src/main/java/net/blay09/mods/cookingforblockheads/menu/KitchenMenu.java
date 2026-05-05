package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.blay09.mods.cookingforblockheads.crafting.CraftingOperation;
import net.blay09.mods.cookingforblockheads.crafting.KitchenImpl;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.blay09.mods.cookingforblockheads.menu.comparator.ComparatorName;
import net.blay09.mods.cookingforblockheads.menu.comparator.FavoriteComparator;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftableListingFakeSlot;
import net.blay09.mods.cookingforblockheads.network.message.*;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KitchenMenu extends AbstractContainerMenu {

    private final Player player;
    private final KitchenImpl kitchen;

    private final List<CraftableListingFakeSlot> recipeListingSlots = new ArrayList<>();
    private final List<CraftMatrixFakeSlot> matrixSlots = new ArrayList<>();

    private @Nullable List<ItemStack> lockedInputs;

    private final List<RecipeWithStatus> filteredCraftables = new ArrayList<>();
    private final List<RecipeWithStatus> history = new ArrayList<>();

    private String currentSearch;
    private Comparator<RecipeWithStatus> currentSorting = new FavoriteComparator(new ComparatorName());

    private List<RecipeWithStatus> craftables = new ArrayList<>();

    private boolean craftablesDirty = true;
    private boolean recipesDirty = true;
    private boolean scrollOffsetDirty;
    private int scrollOffset;

    private RecipeWithStatus selectedCraftable;
    private List<RecipeWithStatus> recipesForSelection;
    private boolean selectionRecipesPending;
    private int recipesForSelectionIndex;
    // I hate this code, I can't wait for 1.21.1 to be stable and die
    private ResourceLocation serversideSelectedRecipe;

    public KitchenMenu(MenuType<KitchenMenu> containerType, int windowId, Player player, KitchenImpl kitchen) {
        super(containerType, windowId);

        this.player = player;
        this.kitchen = kitchen;

        final var fakeInventory = new DefaultContainer(4 * 3 + 3 * 3);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                final var slot = new CraftableListingFakeSlot(fakeInventory, j + i * 3, 102 + j * 18, 11 + i * 18);
                recipeListingSlots.add(slot);
                addSlot(slot);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final var slot = new CraftMatrixFakeSlot(fakeInventory, j + i * 3, 24 + j * 18, 20 + i * 18);
                matrixSlots.add(slot);
                addSlot(slot);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player.getInventory(), j + i * 9 + 9, 8 + j * 18, 92 + i * 18) {
                    @Override
                    public void setChanged() {
                        craftablesDirty = true;
                        recipesDirty = true;
                    }
                });
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(player.getInventory(), i, 8 + i * 18, 150) {
                @Override
                public void setChanged() {
                    craftablesDirty = true;
                    recipesDirty = true;
                }
            });
        }
    }

    @Override
    public void clicked(int slotNumber, int dragType, ClickType clickType, Player player) {
        var handled = false;
        if (slotNumber >= 0 && slotNumber < slots.size()) {
            Slot slot = slots.get(slotNumber);
            if (slot instanceof CraftableListingFakeSlot craftableSlot) {
                if (player.level().isClientSide) {
                    if (isSelectedSlot(craftableSlot)) {
                        if (clickType == ClickType.PICKUP || clickType == ClickType.PICKUP_ALL || clickType == ClickType.QUICK_MOVE || clickType == ClickType.CLONE) {
                            requestCraft(clickType == ClickType.QUICK_MOVE, clickType == ClickType.CLONE);
                            handled = true;
                        }
                    } else {
                        clearHistory();
                        selectCraftable(craftableSlot.getCraftable());
                        handled = true;
                    }
                }
            }
        }

        if (!handled) {
            super.clicked(slotNumber, dragType, clickType, player);
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (craftablesDirty) {
            broadcastAvailableRecipes();
            craftablesDirty = false;
        }

        if (recipesDirty) {
            if (selectedCraftable != null) {
                broadcastRecipesForResultItem(selectedCraftable.resultItem());
            }
            recipesDirty = false;
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

    public void selectCraftable(@Nullable RecipeWithStatus recipe) {
        selectedCraftable = recipe;
        resetSelectedRecipe();
        updateCraftableSlots();

        if (recipe != null) {
            if (player.level().isClientSide) {
                lockedInputs = null;
                selectionRecipesPending = true;
                requestSelectionRecipes(recipe);
            }
        } else {
            selectionRecipesPending = false;
            resetSelectedRecipe();
            updateMatrixSlots();
        }
    }

    public void resetSelectedRecipe() {
        recipesForSelection = null;
        recipesForSelectionIndex = 0;
        lockedInputs = null;
        updateMatrixSlots();
    }

    public void requestCraftables() {
        Balm.getNetworking().sendToServer(new RequestAvailableCraftablesMessage());
    }

    public void handleRequestCraftables() {
        craftablesDirty = true;
    }

    public void requestSelectionRecipes(RecipeWithStatus craftable) {
        final var selectedRecipe = getSelectedRecipe();
        Balm.getNetworking().sendToServer(new RequestSelectionRecipesMessage(craftable.resultItem(), selectedRecipe != null ? selectedRecipe.recipe(player).id() : null, lockedInputs != null ? lockedInputs : List.of()));
    }

    public void handleRequestSelectionRecipes(ItemStack resultItem, ResourceLocation selectedRecipe, List<ItemStack> lockedInputs) {
        selectedCraftable = findRecipeForResultItem(resultItem);
        this.serversideSelectedRecipe = selectedRecipe;
        this.lockedInputs = lockedInputs;
        recipesDirty = true;
    }

    private void requestCraft(boolean craftFullStack, boolean addToInventory) {
        final var selectedRecipe = getSelectedRecipe();
        if (selectedRecipe != null) {
            final var recipe = selectedRecipe.recipe(player);
            if (kitchen.canProcess(recipe.value().getType())) {
                Balm.getNetworking().sendToServer(new CraftRecipeMessage(recipe.id(), lockedInputs, craftFullStack, addToInventory));
            }
        }
    }

    public List<RecipeWithStatus> getAvailableCraftables() {
        final var result = new HashMap<ResourceLocation, RecipeWithStatus>();
        final var context = new CraftingContext(kitchen, player);
        final var recipesByItemId = CookingForBlockheadsRegistry.getRecipesByItemId();
        for (ResourceLocation itemId : recipesByItemId.keySet()) {
            for (final var recipe : recipesByItemId.get(itemId)) {
                final var resultItem = recipe.value().getResultItem(player.level().registryAccess());
                if (isGroupItem(resultItem)) {
                    continue;
                }

                final var operation = context.createOperation(recipe).prepare();
                if (!shouldShowRecipe(operation)) {
                    continue;
                }

                final var recipeWithStatus = new RecipeWithStatus(recipe.id(),
                        resultItem,
                        operation.getMissingIngredients(),
                        operation.getMissingIngredientsMask(),
                        operation.getLockedInputs(),
                        operation.getIngredientOptions());
                result.compute(itemId, (k, v) -> RecipeWithStatus.best(v, recipeWithStatus));
            }
        }
        return result.values().stream().toList();
    }

    private boolean isGroupItem(ItemStack resultItem) {
        final var itemId = BuiltInRegistries.ITEM.getKey(resultItem.getItem());
        for (final var group : CookingForBlockheadsRegistry.getGroups()) {
            final var groupItemId = BuiltInRegistries.ITEM.getKey(group.getParentItem());
            if (groupItemId.equals(itemId)) {
                continue;
            }

            for (final var ingredient : group.getChildren()) {
                if (ingredient.test(resultItem)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Collection<RecipeHolder<Recipe<?>>> getRecipesFor(ItemStack resultItem) {
        final var recipes = new ArrayList<>(CookingForBlockheadsRegistry.getRecipesFor(resultItem));
        recipes.addAll(CookingForBlockheadsRegistry.getRecipesInGroup(resultItem));
        return recipes;
    }

    public void broadcastAvailableRecipes() {
        craftables = getAvailableCraftables();
        Balm.getNetworking().sendTo(player, new AvailableCraftablesListMessage(craftables));
    }

    public void broadcastRecipesForResultItem(ItemStack resultItem) {
        final List<RecipeWithStatus> result = new ArrayList<>();

        final var context = new CraftingContext(kitchen, player);
        final var recipesForResult = getRecipesFor(resultItem);
        for (final var recipe : recipesForResult) {
            final var recipeResultItem = recipe.value().getResultItem(player.level().registryAccess());
            final var operation = context.createOperation(recipe);
            if (recipe.id().equals(serversideSelectedRecipe)) {
                operation.withLockedInputs(lockedInputs);
            }
            operation.prepare();
            if (!shouldShowRecipe(operation)) {
                continue;
            }

            result.add(new RecipeWithStatus(recipe.id(),
                    recipeResultItem,
                    operation.getMissingIngredients(),
                    operation.getMissingIngredientsMask(),
                    operation.getLockedInputs(),
                    operation.getIngredientOptions()));
        }

        result.sort(currentSorting);
        this.recipesForSelection = result;
        Balm.getNetworking().sendTo(player, new SelectionRecipesListMessage(result));
    }

    private boolean shouldShowRecipe(CraftingOperation operation) {
        return operation.hasIngredients() || operation.hasMissingLockedInputs() || kitchen.isNoFilter();
    }

    public void craft(ResourceLocation recipeId, List<ItemStack> lockedInputs, boolean craftFullStack, boolean addToInventory) {
        final var level = player.level();
        final var recipe = (RecipeHolder<Recipe<?>>) level.getRecipeManager().byKey(recipeId).orElse(null);
        if (recipe == null) {
            CookingForBlockheads.logger.error("Received invalid recipe from client: {}", recipeId);
            return;
        }

        if (!kitchen.canProcess(recipe.value().getType())) {
            CookingForBlockheads.logger.error("Received invalid craft request, unprocessable recipe {}", recipeId);
            return;
        }

        var craftable = this.craftables.stream().filter(it -> it.recipe(player) == recipe).findAny().orElse(null);
        if (craftable == null) {
            craftable = this.recipesForSelection.stream().filter(it -> it.recipe(player) == recipe).findAny().orElse(null);
            if (craftable == null) {
                CookingForBlockheads.logger.error("Received invalid craft request, unknown recipe {}", recipeId);
                return;
            }
        }

        final var context = new CraftingContext(kitchen, player);
        context.addListener(operation -> {
            final var feedback = operation.getFeedback();
            feedback.ifPresent(component -> Balm.getNetworking().sendTo(player, new KitchenFeedbackMessage(component)));
        });
        final var operation = context.createOperation(recipe).withLockedInputs(lockedInputs);
        final var resultItem = recipe.value().getResultItem(level.registryAccess());
        final var repeats = craftFullStack ? resultItem.getMaxStackSize() / resultItem.getCount() : 1;
        for (int i = 0; i < repeats; i++) {
            operation.prepare();
            if (operation.canCraft()) {
                final var carried = getCarried();
                if (!carried.isEmpty() && (!ItemStack.isSameItemSameComponents(carried, resultItem) || carried.getCount() >= carried.getMaxStackSize())) {
                    if (craftFullStack || addToInventory) {
                        addToInventory = true;
                    } else {
                        break;
                    }
                }
                final var itemStack = operation.craft(this, player.level().registryAccess());
                if (!itemStack.isEmpty()) {
                    if (addToInventory) {
                        if (!player.getInventory().add(itemStack)) {
                            player.drop(itemStack, false);
                        }
                    } else {

                        if (carried.isEmpty()) {
                            setCarried(itemStack);
                        } else if (ItemStack.isSameItemSameComponents(carried, itemStack) && carried.getCount() < carried.getMaxStackSize()) {
                            carried.grow(itemStack.getCount());
                        } else {
                            if (!player.getInventory().add(itemStack)) {
                                player.drop(itemStack, false);
                            }
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        craftablesDirty = true;
        recipesDirty = true;
    }

    public void setCraftables(List<RecipeWithStatus> craftables) {
        int previousSelectionIndex = selectedCraftable != null ? filteredCraftables.indexOf(selectedCraftable) : -1;

        this.craftables = craftables;
        updateFilteredRecipes();

        // Make sure the previously selected recipe stays in the same slot, even if others moved
        if (previousSelectionIndex != -1) {
            Iterator<RecipeWithStatus> it = filteredCraftables.iterator();
            RecipeWithStatus found = null;
            while (it.hasNext()) {
                RecipeWithStatus recipe = it.next();
                if (ItemStack.isSameItem(recipe.resultItem(), selectedCraftable.resultItem())) {
                    found = recipe;
                    it.remove();
                    break;
                }
            }
            while (previousSelectionIndex > filteredCraftables.size()) {
                filteredCraftables.add(null);
            }
            filteredCraftables.add(previousSelectionIndex, found);
            selectedCraftable = found;
        }

        // Updates the items inside the recipe slots
        updateCraftableSlots();

        setScrollOffsetDirty(true);
    }

    public void updateCraftableSlots() {
        int i = scrollOffset * 3;
        for (final var slot : recipeListingSlots) {
            if (i < filteredCraftables.size()) {
                final var craftable = filteredCraftables.get(i);
                if (craftable != null && selectedCraftable != null && ItemStack.isSameItemSameComponents(selectedCraftable.resultItem(),
                        craftable.resultItem())) {
                    final var selectedRecipe = getSelectedRecipe();
                    slot.setCraftable(selectedRecipe != null ? selectedRecipe : craftable);
                } else {
                    slot.setCraftable(craftable);
                }
                i++;
            } else {
                slot.setCraftable(null);
            }
        }
    }

    private void updateMatrixSlots() {
        final var selectedRecipe = getSelectedRecipe();
        if (selectedRecipe != null) {
            final var recipe = selectedRecipe.recipe(player);
            updateMatrixSlots(recipe.value(), selectedRecipe);
        } else {
            for (int i = 0; i < matrixSlots.size(); i++) {
                CraftMatrixFakeSlot matrixSlot = matrixSlots.get(i);
                matrixSlot.setIngredient(i, List.of(), -1);
                matrixSlot.setMissing(true);
            }
        }
    }

    private <C extends RecipeInput, T extends Recipe<C>> void updateMatrixSlots(T recipe, RecipeWithStatus status) {
        final var ingredients = recipe.getIngredients();
        final var matrix = NonNullList.withSize(9, Ingredient.EMPTY);
        final var missingMatrix = new boolean[9];
        final var ingredientIndexMatrix = new int[9];
        Arrays.fill(ingredientIndexMatrix, -1);
        final var recipeTypeHandler = CookingForBlockheadsRegistry.getRecipeWorkshopHandler(recipe);
        if (recipeTypeHandler != null) {
            for (int i = 0; i < ingredients.size(); i++) {
                final var ingredient = ingredients.get(i);
                final var matrixSlot = recipeTypeHandler.mapToMatrixSlot(recipe, i);
                matrix.set(matrixSlot, ingredient);
                missingMatrix[matrixSlot] = (status.missingIngredientsMask() & (1 << i)) == (1 << i);
                ingredientIndexMatrix[matrixSlot] = i;
            }
        }

        for (int i = 0; i < matrixSlots.size(); i++) {
            final var matrixSlot = matrixSlots.get(i);
            final var ingredientOptions = status.ingredientOptions();
            final int ingredientIndex = ingredientIndexMatrix[i];
            final var lockedInput = lockedInputs != null && ingredientIndex >= 0 && ingredientIndex < lockedInputs.size() ? lockedInputs.get(ingredientIndex) : ItemStack.EMPTY;
            final var options = ingredientIndex >= 0 && ingredientIndex < ingredientOptions.size() ? ingredientOptions.get(ingredientIndex) : List.<ItemStack>of();
            var optionIndex = 0;
            for (int j = 0; j < options.size(); j++) {
                if (ItemStack.isSameItemSameComponents(options.get(j), lockedInput)) {
                    optionIndex = j;
                    break;
                }
            }
            matrixSlot.setIngredient(ingredientIndex, options, optionIndex);
            matrixSlot.setMissing(missingMatrix[i]);
        }
    }

    public void setSortComparator(Comparator<RecipeWithStatus> comparator) {
        currentSorting = new FavoriteComparator(comparator);
        // When re-sorting, make sure to remove all null slots that were added to preserve layout
        filteredCraftables.removeIf(Objects::isNull);
        filteredCraftables.sort(currentSorting);
        updateCraftableSlots();
    }

    public int getItemListCount() {
        return filteredCraftables.size();
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        updateCraftableSlots();
    }

    public void search(@Nullable String term) {
        this.currentSearch = term;
        updateFilteredRecipes();
        setScrollOffset(0);
    }

    private void updateFilteredRecipes() {
        filteredCraftables.clear();
        for (RecipeWithStatus craftable : craftables) {
            if (searchMatches(craftable)) {
                filteredCraftables.add(craftable);
            }
        }
        filteredCraftables.sort(currentSorting);
    }

    private boolean searchMatches(RecipeWithStatus craftable) {
        if (currentSearch == null || currentSearch.trim().isEmpty()) {
            return true;
        }

        final var resultItem = craftable.resultItem();
        final var lowerCaseSearch = currentSearch.toLowerCase();
        if (resultItem.getDisplayName().getString().toLowerCase(Locale.ENGLISH).contains(lowerCaseSearch)) {
            return true;
        } else {
            List<Component> tooltips = resultItem.getTooltipLines(Item.TooltipContext.EMPTY, player, TooltipFlag.Default.NORMAL);
            for (Component tooltip : tooltips) {
                if (tooltip.getString().toLowerCase(Locale.ENGLISH).contains(lowerCaseSearch)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public RecipeWithStatus getSelectedRecipe() {
        return recipesForSelection != null ? recipesForSelection.get(recipesForSelectionIndex) : null;
    }

    public boolean isSelectionRecipesPending() {
        return selectionRecipesPending;
    }

    public boolean isSelectedSlot(CraftableListingFakeSlot slot) {
        final var selectedRecipe = getSelectedRecipe();
        final var craftable = selectedRecipe != null ? selectedRecipe : selectedCraftable;
        return craftable != null && slot.getCraftable() != null && ItemStack.isSameItemSameComponents(slot.getCraftable().resultItem(),
                craftable.resultItem());
    }

    public boolean isScrollOffsetDirty() {
        return scrollOffsetDirty;
    }

    public void setScrollOffsetDirty(boolean dirty) {
        scrollOffsetDirty = dirty;
    }

    public void setRecipesForSelection(List<RecipeWithStatus> recipes) {
        selectionRecipesPending = false;
        if (recipes.isEmpty()) {
            selectedCraftable = null;
            resetSelectedRecipe();
            updateCraftableSlots();
            return;
        }

        recipesForSelection = recipes;
        recipesForSelectionIndex = Math.max(0, Math.min(recipesForSelection.size() - 1, recipesForSelectionIndex));
        lockedInputs = recipesForSelection.get(recipesForSelectionIndex).lockedInputs();

        updateCraftableSlots();
        updateMatrixSlots();
    }

    public void nextRecipe(int dir) {
        if (recipesForSelection != null) {
            recipesForSelectionIndex = Math.max(0, Math.min(recipesForSelection.size() - 1, recipesForSelectionIndex + dir));
            lockedInputs = recipesForSelection.get(recipesForSelectionIndex).lockedInputs();
            updateCraftableSlots();
        }

        updateMatrixSlots();
    }

    public boolean selectionHasRecipeVariants() {
        return recipesForSelection != null && recipesForSelection.size() > 1;
    }

    public boolean selectionHasPreviousRecipe() {
        return recipesForSelectionIndex > 0;
    }

    public boolean selectionHasNextRecipe() {
        return recipesForSelection != null && recipesForSelectionIndex < recipesForSelection.size() - 1;
    }

    public List<CraftMatrixFakeSlot> getMatrixSlots() {
        return matrixSlots;
    }

    @Nullable
    public RecipeWithStatus findRecipeForResultItem(ItemStack resultItem) {
        return craftables.stream().filter(it -> ItemStack.isSameItemSameComponents(it.resultItem(), resultItem)).findAny().orElse(null);
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public void setLockedInput(int i, ItemStack lockedInput) {
        if (selectedCraftable != null) {
            final var recipe = getSelectedRecipe().recipe(player).value();
            if (lockedInputs == null || lockedInputs.size() != recipe.getIngredients().size()) {
                lockedInputs = NonNullList.withSize(recipe.getIngredients().size(), ItemStack.EMPTY);
            }
            lockedInputs.set(i, lockedInput);
            requestSelectionRecipes(selectedCraftable);
        }
    }

    public int getRecipesForSelectionIndex() {
        return filteredCraftables.indexOf(selectedCraftable);
    }

    public void clearHistory() {
        history.clear();
    }

    public void pushHistory() {
        if (selectedCraftable != null) {
            history.add(selectedCraftable);
        }
    }

    public void popHistory() {
        if (!history.isEmpty()) {
            final var entry = history.removeLast();
            selectCraftable(entry);
        }
    }
}
