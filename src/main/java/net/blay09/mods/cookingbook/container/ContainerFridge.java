package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.block.TileEntityFridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFridge extends Container {

    private final TileEntityFridge tileEntity;

    public ContainerFridge(InventoryPlayer inventoryPlayer, TileEntityFridge tileEntity) {
        this.tileEntity = tileEntity;

        int numRows = 3;
        if(tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord) == CookingBook.blockFridge || tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord) == CookingBook.blockFridge) {
            numRows = 6;
        }
        int playerInventoryStart = numRows * 18;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(tileEntity, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 31 + i * 18 + playerInventoryStart));
            }
        }

        for(int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 89 + playerInventoryStart));
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < 6 * 9)
            {
                if (!this.mergeItemStack(itemstack1, 6 * 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 6 * 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
