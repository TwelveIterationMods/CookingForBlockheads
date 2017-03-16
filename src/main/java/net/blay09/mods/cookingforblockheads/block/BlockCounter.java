package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.blaycommon.ItemUtils;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Locale;

public class BlockCounter extends BlockKitchen {

	public enum ModelPass implements IStringSerializable {
		STATIC,
		DOOR,
		DOOR_FLIPPED;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}

	public static final PropertyEnum<ModelPass> PASS = PropertyEnum.create("pass", ModelPass.class);

	public BlockCounter() {
		super(Material.WOOD);

		setRegistryName(CookingForBlockheads.MOD_ID, "counter");
		setUnlocalizedName(getRegistryNameString());
		setSoundType(SoundType.WOOD);
		setHardness(5f);
		setResistance(10f);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, FLIPPED, PASS, COLOR);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileCounter) {
			return state.withProperty(COLOR, ((TileCounter) tileEntity).getColor());
		}
		return state;
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty() && heldItem.getItem() == Items.DYE) {
			if (recolorBlock(world, pos, facing, EnumDyeColor.byDyeDamage(heldItem.getItemDamage()))) {
				heldItem.shrink(1);
			}
			return true;
		}
		if(facing == state.getValue(FACING)) {
			TileCounter tileCounter = (TileCounter) world.getTileEntity(pos);
			if(tileCounter != null) {
				if (player.isSneaking()) {
					tileCounter.getDoorAnimator().toggleForcedOpen();
					return true;
				} else if (!heldItem.isEmpty() && tileCounter.getDoorAnimator().isForcedOpen()) {
					heldItem = ItemHandlerHelper.insertItemStacked(tileCounter.getItemHandler(), heldItem, false);
					player.setHeldItem(hand, heldItem);
					return true;
				}
			}
		}
		if (!world.isRemote) {
			if(facing == EnumFacing.UP && !heldItem.isEmpty()) {
				return false;
			}
			player.openGui(CookingForBlockheads.instance, GuiHandler.COUNTER, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer);
		return state.withProperty(FLIPPED, shouldbePlacedFlipped(pos, state.getValue(FACING), placer));
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

	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileCounter) {
			TileCounter tileCounter = (TileCounter) tileEntity;
			tileCounter.setColor(color);
		}
		return true;
	}

}
