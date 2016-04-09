package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TileFridge extends TileEntity implements ITickable, IKitchenItemProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            isDirty = true;
            markDirty();
        }
    };
    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    private EnumDyeColor fridgeColor = EnumDyeColor.WHITE;
    private boolean isDirty;

    public TileFridge() {
        doorAnimator.setOpenRadius(2);
    }

    public void setFridgeColor(EnumDyeColor fridgeColor) {
        this.fridgeColor = fridgeColor;
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.markAndNotifyBlock(pos, worldObj.getChunkFromBlockCoords(pos), state, state, 1|2);
        markDirty();
    }

    @Override
    public void update() {
        doorAnimator.update();

        if(isDirty) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            isDirty = false;
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
        fridgeColor = EnumDyeColor.byDyeDamage(tagCompound.getByte("FridgeColor"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
        tagCompound.setByte("FridgeColor", (byte) fridgeColor.getDyeDamage());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
        doorAnimator.setForcedOpen(pkt.getNbtCompound().getBoolean("IsForcedOpen"));
        doorAnimator.setNumPlayersUsing(pkt.getNbtCompound().getByte("NumPlayersUsing"));
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        tagCompound.setBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
        tagCompound.setByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        return new SPacketUpdateTileEntity(pos, 0, tagCompound);
    }

    public TileFridge findNeighbourFridge() {
        if (worldObj.getBlockState(pos.up()).getBlock() == ModBlocks.fridge) {
            return (TileFridge) worldObj.getTileEntity(pos.up());
        } else if (worldObj.getBlockState(pos.down()).getBlock() == ModBlocks.fridge) {
            return (TileFridge) worldObj.getTileEntity(pos.down());
        }
        return null;
    }

    public TileFridge getBaseFridge() {
        if (worldObj.getBlockState(pos.down()).getBlock() == ModBlocks.fridge) {
            return (TileFridge) worldObj.getTileEntity(pos.down());
        }
        return this;
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) getCombinedItemHandler();
        }
        if(capability == CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }

    public EnumDyeColor getFridgeColor() {
        return fridgeColor;
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    public IItemHandler getCombinedItemHandler() {
        TileFridge baseFridge = getBaseFridge();
        TileFridge neighbourFridge;
        if(baseFridge == this) {
            neighbourFridge = findNeighbourFridge();
        } else {
            neighbourFridge = this;
        }
        if (neighbourFridge != null) {
            return new CombinedInvWrapper(neighbourFridge.itemHandler, baseFridge.itemHandler);
        }
        return itemHandler;
    }
}