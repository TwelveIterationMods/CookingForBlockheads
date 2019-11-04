package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.CabinetTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CabinetBlock extends KitchenCounterBlock {

    public static final String name = "cabinet";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape BOUNDING_BOX_NORTH = Block.makeCuboidShape(0, 2, 2, 16, 16, 16);
    private static final VoxelShape BOUNDING_BOX_EAST = Block.makeCuboidShape(0, 2, 0, 14, 16, 16);
    private static final VoxelShape BOUNDING_BOX_WEST = Block.makeCuboidShape(2, 2, 0, 16, 16, 16);
    private static final VoxelShape BOUNDING_BOX_SOUTH = Block.makeCuboidShape(0, 2, 0, 16, 16, 14);

    public CabinetBlock() {
        super(registryName);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CabinetTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case EAST:
                return BOUNDING_BOX_EAST;
            case WEST:
                return BOUNDING_BOX_WEST;
            case SOUTH:
                return BOUNDING_BOX_SOUTH;
            case NORTH:
            default:
                return BOUNDING_BOX_NORTH;
        }
    }
}
