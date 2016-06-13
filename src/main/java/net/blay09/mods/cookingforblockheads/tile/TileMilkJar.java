package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class TileMilkJar extends TileEntity {

	protected static final int MILK_CAPACITY = 8000;

	private static class MilkJarItemProvider implements IKitchenItemProvider {
		private final List<ItemStack> itemStacks = Lists.newArrayList();
		private final TileMilkJar tileMilkJar;
		private int milkUsed;

		public MilkJarItemProvider(TileMilkJar tileMilkJar) {
			this.tileMilkJar = tileMilkJar;
			itemStacks.add(new ItemStack(Items.MILK_BUCKET));
			Item pamsMilkItem = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", "freshmilkItem")); // TODO apify
			if(pamsMilkItem != null) {
				itemStacks.add(new ItemStack(pamsMilkItem));
			}
		}

		@Override
		public void resetSimulation() {
			milkUsed = 0;
		}

		@Override
		public ItemStack useItemStack(int slot, int amount, boolean simulate) {
			if(tileMilkJar.getMilkAmount() - milkUsed > amount * 1000) {
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
		public ItemStack getStackInSlot(int slot) {
			return itemStacks.get(slot);
		}
	}

	private final MilkJarItemProvider itemProvider = new MilkJarItemProvider(this);
	protected float milkAmount;

	public void fill(int amount) {
		milkAmount = Math.min(MILK_CAPACITY, milkAmount + amount);
	}

	public void drain(int amount) {
		milkAmount = Math.max(0, milkAmount - amount);
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
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, new NBTTagCompound());
	}

	public float getMilkAmount() {
		return milkAmount;
	}

	public float getMilkCapacity() {
		return MILK_CAPACITY;
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
