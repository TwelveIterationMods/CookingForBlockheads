package net.blay09.mods.cookingforblockheads.block;

import com.google.common.base.Predicate;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockKitchen extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
		@Override
		public boolean apply(@Nullable EnumFacing input) {
			return input != EnumFacing.DOWN && input != EnumFacing.UP;
		}
	});

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.975, 0.9375);

	protected BlockKitchen(Material material) {
		super(material);
		setCreativeTab(CookingForBlockheads.creativeTab);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		super.setBlockBoundsBasedOnState(worldIn, pos);
		setBlockBounds((float) BOUNDING_BOX.minX, (float) BOUNDING_BOX.minY, (float) BOUNDING_BOX.minZ, (float) BOUNDING_BOX.maxX, (float) BOUNDING_BOX.maxY, (float) BOUNDING_BOX.maxZ);
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		int facing = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch (facing) {
			case 0:
				world.setBlockState(pos, state.withProperty(FACING, EnumFacing.NORTH));
				break;
			case 1:
				world.setBlockState(pos, state.withProperty(FACING, EnumFacing.EAST));
				break;
			case 2:
				world.setBlockState(pos, state.withProperty(FACING, EnumFacing.SOUTH));
				break;
			case 3:
				world.setBlockState(pos, state.withProperty(FACING, EnumFacing.WEST));
				break;
		}
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add(EnumChatFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":multiblockKitchen"));
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemModelMesher mesher) {
		mesher.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
