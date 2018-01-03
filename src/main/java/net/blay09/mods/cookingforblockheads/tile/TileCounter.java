package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.BlockCounter;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.quark.api.IDropoffManager;

import javax.annotation.Nullable;

@Optional.Interface(modid = Compat.QUARK, iface = "vazkii.quark.api.IDropoffManager", striprefs = true)
public class TileCounter extends TileEntity implements ITickable, IDropoffManager {

	private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
		@Override
		protected void onContentsChanged(int slot) {
			isDirty = true;
			markDirty();
		}
	};
	private final KitchenItemProvider itemProvider = new KitchenItemProvider(itemHandler);
	private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

	private boolean isFirstTick = true;

	private boolean isDirty;

	private EnumDyeColor color = EnumDyeColor.WHITE;

	private EnumFacing cachedFacing;
	private boolean cachedFlipped;

	public TileCounter() {
		doorAnimator.setOpenRadius(2);
		doorAnimator.setSoundEventOpen(SoundEvents.BLOCK_CHEST_OPEN);
		doorAnimator.setSoundEventClose(SoundEvents.BLOCK_CHEST_CLOSE);
	}

	@Override
	public void update() {
		if (isFirstTick) {
			// onLoad doesn't work when you need to touch the world TODO I think this was fixed in a newer Forge build?
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == ModBlocks.counter) { // looks like there's an issue here similar to TESRs where the state doesn't match the tile
				cachedFacing = state.getValue(BlockCounter.FACING);
				cachedFlipped = state.getValue(BlockCounter.FLIPPED);
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
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
		color = EnumDyeColor.byDyeDamage(tagCompound.getByte("Color"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
		tagCompound.setByte("Color", (byte) color.getDyeDamage());
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
		doorAnimator.setForcedOpen(pkt.getNbtCompound().getBoolean("IsForcedOpen"));
		doorAnimator.setNumPlayersUsing(pkt.getNbtCompound().getByte("NumPlayersUsing"));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		tagCompound.setBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
		tagCompound.setByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
		return tagCompound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	public IItemHandler getItemHandler() {
		return itemHandler;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityKitchenItemProvider.CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		}
		if (capability == CapabilityKitchenItemProvider.CAPABILITY) {
			return (T) itemProvider;
		}
		return super.getCapability(capability, facing);
	}

	public DoorAnimator getDoorAnimator() {
		return doorAnimator;
	}

	public void setColor(EnumDyeColor color) {
		this.color = color;
		IBlockState state = world.getBlockState(pos);
		world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
		markDirty();
	}

	public EnumDyeColor getColor() {
		return color;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (oldState.getBlock() == newState.getBlock()) {
			cachedFacing = newState.getValue(BlockCounter.FACING);
			cachedFlipped = newState.getValue(BlockCounter.FLIPPED);
			return false;
		}
		return true;
	}

	public EnumFacing getFacing() {
		if (cachedFacing == null) {
			return EnumFacing.NORTH;
		}
		return cachedFacing;
	}

	public boolean isFlipped() {
		return cachedFlipped;
	}

	@Override
	public boolean acceptsDropoff(EntityPlayer entityPlayer) {
		return true;
	}

}