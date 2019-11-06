package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.util.IDyeableKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SinkTileEntity extends TileEntity implements IDyeableKitchen {

    private static class WaterTank extends FluidTank {

        public WaterTank(int capacity) {
            super(capacity);
        }

        private static final FluidStack MAX_WATER = new FluidStack(Fluids.WATER, Integer.MAX_VALUE);

        @Override
        public FluidStack getFluid() {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                return MAX_WATER;
            }

            return super.getFluid();
        }

        @Override
        public int getFluidAmount() {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                return Integer.MAX_VALUE;
            }

            return super.getFluidAmount();
        }

        @Override
        public int getCapacity() {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                return Integer.MAX_VALUE;
            }

            return super.getCapacity();
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get() && resource.getFluid() == Fluids.WATER) {
                return resource.copy();
            }

            return super.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                return new FluidStack(Fluids.WATER, maxDrain);
            }

            return super.drain(maxDrain, action);
        }

    }

    private static class SinkItemProvider extends DefaultKitchenItemProvider {
        private final NonNullList<ItemStack> itemStacks = NonNullList.create();
        private final FluidTank fluidTank;
        private int waterUsed;

        public SinkItemProvider(FluidTank fluidTank) {
            this.fluidTank = fluidTank;
            itemStacks.addAll(CookingRegistry.getWaterItems());
        }

        @Override
        public void resetSimulation() {
            waterUsed = 0;
        }

        @Override
        public int getSimulatedUseCount(int slot) {
            return waterUsed / 1000;
        }

        @Override
        public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get() || fluidTank.getFluidAmount() - waterUsed > amount * 1000) {
                if (requireBucket && itemStacks.get(slot).getItem() == Items.MILK_BUCKET) {
                    if (!CookingRegistry.consumeBucket(inventories, simulate)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (simulate) {
                    waterUsed += amount * 1000;
                } else {
                    fluidTank.drain(amount * 1000, IFluidHandler.FluidAction.EXECUTE);
                }
                return ItemHandlerHelper.copyStackWithSize(itemStacks.get(slot), amount);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
            for (ItemStack providedStack : itemStacks) {
                if (ItemHandlerHelper.canItemStacksStackRelaxed(itemStack, providedStack)) {
                    fluidTank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                    break;
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public int getSlots() {
            return itemStacks.size();
        }

        @Override
        @Nonnull
        public ItemStack getStackInSlot(int slot) {
            if (CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get() && fluidTank.getFluidAmount() - waterUsed < 1000) {
                return ItemStack.EMPTY;
            }

            return itemStacks.get(slot);
        }
    }

    private final FluidTank fluidHandler = new WaterTank(16000);
    private final SinkItemProvider itemProvider = new SinkItemProvider(fluidHandler);

    private final LazyOptional<IFluidHandler> fluidHandlerCap = LazyOptional.of(() -> fluidHandler);
    private final LazyOptional<IKitchenItemProvider> itemProviderCap = LazyOptional.of(() -> itemProvider);

    private DyeColor color = DyeColor.WHITE;

    public SinkTileEntity() {
        super(ModTileEntities.sink);
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
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        fluidHandler.writeToNBT(tagCompound);
        tagCompound.putByte("Color", (byte) color.getId());
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        fluidHandler.readFromNBT(tagCompound);
        color = DyeColor.byId(tagCompound.getByte("Color"));
    }

    public int getWaterAmount() {
        return fluidHandler.getFluidAmount();
    }

    public int getWaterCapacity() {
        return fluidHandler.getCapacity();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        if (!result.isPresent()) {
            result = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(capability, fluidHandlerCap);
        }

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public DyeColor getDyedColor() {
        return color;
    }

    @Override
    public void setDyedColor(DyeColor color) {
        this.color = color;
        BlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, world.getChunkAt(pos), state, state, 3);
        markDirty();
    }
}
