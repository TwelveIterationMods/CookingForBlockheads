package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CuttingBoardMenu extends RecipeBookMenu {
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public CuttingBoardMenu(int windowId, Inventory inventory) {
        this(windowId, inventory, ContainerLevelAccess.NULL);
    }

    public CuttingBoardMenu(int windowId, Inventory inventory, ContainerLevelAccess access) {
        super(MenuType.CRAFTING, windowId);
        this.access = access;
        this.player = inventory.player;
        addSlot(new ResultSlot(inventory.player, craftSlots, resultSlots, 0, 124, 35));

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addSlot(new Slot(craftSlots, y + x * 3, 30 + y * 18, 17 + x * 18));
            }
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 9; y++) {
                addSlot(new Slot(inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (!level.isClientSide) {
            final var recipeInput = craftingContainer.asCraftInput();
            final var serverPlayer = (ServerPlayer) player;
            var itemStack = ItemStack.EMPTY;
            final var optionalRecipe = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, recipeInput, level);
            if (optionalRecipe.isPresent()) {
                final var recipeHolder = optionalRecipe.get();
                final var recipe = recipeHolder.value();
                if (resultContainer.setRecipeUsed(serverPlayer, recipeHolder)) {
                    final var assembledStack = recipe.assemble(recipeInput, level.registryAccess());
                    if (assembledStack.isItemEnabled(level.enabledFeatures())) {
                        itemStack = assembledStack;
                    }
                }
            }

            resultContainer.setItem(0, itemStack);
            menu.setRemoteSlot(0, itemStack);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemStack));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        access.execute((level, pos) -> slotChangedCraftingGrid(this, level, player, craftSlots, resultSlots));
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        craftSlots.fillStackedContents(stackedContents);
    }

    @Override
    public void clearCraftingContent() {
        craftSlots.clearContent();
        resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
        return recipe.value().matches(craftSlots.asCraftInput(), player.level());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, pos) -> clearContainer(player, craftSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.cuttingBoard);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        var itemStack = ItemStack.EMPTY;
        final var slot = slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            final var slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotId == 0) {
                this.access.execute(($$2x, $$3x) -> slotStack.getItem().onCraftedBy(slotStack, $$2x, player));
                if (!moveItemStackTo(slotStack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (slotId >= 10 && slotId < 46) {
                if (!moveItemStackTo(slotStack, 1, 10, false)) {
                    if (slotId < 37) {
                        if (!moveItemStackTo(slotStack, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!moveItemStackTo(slotStack, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!moveItemStackTo(slotStack, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            if (slotId == 0) {
                player.drop(slotStack, false);
            }
        }

        return itemStack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != resultSlots && super.canTakeItemForPickAll(itemStack, slot);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot != getResultSlotIndex();
    }
}
