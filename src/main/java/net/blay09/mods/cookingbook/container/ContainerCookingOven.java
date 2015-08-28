package net.blay09.mods.cookingbook.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.cookingbook.block.TileEntityCookingOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerCookingOven extends Container {

    private final TileEntityCookingOven tileEntity;
    private final int[] lastCookTime = new int[9];
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerCookingOven(InventoryPlayer inventoryPlayer, TileEntityCookingOven tileEntity) {
        this.tileEntity = tileEntity;

        for(int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotOvenInput(tileEntity, i, 62 + i * 18, 19));
        }

        addSlotToContainer(new SlotOvenFuel(tileEntity, 3, 39, 59));

        for(int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotOvenResult(tileEntity, i + 4, 120, 41 + i * 18));
        }

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotOven(tileEntity, 7 + j + i * 3, 62 + j * 18, 41 + i * 18));
            }
        }

        for(int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotOvenTool(tileEntity, 16 + i, -14, 19 + i * 18));
        }

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 169));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, tileEntity.furnaceBurnTime);
        crafter.sendProgressBarUpdate(this, 1, tileEntity.currentItemBurnTime);
        for(int i = 0; i < tileEntity.slotCookTime.length; i++) {
            crafter.sendProgressBarUpdate(this, 2 + i, tileEntity.slotCookTime[i]);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(Object obj : crafters) {
            ICrafting crafter = (ICrafting) obj;

            if (this.lastBurnTime != tileEntity.furnaceBurnTime) {
                crafter.sendProgressBarUpdate(this, 0, tileEntity.furnaceBurnTime);
            }

            if (this.lastItemBurnTime != tileEntity.currentItemBurnTime) {
                crafter.sendProgressBarUpdate(this, 1, tileEntity.currentItemBurnTime);
            }

            for (int i = 0; i < tileEntity.slotCookTime.length; i++) {
                if (lastCookTime[i] != tileEntity.slotCookTime[i]) {
                    crafter.sendProgressBarUpdate(this, 2 + i, tileEntity.slotCookTime[i]);
                }
            }

        }

        this.lastBurnTime = this.tileEntity.furnaceBurnTime;
        this.lastItemBurnTime = this.tileEntity.currentItemBurnTime;
        for (int i = 0; i < tileEntity.slotCookTime.length; i++) {
            lastCookTime[i] = tileEntity.slotCookTime[i];
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            tileEntity.furnaceBurnTime = value;
        } else if (id == 1) {
            tileEntity.currentItemBurnTime = value;
        } else if(id >= 2 && id <= tileEntity.slotCookTime.length + 2) {
            tileEntity.slotCookTime[id - 2] = value;
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (slotIndex >= 7 && slotIndex <= 20) {
                if (!mergeItemStack(slotStack, 20, 56, true)) {
                    return null;
                }
                slot.onSlotChange(slotStack, itemStack);
            } else if(slotIndex >= 4 && slotIndex <= 6) {
                if (!this.mergeItemStack(slotStack, 20, 56, false)) {
                    return null;
                }
            } else if (slotIndex > 20) {
                ItemStack smeltingResult = TileEntityCookingOven.getSmeltingResult(slotStack);
                if (TileEntityCookingOven.isItemFuel(slotStack)) {
                    if (!mergeItemStack(slotStack, 3, 4, false)) {
                        return null;
                    }
                } else if (smeltingResult != null && smeltingResult.getItem() instanceof ItemFood) {
                    if (!this.mergeItemStack(slotStack, 0, 3, false)) {
                        return null;
                    }
                } else if (slotIndex >= 21 && slotIndex < 49) {
                    if (!this.mergeItemStack(slotStack, 49, 56, false)) {
                        return null;
                    }
                } else if (slotIndex >= 49 && slotIndex < 56 && !this.mergeItemStack(slotStack, 20, 49, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(slotStack, 20, 49, false)) {
                return null;
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

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
