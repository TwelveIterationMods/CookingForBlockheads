package net.blay09.mods.cookingforblockheads.block;

import com.google.common.base.Predicate;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	@SuppressWarnings("deprecation")
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
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(state.getValue(FACING).getAxis() == EnumFacing.Axis.X) {
			return new AxisAlignedBB(0.03125, 0, 0, 0.96875, 0.9375, 1);
		} else {
			return new AxisAlignedBB(0, 0, 0.03125, 1, 0.9375, 0.96875);
		}
//		return FULL_BLOCK_AABB;
//		return BOUNDING_BOX;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		int facing = MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":multiblock_kitchen"));
	}

	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryNameString(), "inventory"));
	}

	public String getRegistryNameString() {
		//noinspection ConstantConditions
		return getRegistryName().toString();
	}
}
