package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.tile.FruitBasketBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FruitBasketMenu extends AbstractContainerMenu {

    private final FruitBasketBlockEntity fruitBasket;
    private final int numRows;

    public FruitBasketMenu(int windowId, Inventory playerInventory, FruitBasketBlockEntity fruitBasket) {
        super(ModMenus.fruitBasket.get(), windowId);
        this.fruitBasket = fruitBasket;
        this.numRows = fruitBasket.getContainer().getContainerSize() / 9;
        int playerInventoryStart = numRows * 18;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(fruitBasket.getContainer(), j + i * 9, 8 + j * 18, 18 + i * 18));
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
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotIndex < numRows * 9) {
                if (!this.moveItemStackTo(slotStack, numRows * 9, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, numRows * 9, false)) {
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
        return Container.stillValidBlockEntity(fruitBasket, player);
    }

    public int getNumRows() {
        return numRows;
    }
}
