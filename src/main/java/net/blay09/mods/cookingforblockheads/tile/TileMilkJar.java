package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.fluid.Fluid;
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
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileMilkJar extends TileEntity {

    protected static final int MILK_CAPACITY = 8000;

    private static class MilkJarItemProvider extends DefaultKitchenItemProvider {
        private final NonNullList<ItemStack> itemStacks = NonNullList.create();
        private final TileMilkJar tileMilkJar;
        private int milkUsed;

        public MilkJarItemProvider(TileMilkJar tileMilkJar) {
            this.tileMilkJar = tileMilkJar;
            itemStacks.addAll(CookingRegistry.getMilkItems());
        }

        @Override
        public void resetSimulation() {
            milkUsed = 0;
        }

        @Override
        public int getSimulatedUseCount(int slot) {
            return milkUsed / 1000;
        }

        @Override
        public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
            if (tileMilkJar.getMilkAmount() - milkUsed >= amount * 1000) {
                if (requireBucket && itemStacks.get(slot).getItem() == Items.MILK_BUCKET) {
                    if (!CookingRegistry.consumeBucket(inventories, simulate)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (simulate) {
                    milkUsed += amount * 1000;
                } else {
                    tileMilkJar.drain(amount * 1000, IFluidHandler.FluidAction.EXECUTE);
                }
                return ItemHandlerHelper.copyStackWithSize(itemStacks.get(slot), amount);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
            for (ItemStack providedStack : itemStacks) {
                if (ItemHandlerHelper.canItemStacksStackRelaxed(itemStack, providedStack)) {
                    tileMilkJar.fill(1000, IFluidHandler.FluidAction.EXECUTE);
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
            if (tileMilkJar.getMilkAmount() - milkUsed < 1000) {
                return ItemStack.EMPTY;
            }

            return itemStacks.get(slot);
        }
    }

    private final MilkJarItemProvider itemProvider = new MilkJarItemProvider(this);

    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            Fluid milkFluid = Compat.getMilkFluid();
            return milkFluid != null ? new FluidStack(milkFluid, (int) milkAmount) : FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return MILK_CAPACITY;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.getFluid() == Compat.getMilkFluid()) {
                return TileMilkJar.this.fill(resource.getAmount(), action);
            }
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            Fluid milkFluid = Compat.getMilkFluid();
            if (milkFluid != null && resource.getFluid() == milkFluid) {
                return new FluidStack(milkFluid, TileMilkJar.this.drain(resource.getAmount(), action));
            }

            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            Fluid milkFluid = Compat.getMilkFluid();
            if (milkFluid != null) {
                return new FluidStack(milkFluid, TileMilkJar.this.drain(maxDrain, action));
            }

            return FluidStack.EMPTY;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return stack.getFluid() == Compat.getMilkFluid();
        }
    };

    private final LazyOptional<IFluidHandler> fluidHandlerCap = LazyOptional.of(() -> fluidHandler);
    private final LazyOptional<IKitchenItemProvider> itemProviderCap = LazyOptional.of(() -> itemProvider);

    protected float milkAmount;

    public TileMilkJar() {
        super(ModTileEntities.milkJar);
    }

    public int fill(int amount, IFluidHandler.FluidAction action) {
        int filled = (int) Math.min(MILK_CAPACITY - milkAmount, amount);
        if (action == IFluidHandler.FluidAction.EXECUTE) {
            milkAmount += filled;
            VanillaPacketHandler.sendTileEntityUpdate(this);
        }
        return filled;
    }

    public int drain(int amount, IFluidHandler.FluidAction action) {
        int drained = (int) Math.min(milkAmount, amount);
        if (action == IFluidHandler.FluidAction.EXECUTE) {
            milkAmount -= drained;
            VanillaPacketHandler.sendTileEntityUpdate(this);
        }
        return drained;
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.putFloat("MilkAmount", milkAmount);
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        milkAmount = tagCompound.getFloat("MilkAmount");
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

    public float getMilkAmount() {
        return milkAmount;
    }

    public float getMilkCapacity() {
        return MILK_CAPACITY;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        if (!result.isPresent() && Compat.getMilkFluid() != null) {
            result = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(capability, fluidHandlerCap);
        }

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }

}
