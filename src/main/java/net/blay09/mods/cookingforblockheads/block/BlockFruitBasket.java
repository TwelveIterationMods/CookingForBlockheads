package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.FruitBasketTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFruitBasket extends BlockKitchen {

    public static final String name = "fruit_basket";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.1, 0.875);

    public BlockFruitBasket() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5f), registryName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (shouldBlockRenderLowered(world, pos)) {
            return BOUNDING_BOX.expand(0, -0.05, 0);
        }

        return BOUNDING_BOX;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(LOWERED, shouldBlockRenderLowered(world, pos));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new FruitBasketTileEntity();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (hand != Hand.MAIN_HAND) {
            return true;
        }

        player.openGui(CookingForBlockheads.instance, GuiHandler.FRUIT_BASKET, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

}
