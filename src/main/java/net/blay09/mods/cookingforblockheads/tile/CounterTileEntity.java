package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.KitchenCounterBlock;
import net.blay09.mods.cookingforblockheads.container.CounterContainer;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.tile.util.DoorAnimator;
import net.blay09.mods.cookingforblockheads.tile.util.IDyeableKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CounterTileEntity extends TileEntity implements ITickableTileEntity, IDyeableKitchen, INamedContainerProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(CookingForBlockheadsConfig.COMMON.largeCounters.get() ? 54 : 27) {
        @Override
        protected void onContentsChanged(int slot) {
            isDirty = true;
            markDirty();
        }
    };

    private final KitchenItemProvider itemProvider = new KitchenItemProvider(itemHandler);
    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IKitchenItemProvider> itemProviderCap = LazyOptional.of(() -> itemProvider);

    private boolean isFirstTick = true;

    private boolean isDirty;

    private DyeColor color = DyeColor.WHITE;

    private Direction cachedFacing;
    private boolean cachedFlipped;

    public CounterTileEntity() {
        this(ModTileEntities.counter);
    }

    public CounterTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        doorAnimator.setOpenRadius(2);
        doorAnimator.setSoundEventOpen(SoundEvents.BLOCK_CHEST_OPEN);
        doorAnimator.setSoundEventClose(SoundEvents.BLOCK_CHEST_CLOSE);
    }

    @Override
    public void tick() {
        if (isFirstTick) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof KitchenCounterBlock) { // looks like there's an issue here similar to TESRs where the state doesn't match the tile
                cachedFacing = state.get(KitchenCounterBlock.FACING);
                cachedFlipped = state.get(KitchenCounterBlock.FLIPPED);
                isFirstTick = false;
            }
        }

        doorAnimator.update();

        if (isDirty) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            isDirty = false;
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        CompoundNBT itemHandlerCompound = tagCompound.getCompound("ItemHandler");
        if (CookingForBlockheadsConfig.COMMON.largeCounters.get() && itemHandlerCompound.getInt("Size") < 54) {
            itemHandlerCompound.putInt("Size", 54);
        }

        itemHandler.deserializeNBT(itemHandlerCompound);

        color = DyeColor.byId(tagCompound.getByte("Color"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        tagCompound.putByte("Color", (byte) color.getId());
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
        doorAnimator.setForcedOpen(pkt.getNbtCompound().getBoolean("IsForcedOpen"));
        doorAnimator.setNumPlayersUsing(pkt.getNbtCompound().getByte("NumPlayersUsing"));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tagCompound = new CompoundNBT();
        write(tagCompound);
        tagCompound.putBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
        tagCompound.putByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        return tagCompound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, itemHandlerCap);
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        }

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    @Override
    public void setDyedColor(DyeColor color) {
        this.color = color;
        BlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, world.getChunkAt(pos), state, state, 3);
        markDirty();
    }

    @Override
    public DyeColor getDyedColor() {
        return color;
    }

    public Direction getFacing() {
        return cachedFacing == null ? Direction.NORTH : cachedFacing;
    }

    public boolean isFlipped() {
        return cachedFlipped;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cookingforblockheads.counter");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CounterContainer(i, playerInventory, this);
    }
}
