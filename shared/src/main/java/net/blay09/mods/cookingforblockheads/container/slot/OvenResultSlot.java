package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OvenResultSlot extends SlotItemHandler {

    private final PlayerEntity player;
    private final OvenTileEntity tileEntity;
    private int removeCount;

    public OvenResultSlot(PlayerEntity player, OvenTileEntity tileEntity, IItemHandler itemHandler, int i, int x, int y) {
        super(itemHandler, i, x, y);
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (getHasStack()) {
            removeCount += Math.min(amount, getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public void onSlotChange(ItemStack oldStack, ItemStack newStack) {
        int amount = newStack.getCount() - oldStack.getCount();
        if (amount > 0) {
            onCrafting(newStack, amount);
        }
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        onCrafting(stack);
        return super.onTake(thePlayer, stack);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        removeCount += amount;
        onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(player.world, player, removeCount);

        removeCount = 0;
        if (tileEntity.getWorld() != null && !stack.isEmpty()) {
            MinecraftForge.EVENT_BUS.post(new OvenItemSmeltedEvent(player, tileEntity.getWorld(), tileEntity.getPos(), stack));
        }
    }
}
