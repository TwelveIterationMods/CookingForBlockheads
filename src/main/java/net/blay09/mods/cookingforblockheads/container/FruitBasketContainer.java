package net.blay09.mods.cookingforblockheads.container;

import invtweaks.api.container.ChestContainer;
import net.blay09.mods.cookingforblockheads.tile.FruitBasketTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class FruitBasketContainer extends Container {

    private final int numRows;

    public FruitBasketContainer(int windowId, PlayerInventory playerInventory, FruitBasketTileEntity tileFruitBasket) {
        super(ModContainers.fruitBasket, windowId);
        this.numRows = tileFruitBasket.getItemHandler().getSlots() / 9;
        int playerInventoryStart = numRows * 18;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new SlotItemHandler(tileFruitBasket.getItemHandler(), j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 31 + i * 18 + playerInventoryStart));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 89 + playerInventoryStart));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (slotIndex < numRows * 9) {
                if (!this.mergeItemStack(slotStack, numRows * 9, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 0, numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public int getNumRows() {
        return numRows;
    }
}
