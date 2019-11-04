package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.ModSounds;
import net.blay09.mods.cookingforblockheads.api.ToasterHandler;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.ToasterBlock;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class ToasterTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int TOAST_TICKS = 1200;

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
            VanillaPacketHandler.sendTileEntityUpdate(ToasterTileEntity.this);
        }
    };

    private boolean active;
    private int toastTicks;

    public ToasterTileEntity() {
        super(ModTileEntities.toaster);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 0) {
            world.playSound(null, pos, ModSounds.toasterStart, SoundCategory.BLOCKS, 1f, 1f);
            return true;
        } else if (id == 1) {
            world.playSound(null, pos, ModSounds.toasterStop, SoundCategory.BLOCKS, 1f, 1f);
            return true;
        } else if (id == 2) {
            BlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkAt(pos), state, state, 3);
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        active = tagCompound.getBoolean("Active");
        toastTicks = tagCompound.getInt("ToastTicks");
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        tagCompound.putBoolean("Active", active);
        tagCompound.putInt("ToastTicks", toastTicks);
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void tick() {
        if (active) {
            toastTicks--;
            if (toastTicks <= 0 && !world.isRemote) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack inputStack = itemHandler.getStackInSlot(i);
                    if (!inputStack.isEmpty()) {
                        ToasterHandler toastHandler = CookingRegistry.getToasterHandler(inputStack);
                        ItemStack outputStack = toastHandler.getToasterOutput(inputStack);
                        if (outputStack.isEmpty()) {
                            outputStack = inputStack;
                        } else {
                            outputStack = outputStack.copy();
                        }
                        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.75f, pos.getZ() + 0.5f, outputStack);
                        itemEntity.setMotion(0f, 0.1f, 0f);
                        world.addEntity(itemEntity);
                        itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
                setActive(false);
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            toastTicks = TOAST_TICKS;
            world.addBlockEvent(pos, ModBlocks.toaster, 0, 0);
        } else {
            toastTicks = 0;
            world.addBlockEvent(pos, ModBlocks.toaster, 1, 0);
        }

        world.addBlockEvent(pos, ModBlocks.toaster, 2, 0);

        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(ToasterBlock.ACTIVE, active));
        markDirty();
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
