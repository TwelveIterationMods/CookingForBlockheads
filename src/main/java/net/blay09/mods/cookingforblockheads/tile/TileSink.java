package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.ModConfig;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
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
            if (!ModConfig.general.sinkRequiresWater) {
                return MAX_WATER;
            }

            return super.getFluid();
        }

        @Override
        public int getFluidAmount() {
            if (!ModConfig.general.sinkRequiresWater) {
                return Integer.MAX_VALUE;
            }

            return super.getFluidAmount();
        }

        @Override
        public int getCapacity() {
            if (!ModConfig.general.sinkRequiresWater) {
                return Integer.MAX_VALUE;
            }

            return super.getCapacity();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (!ModConfig.general.sinkRequiresWater || resource.getFluid() != FluidRegistry.WATER) {
                return resource.amount;
            }

            return super.fill(resource, doFill);
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (!ModConfig.general.sinkRequiresWater && resource.getFluid() == FluidRegistry.WATER) {
                return resource.copy();
            }

            return super.drain(resource, doDrain);
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (!ModConfig.general.sinkRequiresWater) {
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
            if (!ModConfig.general.sinkRequiresWater || fluidTank.getFluidAmount() - waterUsed > amount * 1000) {
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
        public ItemStack returnItemStack(ItemStack itemStack) {
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
            return itemStacks.get(slot);
        }
    }

    private final FluidTank waterTank = new WaterTank(16000);
    private final SinkItemProvider itemProvider = new SinkItemProvider(waterTank);

    private EnumDyeColor color = EnumDyeColor.WHITE;

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        waterTank.writeToNBT(tagCompound);
        tagCompound.setByte("Color", (byte) color.getDyeDamage());
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        waterTank.readFromNBT(tagCompound);
        color = EnumDyeColor.byDyeDamage(tagCompound.getByte("Color"));
    }

    public int getWaterAmount() {
        return waterTank.getFluidAmount();
    }

    public int getWaterCapacity() {
        return waterTank.getCapacity();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityKitchenItemProvider.CAPABILITY
                || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityKitchenItemProvider.CAPABILITY) {
            return (T) itemProvider;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) waterTank;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public EnumDyeColor getDyedColor() {
        return color;
    }

    @Override
    public void setDyedColor(EnumDyeColor color) {
        this.color = color;
        IBlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
        markDirty();
    }
}
