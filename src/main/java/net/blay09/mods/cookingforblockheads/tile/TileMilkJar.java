package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileMilkJar extends TileEntity { // TODO test milk fluid handler

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
					tileMilkJar.drain(amount * 1000, true);
				}
				return ItemHandlerHelper.copyStackWithSize(itemStacks.get(slot), amount);
			}
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack returnItemStack(ItemStack itemStack) {
			for (ItemStack providedStack : itemStacks) {
				if (ItemHandlerHelper.canItemStacksStackRelaxed(itemStack, providedStack)) {
					tileMilkJar.fill(1000, true);
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

	private final MilkJarItemProvider itemProvider = new MilkJarItemProvider(this);

	private final IFluidHandler milkFluidHandler = new IFluidHandler() {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			Fluid milkFluid = Compat.getMilkFluid();
			if (milkFluid != null) {
				return new IFluidTankProperties[]{
						new FluidTankProperties(new FluidStack(milkFluid, (int) milkAmount), MILK_CAPACITY)
				};
			}
			return new IFluidTankProperties[0];
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource.getFluid() == Compat.getMilkFluid()) {
				return TileMilkJar.this.fill(resource.amount, doFill);
			}
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			Fluid milkFluid = Compat.getMilkFluid();
			if (milkFluid != null && resource.getFluid() == milkFluid) {
				return new FluidStack(milkFluid, TileMilkJar.this.drain(resource.amount, doDrain));
			}
			return null;
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			Fluid milkFluid = Compat.getMilkFluid();
			if (milkFluid != null) {
				return new FluidStack(milkFluid, TileMilkJar.this.drain(maxDrain, doDrain));
			}
			return null;
		}
	};

	protected float milkAmount;

	public int fill(int amount, boolean doFill) {
		int filled = (int) Math.min(MILK_CAPACITY - milkAmount, amount);
		if (doFill) {
			milkAmount += filled;
			VanillaPacketHandler.sendTileEntityUpdate(this);
		}
		return filled;
	}

	public int drain(int amount, boolean doDrain) {
		int drained = (int) Math.min(milkAmount, amount);
		if(doDrain) {
			milkAmount -= drained;
			VanillaPacketHandler.sendTileEntityUpdate(this);
		}
		return drained;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setFloat("MilkAmount", milkAmount);
		return tagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		milkAmount = tagCompound.getFloat("MilkAmount");
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	public float getMilkAmount() {
		return milkAmount;
	}

	public float getMilkCapacity() {
		return MILK_CAPACITY;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityKitchenItemProvider.CAPABILITY
				|| (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && Compat.getMilkFluid() != null)
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityKitchenItemProvider.CAPABILITY) {
			return (T) itemProvider;
		} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && Compat.getMilkFluid() != null) {
			return (T) milkFluidHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

}
