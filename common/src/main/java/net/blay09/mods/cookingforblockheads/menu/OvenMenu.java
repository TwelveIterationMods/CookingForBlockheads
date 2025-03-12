package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.menu.slot.*;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.blay09.mods.cookingforblockheads.network.message.ClientboundOvenResultsPacket;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class OvenMenu extends AbstractContainerMenu implements IContainerWithDoor {

    private final OvenBlockEntity oven;
    private final Player player;
    private NonNullList<ItemStack> resultItems = NonNullList.withSize(9, ItemStack.EMPTY);

    public OvenMenu(int windowId, Inventory playerInventory, OvenBlockEntity oven) {
        super(ModMenus.oven.get(), windowId);
        this.oven = oven;
        this.player = playerInventory.player;

        Container container = oven.getInternalContainer();

        int offsetX = oven.hasPowerUpgrade() ? -5 : 0;

        for (int i = 0; i < 3; i++) {
            addSlot(new Slot(container, i, 84 + i * 18 + offsetX, 19));
        }

        addSlot(new SlotOvenFuel(this, container, 3, 61 + offsetX, 59));

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

    public OvenBlockEntity getOven() {
        return oven;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        oven.getDoorAnimator().closeContainer(player);
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
                ItemStack smeltingResult = oven.getSmeltingResult(slotStack);
                if (isFuel(slotStack)) {
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
        return Container.stillValidBlockEntity(oven, player);
    }

    @Override
    public boolean isTileEntity(BlockEntity blockEntity) {
        return this.oven == blockEntity;
    }

    public boolean isFuel(ItemStack itemStack) {
        return OvenBlockEntity.isItemFuel(oven.getLevel(), itemStack);
    }

    public void setResultItems(NonNullList<ItemStack> resultItems) {
        this.resultItems = resultItems;
    }

    public NonNullList<ItemStack> getResultItems() {
        return resultItems;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (!player.isLocalPlayer()) {
            var changes = false;
            for (int i = 0; i < 9; i++) {
                final var result = oven.getSmeltingResult(slots.get(i + 7).getItem());
                if (!ItemStack.isSameItemSameComponents(resultItems.get(i), result)) {
                    resultItems.set(i, result);
                    changes = true;
                }
            }
            if (changes) {
                Balm.getNetworking().sendTo(this.player, new ClientboundOvenResultsPacket(resultItems));
            }
        }
    }
}
