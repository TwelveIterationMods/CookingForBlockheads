package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.util.IDyeableKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Items;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileSink extends TileEntity implements IDyeableKitchen {

    private static class WaterTank extends FluidTank {

        public WaterTank(int capacity) {
            super(capacity);
        }

        private static final FluidStack MAX_WATER = new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE);

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
        public int fill(FluidStack resource, boolean doFill) {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get() || resource.getFluid() != FluidRegistry.WATER) {
                return resource.amount;
            }

            return super.fill(resource, doFill);
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get() && resource.getFluid() == FluidRegistry.WATER) {
                return resource.copy();
            }

            return super.drain(resource, doDrain);
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                return new FluidStack(FluidRegistry.WATER, maxDrain);
            }

            return super.drain(maxDrain, doDrain);
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
            if (!CookingForBlockheadsConfig.general.sinkRequiresWater || fluidTank.getFluidAmount() - waterUsed > amount * 1000) {
                if (requireBucket && itemStacks.get(slot).getItem() == Items.MILK_BUCKET) {
                    if (!CookingRegistry.consumeBucket(inventories, simulate)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (simulate) {
                    waterUsed += amount * 1000;
                } else {
                    fluidTank.drain(amount * 1000, true);
                }
                return ItemHandlerHelper.copyStackWithSize(itemStacks.get(slot), amount);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
            for (ItemStack providedStack : itemStacks) {
                if (ItemHandlerHelper.canItemStacksStackRelaxed(itemStack, providedStack)) {
                    fluidTank.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
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

    private final FluidTank waterTank = new WaterTank(16000);
    private final SinkItemProvider itemProvider = new SinkItemProvider(waterTank);

    private DyeColor color = DyeColor.WHITE;

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
        waterTank.write(tagCompound);
        tagCompound.putByte("Color", (byte) color.getId());
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        waterTank.read(tagCompound);
        color = DyeColor.byId(tagCompound.getByte("Color"));
    }

    public int getWaterAmount() {
        return waterTank.getFluidAmount();
    }

    public int getWaterCapacity() {
        return waterTank.getCapacity();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        if (!result.isPresent()) {
            result = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(capability, waterTankCap);
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
