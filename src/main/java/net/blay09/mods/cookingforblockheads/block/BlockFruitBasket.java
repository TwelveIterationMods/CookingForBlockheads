package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileFruitBasket;
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

public class BlockFruitBasket extends BlockKitchen {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.1, 0.875);

    public BlockFruitBasket() {
        super(Material.WOOD);

        setSoundType(SoundType.WOOD);
        setHardness(2.5f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (shouldBlockRenderLowered(world, pos)) {
            return BOUNDING_BOX.expand(0, -0.05, 0);
        }

        return BOUNDING_BOX;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LOWERED);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(LOWERED, shouldBlockRenderLowered(world, pos));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileFruitBasket();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand != EnumHand.MAIN_HAND) {
            return true;
        }

        player.openGui(CookingForBlockheads.instance, GuiHandler.FRUIT_BASKET, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public String getIdentifier() {
        return "fruit_basket";
    }

    @Nullable
    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileFruitBasket.class;
    }
}