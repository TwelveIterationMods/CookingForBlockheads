package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class TileToaster extends TileEntity implements ITickable {

    private static final int TOAST_TICKS = 1200;

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
            VanillaPacketHandler.sendTileEntityUpdate(TileToaster.this);
        }
    };

    private boolean active;
    private int toastTicks;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
        active = tagCompound.getBoolean("Active");
        toastTicks = tagCompound.getInteger("ToastTicks");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
        tagCompound.setBoolean("Active", active);
        tagCompound.setInteger("ToastTicks", toastTicks);
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void update() {
        if(active) {
            toastTicks--;
            if(toastTicks <= 0 && !worldObj.isRemote) {
                for(int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack inputStack = itemHandler.getStackInSlot(i);
                    if(inputStack != null) {
                        ToastHandler toastHandler = CookingRegistry.getToastHandler(inputStack);
                        ItemStack outputStack = toastHandler instanceof ToastOutputHandler ? ((ToastOutputHandler) toastHandler).getToasterOutput(inputStack) : null;
                        if (outputStack == null) {
                            outputStack = inputStack;
                        } else {
                            outputStack = outputStack.copy();
                        }
                        EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5f, pos.getY() + 0.75f, pos.getZ() + 0.5f, outputStack);
                        entityItem.motionX = 0f;
                        entityItem.motionY = 0.1f;
                        entityItem.motionZ = 0f;
                        worldObj.spawnEntityInWorld(entityItem);
                        itemHandler.setStackInSlot(i, null);
                    }
                }
                setActive(false);
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if(active) {
            toastTicks = TOAST_TICKS;
        } else {
            toastTicks = 0;
        }
        VanillaPacketHandler.sendTileEntityUpdate(this);
    }

    public boolean isActive() {
        return active;
    }

    public IItemHandlerModifiable getItemHandler() {
        return itemHandler;
    }

    public float getToastProgress() {
        return 1f - toastTicks / (float) TOAST_TICKS;
    }
}
