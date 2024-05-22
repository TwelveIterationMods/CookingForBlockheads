package net.blay09.mods.cookingforblockheads.menu.slot;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OvenResultSlot extends Slot {

    private final Player player;
    private final OvenBlockEntity tileEntity;
    private int removeCount;

    public OvenResultSlot(Player player, OvenBlockEntity tileEntity, Container container, int i, int x, int y) {
        super(container, i, x, y);
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    public ItemStack remove(int amount) {
        if (hasItem()) {
            removeCount += Math.min(amount, getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onQuickCraft(ItemStack oldStack, ItemStack newStack) {
        int amount = newStack.getCount() - oldStack.getCount();
        if (amount > 0) {
            onQuickCraft(newStack, amount);
        }
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        checkTakeAchievements(itemStack);
        super.onTake(player, itemStack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        removeCount += amount;
        checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(player.level(), player, removeCount);

        removeCount = 0;
        if (tileEntity.getLevel() != null && !stack.isEmpty()) {
            Balm.getEvents().fireEvent(new OvenItemSmeltedEvent(player, tileEntity.getLevel(), tileEntity.getBlockPos(), stack));
        }
    }
}
