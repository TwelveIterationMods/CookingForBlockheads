package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CookieJarMenu extends AbstractContainerMenu {

    private static final int SLOT_COUNT = 5;

    private final Container container;
    private final ContainerLevelAccess access;

    public CookieJarMenu(int windowId, Inventory playerInventory) {
        this(windowId, playerInventory, new SimpleContainer(SLOT_COUNT), ContainerLevelAccess.NULL);
    }

    public CookieJarMenu(int windowId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(ModMenus.cookieJar.value(), windowId);
        checkContainerSize(container, SLOT_COUNT);
        this.container = container;
        this.access = access;
        container.startOpen(playerInventory.player);

        for (int i = 0; i < SLOT_COUNT; i++) {
            addSlot(new Slot(container, i, 44 + i * 18, 20));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotIndex < SLOT_COUNT) {
                if (!moveItemStackTo(slotStack, SLOT_COUNT, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(slotStack, 0, SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.cookieJar.value());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }
}
