package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class TileMilkJar extends TileEntity implements IFluidHandler {

	protected static final int MILK_CAPACITY = 8000;

	private static class MilkJarItemProvider implements IKitchenItemProvider {
		private final List<ItemStack> itemStacks = Lists.newArrayList();
		private final TileMilkJar tileMilkJar;
		private int milkUsed;

		public MilkJarItemProvider(TileMilkJar tileMilkJar) {
			this.tileMilkJar = tileMilkJar;
			itemStacks.add(new ItemStack(Items.MILK_BUCKET));
			itemStacks.addAll(CookingRegistry.getMilkItems());
		}

		@Override
		public void resetSimulation() {
			milkUsed = 0;
		}

		@Override
		public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
			if(tileMilkJar.getMilkAmount() - milkUsed >= amount * 1000) {
				if(requireBucket && getStackInSlot(slot).getItem() == Items.MILK_BUCKET) {
					if(!CookingRegistry.consumeBucket(inventories, simulate)) {
						return null;
					}
				}
				if(simulate) {
					milkUsed += amount * 1000;
				} else {
					tileMilkJar.drain(amount * 1000);
				}
				return ItemHandlerHelper.copyStackWithSize(getStackInSlot(slot), amount);
			}
			return null;
		}

		@Override
		public ItemStack returnItemStack(ItemStack itemStack) {
			for (ItemStack providedStack : itemStacks) {
				if (ItemHandlerHelper.canItemStacksStackRelaxed(itemStack, providedStack)) {
					tileMilkJar.fill(1000);
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
		@Nonnull
		public ItemStack getStackInSlot(int slot) {
			return itemStacks.get(slot);
		}
	}

	private final MilkJarItemProvider itemProvider = new MilkJarItemProvider(this);
	protected float milkAmount;

	public int fill(int amount) {
		
		int filled = amount;
		if (filled > MILK_CAPACITY - milkAmount)
		{
			filled = (int) (MILK_CAPACITY - milkAmount);
		}

		milkAmount = Math.min(MILK_CAPACITY, milkAmount + amount);
		VanillaPacketHandler.sendTileEntityUpdate(this);
		
		return filled;
	}

	public int drain(int amount) {
		
		int drained = amount;
		if (milkAmount < drained)
		{
			drained = (int) milkAmount;
		}
		
		milkAmount = Math.max(0, milkAmount - amount);
		VanillaPacketHandler.sendTileEntityUpdate(this);
		
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityKitchenItemProvider.CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityKitchenItemProvider.CAPABILITY) {
			return (T) itemProvider;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		
		if (FluidRegistry.getFluid("milk") == null || resource.getFluid().getName() != "milk")
		{
			return 0;
		}
		
		if (doFill)
		{
			return fill(resource.amount);
		}
		else
		{
			return (int) (MILK_CAPACITY - milkAmount + resource.amount);
		}
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		
		if (FluidRegistry.getFluid("milk") == null || resource.getFluid().getName() != "milk")
		{
			return null;
		}
		
		if (doDrain)
		{
			return new FluidStack(resource.getFluid(), drain(resource.amount));
		}
		else
		{
			return new FluidStack(resource.getFluid(), (int) Math.min(milkAmount, resource.amount));
		}
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		
		if (FluidRegistry.getFluid("milk") == null)
		{
			return null;
		}
		
		if (doDrain)
		{
			return new FluidStack(FluidRegistry.getFluid("milk"), drain(maxDrain));
		}
		else
		{
			return new FluidStack(FluidRegistry.getFluid("milk"), (int) Math.min(milkAmount, maxDrain));
		}
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		
		return (FluidRegistry.getFluid("milk") != null);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		
		return (FluidRegistry.getFluid("milk") != null);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		
		return new FluidTankInfo[] { new FluidTankInfo(new FluidStack(FluidRegistry.getFluid("milk"), (int) milkAmount), MILK_CAPACITY) };
	}
}
