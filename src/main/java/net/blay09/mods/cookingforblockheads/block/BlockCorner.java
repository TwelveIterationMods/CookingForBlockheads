package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.TileCorner;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockCorner extends BlockKitchen {

	private static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {
			new AxisAlignedBB(0, 0, 0, 0.96875, 0.9375, 0.96875),
			new AxisAlignedBB(0.03125, 0, 0.03125, 1, 0.9375, 1),
			new AxisAlignedBB(0, 0, 0.03125, 0.96875, 0.9375, 1),
			new AxisAlignedBB(0.03125, 0, 0, 1, 0.9375, 0.96875)
	};

	public BlockCorner() {
		super(Material.ROCK);

		setRegistryName(CookingForBlockheads.MOD_ID, "corner");
		setUnlocalizedName(getRegistryNameString());
		setSoundType(SoundType.STONE);
		setHardness(5f);
		setResistance(10f);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, COLOR);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileCorner) {
			return state.withProperty(COLOR, ((TileCorner) tileEntity).getColor());
		}
		return state;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty() && heldItem.getItem() == Items.DYE) {
			if (recolorBlock(world, pos, facing, EnumDyeColor.byDyeDamage(heldItem.getItemDamage()))) {
				heldItem.shrink(1);
			}
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOXES[state.getValue(FACING).getIndex() - 2];
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if(facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileCorner();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
			tooltip.add(TextFormatting.GRAY + s);
		}
	}

	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileCorner) {
			((TileCorner) tileEntity).setColor(color);
		}
		return true;
	}

}
