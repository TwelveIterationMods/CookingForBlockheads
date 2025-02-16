package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.tile.SpiceRackBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SpiceRackMenu extends AbstractContainerMenu {

    private final SpiceRackBlockEntity spiceRack;

    public SpiceRackMenu(int windowId, Inventory playerInventory, SpiceRackBlockEntity spiceRack) {
        super(ModMenus.spiceRack.get(), windowId);
        this.spiceRack = spiceRack;

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(spiceRack.getContainer(), i, 8 + i * 18, 18));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 50 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 108));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotIndex < 9) {
                if (!this.moveItemStackTo(slotStack, 9, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
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
        return Container.stillValidBlockEntity(spiceRack, player);
    }
}
