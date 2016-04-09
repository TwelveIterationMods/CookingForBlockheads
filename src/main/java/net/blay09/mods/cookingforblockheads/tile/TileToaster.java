package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileToaster extends TileEntity implements ITickable {

    private static final int TOAST_TICKS = 60;

    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    private boolean active;
    private int toastTicks;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new SPacketUpdateTileEntity(pos, 0, tagCompound);
    }

    @Override
    public void update() {
        if(active) {
            toastTicks--;
            if(toastTicks <= 0) {
                for(int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack inputStack = itemHandler.getStackInSlot(i);
                    if(inputStack != null) {
                        ItemStack outputStack = CookingRegistry.getToastOutput(inputStack);
                        if (outputStack == null) {
                            outputStack = inputStack;
                        }
                        if (!worldObj.isRemote) {
                            EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5f, pos.getY() + 0.75f, pos.getZ() + 0.5f, outputStack);
                            entityItem.motionX = 0f;
                            entityItem.motionY = 0.1f;
                            entityItem.motionZ = 0f;
                            if (worldObj.spawnEntityInWorld(entityItem)) {
                                itemHandler.setStackInSlot(i, null);
                            }
                        }
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
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }
}
