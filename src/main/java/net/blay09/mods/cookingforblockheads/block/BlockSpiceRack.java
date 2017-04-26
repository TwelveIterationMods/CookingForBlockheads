package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileSpiceRack;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSpiceRack extends BlockKitchen {

	private static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {
			new AxisAlignedBB(0, 0.25, 1 - 0.125, 1, 1, 1),
			new AxisAlignedBB(0, 0.25, 0, 1, 1, 0.125),
			new AxisAlignedBB(1 - 0.125, 0.25, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0.25, 0, 0.125, 1, 1),
	};

    public BlockSpiceRack() {
        super(Material.WOOD);

		setRegistryName(CookingForBlockheads.MOD_ID, "spiceRack");
		setUnlocalizedName(getRegistryName().toString());
        setSoundType(SoundType.WOOD);
        setHardness(2.5f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileSpiceRack();
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		return BOUNDING_BOXES[facing.getIndex() - 2];
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.isSideSolid(pos.offset(EnumFacing.WEST), EnumFacing.EAST) ||
				world.isSideSolid(pos.offset(EnumFacing.EAST), EnumFacing.WEST) ||
				world.isSideSolid(pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH) ||
				world.isSideSolid(pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			facing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(hand != EnumHand.MAIN_HAND) {
    		return true;
		}
        if(heldItem != null && heldItem.getItem() instanceof ItemBlock) {
            return true;
        }
		if(heldItem != null || player.isSneaking()) {
			float hit = hitX;
			EnumFacing facing = state.getValue(FACING);
            switch(facing) {
                case NORTH: hit = hitX; break;
                case SOUTH: hit = 1f - hitX; break;
                case WEST: hit = 1f - hitZ; break;
                case EAST: hit = hitZ; break;
            }
			int hitSlot = (int) ((1f - hit) * 9);
			TileSpiceRack tileSpiceRack = (TileSpiceRack) world.getTileEntity(pos);
			if(tileSpiceRack != null) {
				if(heldItem != null) {
					ItemStack oldToolItem = tileSpiceRack.getItemHandler().getStackInSlot(hitSlot);
                    ItemStack toolItem = heldItem.splitStack(1);
                    if (oldToolItem != null) {
                        if (!player.inventory.addItemStackToInventory(oldToolItem)) {
                            player.dropItem(oldToolItem, false);
                        }
						tileSpiceRack.getItemHandler().setStackInSlot(hitSlot, toolItem);
                    } else {
						tileSpiceRack.getItemHandler().setStackInSlot(hitSlot, toolItem);
                    }
					if(heldItem.stackSize == 0) {
						player.setHeldItem(hand, null);
					}
				} else {
					ItemStack itemStack = tileSpiceRack.getItemHandler().getStackInSlot(hitSlot);
                    if (itemStack != null) {
						tileSpiceRack.getItemHandler().setStackInSlot(hitSlot, null);
						player.setHeldItem(hand, itemStack);
                    }
				}
			}
		} else {
			player.openGui(CookingForBlockheads.instance, GuiHandler.SPICE_RACK, world, pos.getX(), pos.getY(), pos.getZ());
		}
        return true;
    }

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileSpiceRack tileEntity = (TileSpiceRack) world.getTileEntity(pos);
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