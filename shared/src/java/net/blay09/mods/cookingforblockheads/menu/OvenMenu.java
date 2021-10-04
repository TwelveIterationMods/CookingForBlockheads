package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.menu.slot.*;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OvenMenu extends AbstractContainerMenu implements IContainerWithDoor {

    private final OvenBlockEntity tileEntity;

    public OvenMenu(int windowId, Inventory playerInventory, OvenBlockEntity oven) {
        super(ModMenus.oven.get(), windowId);
        this.tileEntity = oven;

        Container container = oven.getContainer();

        int offsetX = oven.hasPowerUpgrade() ? -5 : 0;

        for (int i = 0; i < 3; i++) {
            addSlot(new Slot(container, i, 84 + i * 18 + offsetX, 19));
        }

        addSlot(new SlotOvenFuel(container, 3, 61 + offsetX, 59));

        for (int i = 0; i < 3; i++) {
            addSlot(new OvenResultSlot(playerInventory.player, oven, container, i + 4, 142 + offsetX, 41 + i * 18));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotOven(container, 7 + j + i * 3, 84 + j * 18 + offsetX, 41 + i * 18));
            }
        }

        for (int i = 0; i < 4; i++) {
            addSlot(new SlotOvenTool(container, 16 + i, 8, 19 + i * 18, i));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 30 + j * 18, 111 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 30 + i * 18, 169));
        }

        oven.getDoorAnimator().openContainer(playerInventory.player);

        addDataSlots(oven.getContainerData());
    }

    public OvenBlockEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        tileEntity.getDoorAnimator().closeContainer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (slotIndex >= 7 && slotIndex < 20) {
                if (!moveItemStackTo(slotStack, 20, 56, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 4 && slotIndex <= 6) {
                if (!this.moveItemStackTo(slotStack, 20, 56, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (slotIndex >= 20) {
                ItemStack smeltingResult = tileEntity.getSmeltingResult(slotStack);
                if (OvenBlockEntity.isItemFuel(slotStack)) {
                    if (!moveItemStackTo(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!smeltingResult.isEmpty()) {
                    if (!this.moveItemStackTo(slotStack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 20 && slotIndex < 47) {
                    if (!this.moveItemStackTo(slotStack, 47, 56, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 47 && slotIndex < 56 && !this.moveItemStackTo(slotStack, 20, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 20, 47, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean isTileEntity(BlockEntity blockEntity) {
        return this.tileEntity == blockEntity;
    }
}
