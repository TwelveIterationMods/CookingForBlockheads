package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.block.BalmContainerBlock;
import net.blay09.mods.cookingforblockheads.tile.CabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CabinetBlock extends KitchenCounterBlock implements BalmContainerBlock {

    private static final VoxelShape BOUNDING_BOX_NORTH = Block.box(0, 2, 2, 16, 16, 16);
    private static final VoxelShape BOUNDING_BOX_EAST = Block.box(0, 2, 0, 14, 16, 16);
    private static final VoxelShape BOUNDING_BOX_WEST = Block.box(2, 2, 0, 16, 16, 16);
    private static final VoxelShape BOUNDING_BOX_SOUTH = Block.box(0, 2, 0, 16, 16, 14);

    public CabinetBlock() {
        super(registryName);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> BOUNDING_BOX_EAST;
            case WEST -> BOUNDING_BOX_WEST;
            case SOUTH -> BOUNDING_BOX_SOUTH;
            default -> BOUNDING_BOX_NORTH;
        };
    }
}
