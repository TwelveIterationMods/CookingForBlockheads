package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.tile.IDyeableKitchen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockKitchen extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", input -> input != EnumFacing.DOWN && input != EnumFacing.UP);
    public static final PropertyBool LOWERED = PropertyBool.create("lowered");
    public static final PropertyBool FLIPPED = PropertyBool.create("flipped");
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    private static final AxisAlignedBB BOUNDING_BOX_X = new AxisAlignedBB(0.03125, 0, 0, 0.96875, 0.9375, 1);
    private static final AxisAlignedBB BOUNDING_BOX_Z = new AxisAlignedBB(0, 0, 0.03125, 1, 0.9375, 0.96875);

    protected BlockKitchen(Material material) {
        super(material);
        setCreativeTab(CookingForBlockheads.creativeTab);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(FACING).getAxis() == EnumFacing.Axis.X) {
            return BOUNDING_BOX_X;
        } else {
            return BOUNDING_BOX_Z;
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cookingforblockheads:multiblock_kitchen"));
    }

    public static boolean shouldBlockRenderLowered(IBlockAccess world, BlockPos pos) {
        Block blockBelow = world.getBlockState(pos.down()).getBlock();
        return blockBelow == ModBlocks.corner || blockBelow == ModBlocks.counter;
    }

    public boolean shouldBePlacedFlipped(BlockPos pos, EnumFacing facing, EntityLivingBase placer) {
        boolean flipped;
        double dir = 0;
        if (facing.getAxis() == EnumFacing.Axis.Z) {
            dir = pos.getX() - placer.posX;
            dir *= -1;
        } else if (facing.getAxis() == EnumFacing.Axis.X) {
            dir = pos.getZ() - placer.posZ;
        }
        if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
            flipped = dir < 0;
        } else {
            flipped = dir > 0;
        }
        return flipped;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (itemHandler != null) {
                ItemUtils.dropItemHandlerItems(world, pos, itemHandler);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IDyeableKitchen) {
            IDyeableKitchen dyeable = (IDyeableKitchen) tileEntity;
            if (dyeable.getDyedColor() == color) {
                return false;
            }

            dyeable.setDyedColor(color);
        }

        return true;
    }

}
