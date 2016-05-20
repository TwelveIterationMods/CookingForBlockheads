package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.CookingConfig;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class TileSink extends TileEntity implements IFluidHandler {

    private static class WaterTank extends FluidTank {
        public WaterTank(int capacity) {
            super(capacity);
        }
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if(resource.getFluid() != FluidRegistry.WATER) {
                return 0;
            }
            return super.fill(resource, doFill);
        }
    }

    private static class SinkItemProvider implements IKitchenItemProvider {
        private final List<ItemStack> itemStacks = Lists.newArrayList();
        private final FluidTank fluidTank;
        private int waterUsed;

        public SinkItemProvider(FluidTank fluidTank) {
            this.fluidTank = fluidTank;
            itemStacks.add(new ItemStack(Items.WATER_BUCKET));
            Item pamsWaterItem = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "freshwaterItem")); // TODO apify
            if(pamsWaterItem != null) {
                itemStacks.add(new ItemStack(pamsWaterItem));
            }
        }

        @Override
        public void resetSimulation() {
            waterUsed = 0;
        }

        @Override
        public ItemStack useItemStack(int slot, int amount, boolean simulate) {
            if(fluidTank.getFluidAmount() - waterUsed > amount * 1000) {
                if(simulate) {
                    waterUsed += amount * 1000;
                } else {
                    fluidTank.drain(amount * 1000, true);
                }
                return ItemHandlerHelper.copyStackWithSize(getStackInSlot(slot), amount);
            }
            return null;
        }

        @Override
        public ItemStack returnItemStack(ItemStack itemStack) {
            for (ItemStack providedStack : itemStacks) {
                if (ItemHandlerHelper.canItemStacksStackRelaxed(itemStack, providedStack)) {
                    fluidTank.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
                    break;
                }
            }
            return null;
        }

        @Override
        public int getSlots() {
            return itemStacks.size();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return itemStacks.get(slot);
        }
    }

    private final FluidTank waterTank = new WaterTank(16000);
    private final SinkItemProvider itemProvider = new SinkItemProvider(waterTank);

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
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        waterTank.readFromNBT(tagCompound);
    }

    public int getWaterAmount() {
        return waterTank.getFluidAmount();
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return waterTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return waterTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return CookingConfig.sinkRequiresWater && fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return CookingConfig.sinkRequiresWater && fluid == FluidRegistry.WATER;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] { waterTank.getInfo() };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY) {
            return (T) itemProvider;
        }
        return super.getCapability(capability, facing);
    }
}
