package net.blay09.mods.cookingforblockheads.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FakeSlot extends Slot {

    private ItemStack displayStack = ItemStack.EMPTY;

    public FakeSlot(int slotId, int x, int y) {
        super(null, slotId, x, y);
    }

    public void setDisplayStack(ItemStack itemStack) {
        this.displayStack = itemStack;
    }

    @Override
    public ItemStack getStack() {
        return displayStack;
    }

    @Override
    public boolean getHasStack() {
        return !displayStack.isEmpty();
    }

    @Override
    public void putStack(ItemStack stack) {
        // NOP
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onSlotChanged() {
        // NOP
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        return false;
    }
}
