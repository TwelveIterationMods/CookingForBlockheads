package net.blay09.mods.cookingforblockheads.container;

import net.blay09.mods.cookingforblockheads.container.slot.*;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class OvenContainer extends Container implements IContainerWithDoor {

    private final TileOven tileEntity;
    private final int[] lastCookTime = new int[9];
    private int lastBurnTime;
    private int lastItemBurnTime;

    public OvenContainer(int windowId, PlayerInventory playerInventory, TileOven tileEntity) {
        super(ModContainers.oven, windowId);
        this.tileEntity = tileEntity;

        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;

        for (int i = 0; i < 3; i++) {
            addSlot(new SlotOvenInput(tileEntity.getItemHandler(), i, 84 + i * 18 + offsetX, 19));
        }

        addSlot(new SlotOvenFuel(tileEntity.getItemHandler(), 3, 61 + offsetX, 59));

        for (int i = 0; i < 3; i++) {
            addSlot(new SlotOvenResult(tileEntity.getItemHandler(), i + 4, 142 + offsetX, 41 + i * 18));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotOven(tileEntity.getItemHandler(), 7 + j + i * 3, 84 + j * 18 + offsetX, 41 + i * 18));
            }
        }

        for (int i = 0; i < 4; i++) {
            addSlot(new SlotOvenTool(tileEntity.getItemHandler(), 16 + i, 8, 19 + i * 18, i));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 30 + j * 18, 111 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 30 + i * 18, 169));
        }

        tileEntity.getDoorAnimator().openContainer(playerInventory.player);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, tileEntity.furnaceBurnTime);
        listener.sendWindowProperty(this, 1, tileEntity.currentItemBurnTime);
        for (int i = 0; i < tileEntity.slotCookTime.length; i++) {
            listener.sendWindowProperty(this, 2 + i, tileEntity.slotCookTime[i]);
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        tileEntity.getDoorAnimator().closeContainer(player);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) { // TODO at?
            if (this.lastBurnTime != tileEntity.furnaceBurnTime) {
                listener.sendWindowProperty(this, 0, tileEntity.furnaceBurnTime);
            }

            if (this.lastItemBurnTime != tileEntity.currentItemBurnTime) {
                listener.sendWindowProperty(this, 1, tileEntity.currentItemBurnTime);
            }

            for (int i = 0; i < tileEntity.slotCookTime.length; i++) {
                if (lastCookTime[i] != tileEntity.slotCookTime[i]) {
                    listener.sendWindowProperty(this, 2 + i, tileEntity.slotCookTime[i]);
                }
            }

        }

        this.lastBurnTime = this.tileEntity.furnaceBurnTime;
        this.lastItemBurnTime = this.tileEntity.currentItemBurnTime;
        System.arraycopy(tileEntity.slotCookTime, 0, lastCookTime, 0, tileEntity.slotCookTime.length);
    }

    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            tileEntity.furnaceBurnTime = value;
        } else if (id == 1) {
            tileEntity.currentItemBurnTime = value;
        } else if (id >= 2 && id <= tileEntity.slotCookTime.length + 2) {
            tileEntity.slotCookTime[id - 2] = value;
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (slotIndex >= 7 && slotIndex < 20) {
                if (!mergeItemStack(slotStack, 20, 56, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, itemStack);
            } else if (slotIndex >= 4 && slotIndex <= 6) {
                if (!this.mergeItemStack(slotStack, 20, 56, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 20) {
                ItemStack smeltingResult = TileOven.getSmeltingResult(slotStack);
                if (TileOven.isItemFuel(slotStack)) {
                    if (!mergeItemStack(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!smeltingResult.isEmpty()) {
                    if (!this.mergeItemStack(slotStack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 20 && slotIndex < 47) {
                    if (!this.mergeItemStack(slotStack, 47, 56, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 47 && slotIndex < 56 && !this.mergeItemStack(slotStack, 20, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 20, 47, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean isTileEntity(TileEntity tileEntity) {
        return this.tileEntity == tileEntity;
    }

    /* TODO @ContainerSectionCallback
    @Optional.Method(modid = Compat.INVENTORY_TWEAKS)
    @SuppressWarnings("unchecked unused")
    public Map<ContainerSection, List<Slot>> getContainerSections() {
        Map<ContainerSection, List<Slot>> map = Maps.newHashMap();
        map.put(ContainerSection.FURNACE_IN, inventorySlots.subList(0, 3));
        map.put(ContainerSection.FURNACE_FUEL, inventorySlots.subList(3, 4));
        map.put(ContainerSection.FURNACE_OUT, inventorySlots.subList(4, 7));
        map.put(ContainerSection.INVENTORY, inventorySlots.subList(20, 57));
        map.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(20, 48));
        map.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(47, 57));
        return map;
    }*/
}
