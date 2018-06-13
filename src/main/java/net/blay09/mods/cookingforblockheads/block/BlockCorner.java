package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.TileCorner;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCorner extends BlockKitchen implements IRegisterableBlock {

    private static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[]{
            new AxisAlignedBB(0, 0, 0, 0.96875, 0.9375, 0.96875),
            new AxisAlignedBB(0.03125, 0, 0.03125, 1, 0.9375, 1),
            new AxisAlignedBB(0, 0, 0.03125, 0.96875, 0.9375, 1),
            new AxisAlignedBB(0.03125, 0, 0, 1, 0.9375, 0.96875)
    };

    public BlockCorner() {
        super(Material.ROCK);

        setSoundType(SoundType.STONE);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, COLOR);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCorner) {
            return state.withProperty(COLOR, ((TileCorner) tileEntity).getDyedColor());
        }

        return state;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return applyDye(world, pos, facing, player, player.getHeldItem(hand));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOXES[state.getValue(FACING).getIndex() - 2];
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
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileCorner();
    }

    @Override
    public boolean isDyeable() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "corner";
    }

    @Nullable
    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileCorner.class;
    }

}
