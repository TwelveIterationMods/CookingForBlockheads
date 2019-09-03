package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenConnector;
import net.blay09.mods.cookingforblockheads.tile.util.IDyeableKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CornerTileEntity extends TileEntity implements IDyeableKitchen {

    @SuppressWarnings("NullableProblems")
    private final LazyOptional<CapabilityKitchenConnector.IKitchenConnector> kitchenConnectorCap = LazyOptional.of(CapabilityKitchenConnector.CAPABILITY::getDefaultInstance);

    private DyeColor color = DyeColor.WHITE;

    public CornerTileEntity() {
        super(ModTileEntities.corner);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityKitchenConnector.CAPABILITY.orEmpty(cap, kitchenConnectorCap);
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.putByte("Color", (byte) color.getId());
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        color = DyeColor.byId(tagCompound.getByte("Color"));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tagCompound = new CompoundNBT();
        write(tagCompound);
        return tagCompound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
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

}
