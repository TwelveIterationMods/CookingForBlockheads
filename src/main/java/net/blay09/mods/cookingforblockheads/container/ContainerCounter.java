package net.blay09.mods.cookingforblockheads.container;

import invtweaks.api.container.ChestContainer;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class ContainerCounter extends Container implements IContainerWithDoor {

    private TileCounter tileCounter;
    private final int numRows;

    public ContainerCounter(EntityPlayer player, TileCounter tileCounter) {
        this.tileCounter = tileCounter;
        this.numRows = tileCounter.getItemHandler().getSlots() / 9;
        int playerInventoryStart = numRows * 18;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(tileCounter.getItemHandler(), j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 31 + i * 18 + playerInventoryStart));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 89 + playerInventoryStart));
        }

        tileCounter.getDoorAnimator().openContainer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        tileCounter.getDoorAnimator().closeContainer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (slotIndex < numRows * 9) {
                if (!this.mergeItemStack(slotStack, numRows * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(slotStack, 0, numRows * 9, false)) {
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isTileEntity(TileEntity tileEntity) {
        return tileCounter == tileEntity;
    }

}
