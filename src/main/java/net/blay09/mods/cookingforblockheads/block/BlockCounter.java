package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCounter extends BlockKitchen {

	public static final PropertyBool FLIPPED = PropertyBool.create("flipped");

	public BlockCounter() {
		super(Material.IRON);

		setRegistryName(CookingForBlockheads.MOD_ID, "counter");
		setUnlocalizedName(getRegistryName().toString());
		setSoundType(SoundType.METAL);
		setHardness(5f);
		setResistance(10f);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, FLIPPED);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing;
		switch (meta & 7) {
			case 0:
				facing = EnumFacing.EAST;
				break;
			case 1:
				facing = EnumFacing.WEST;
				break;
			case 2:
				facing = EnumFacing.SOUTH;
				break;
			case 3:
			default:
				facing = EnumFacing.NORTH;
				break;
		}
		return getDefaultState().withProperty(FACING, facing).withProperty(FLIPPED, (meta & 8) != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta;
		switch (state.getValue(FACING)) {
			case EAST:
				meta = 0;
				break;
			case WEST:
				meta = 1;
				break;
			case SOUTH:
				meta = 2;
				break;
			case NORTH:
			default:
				meta = 3;
				break;
		}
		if (state.getValue(FLIPPED)) {
			meta |= 8;
		}
		return meta;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileCounter();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(side == state.getValue(FACING)) {
			TileCounter tileCounter = (TileCounter) world.getTileEntity(pos);
			if(tileCounter != null) {
				if (player.isSneaking()) {
					tileCounter.getDoorAnimator().toggleForcedOpen();
					return true;
				} else if (heldItem != null && tileCounter.getDoorAnimator().isForcedOpen()) {
					heldItem = ItemHandlerHelper.insertItemStacked(tileCounter.getItemHandler(), heldItem, false);
					player.setHeldItem(hand, heldItem);
					return true;
				}
			}
		}
		if (!world.isRemote) {
			player.openGui(CookingForBlockheads.instance, GuiHandler.COUNTER, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		double blockRotation = (double) (placer.rotationYaw * 4f / 360f) + 0.5;
		boolean flipped = Math.abs(blockRotation - (int) blockRotation) < 0.5;
		super.onBlockPlacedBy(world, pos, state.withProperty(FLIPPED, !flipped), placer, stack);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileCounter tileEntity = (TileCounter) world.getTileEntity(pos);
		if (tileEntity != null) {
			ItemUtils.dropContent(world, pos, tileEntity.getItemHandler());
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
			tooltip.add(TextFormatting.GRAY + s);
		}
	}

}
