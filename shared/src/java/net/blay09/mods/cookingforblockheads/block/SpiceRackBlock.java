package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmContainerBlock;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.SpiceRackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class SpiceRackBlock extends BlockKitchen implements BalmContainerBlock {

    public static final String name = "spice_rack";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(0, 4, 14, 16, 16, 16),
            Block.box(0, 4, 0, 16, 16, 2),
            Block.box(14, 4, 0, 16, 16, 16),
            Block.box(0, 4, 0, 2, 16, 16),
    };

    private static final VoxelShape[] RENDER_SHAPES = new VoxelShape[]{
            Block.box(0, 8, 14, 16, 9, 16),
            Block.box(0, 8, 0, 16, 9, 2),
            Block.box(14, 8, 0, 16, 9, 16),
            Block.box(0, 8, 0, 2, 9, 16),
    };

    public SpiceRackBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2.5f), registryName);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpiceRackBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return SHAPES[facing.get3DDataValue() - 2];
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return RENDER_SHAPES[facing.get3DDataValue() - 2];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        if (facing == Direction.UP || facing == Direction.DOWN) {
            facing = Direction.NORTH;
        }

        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide) {
            SpiceRackBlockEntity spiceRack = (SpiceRackBlockEntity) level.getBlockEntity(pos);
            Balm.getNetworking().openGui(player, spiceRack);
        }
        return InteractionResult.SUCCESS;
    }

}
