package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.blay09.mods.cookingforblockheads.block.entity.CabinetBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class CabinetBlock extends CounterBlock {

    public enum CabinetModelType implements StringRepresentable {
        SMALL,
        LARGE_LOWER,
        LARGE_UPPER;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public static final EnumProperty<CabinetModelType> MODEL_TYPE = EnumProperty.create("model", CabinetModelType.class);

    private static final VoxelShape SMALL_BOUNDING_BOX_NORTH = Block.box(0, 2, 2, 16, 16, 16);
    private static final VoxelShape SMALL_BOUNDING_BOX_EAST = Block.box(0, 2, 0, 14, 16, 16);
    private static final VoxelShape SMALL_BOUNDING_BOX_WEST = Block.box(2, 2, 0, 16, 16, 16);
    private static final VoxelShape SMALL_BOUNDING_BOX_SOUTH = Block.box(0, 2, 0, 16, 16, 14);
    private static final VoxelShape LARGE_BOUNDING_BOX_NORTH = Block.box(0, 0, 2, 16, 16, 16 - 0.25);
    private static final VoxelShape LARGE_BOUNDING_BOX_EAST = Block.box(0.25, 0, 0, 14, 16, 16);
    private static final VoxelShape LARGE_BOUNDING_BOX_WEST = Block.box(2, 0, 0, 16 - 0.25, 16, 16);
    private static final VoxelShape LARGE_BOUNDING_BOX_SOUTH = Block.box(0, 0, 0.25, 16, 16, 14);

    public CabinetBlock(Properties properties) {
        this(null, properties);
    }

    public CabinetBlock(@Nullable DyeColor color, Properties properties) {
        super(color, properties);
        registerDefaultState(getStateDefinition().any().setValue(MODEL_TYPE, CabinetModelType.SMALL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED, MODEL_TYPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }

    @Override
    protected boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
        return oldState.is(ModBlockTags.CABINETS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        final var isLarge = state.getValue(MODEL_TYPE) != CabinetModelType.SMALL;
        return switch (state.getValue(FACING)) {
            case EAST -> isLarge ? LARGE_BOUNDING_BOX_EAST : SMALL_BOUNDING_BOX_EAST;
            case WEST -> isLarge ? LARGE_BOUNDING_BOX_WEST : SMALL_BOUNDING_BOX_WEST;
            case SOUTH -> isLarge ? LARGE_BOUNDING_BOX_SOUTH : SMALL_BOUNDING_BOX_SOUTH;
            default -> isLarge ? LARGE_BOUNDING_BOX_NORTH : SMALL_BOUNDING_BOX_NORTH;
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, ModBlockEntities.cabinet.value(), CabinetBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.cabinet.value(), CabinetBlockEntity::serverTick);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean below = level.getBlockState(pos.below()).getBlock() == this;
        boolean above = level.getBlockState(pos.above()).getBlock() == this;
        return !(below && above)
                && !(below && level.getBlockState(pos.below(2)).getBlock() == this)
                && !(above && level.getBlockState(pos.above(2)).getBlock() == this)
                && super.canSurvive(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var state = super.getStateForPlacement(context);
        final var level = context.getLevel();
        final var pos = context.getClickedPos();
        final var stateBelow = level.getBlockState(pos.below());
        final var stateAbove = level.getBlockState(pos.above());
        if (stateBelow.getBlock() == this && stateBelow.getValue(MODEL_TYPE) == CabinetModelType.SMALL) {
            state = state.setValue(MODEL_TYPE, CabinetModelType.LARGE_UPPER)
                    .setValue(FACING, stateBelow.getValue(FACING))
                    .setValue(FLIPPED, stateBelow.getValue(FLIPPED));
        } else if (stateAbove.getBlock() == this && stateAbove.getValue(MODEL_TYPE) == CabinetModelType.SMALL) {
            state = state.setValue(MODEL_TYPE, CabinetModelType.LARGE_LOWER)
                    .setValue(FACING, stateAbove.getValue(FACING))
                    .setValue(FLIPPED, stateAbove.getValue(FLIPPED));
        }
        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource randomSource) {
        final var stateBelow = level.getBlockState(pos.below());
        final var stateAbove = level.getBlockState(pos.above());
        if (stateBelow.getBlock() == this && stateBelow.getValue(MODEL_TYPE) == CabinetModelType.LARGE_LOWER) {
            return state.setValue(MODEL_TYPE, CabinetModelType.LARGE_UPPER)
                    .setValue(FACING, stateBelow.getValue(FACING))
                    .setValue(FLIPPED, stateBelow.getValue(FLIPPED));
        } else if (stateAbove.getBlock() == this && stateAbove.getValue(MODEL_TYPE) == CabinetModelType.LARGE_UPPER) {
            return state.setValue(MODEL_TYPE, CabinetModelType.LARGE_LOWER)
                    .setValue(FACING, stateAbove.getValue(FACING))
                    .setValue(FLIPPED, stateAbove.getValue(FLIPPED));
        } else if (state.getValue(MODEL_TYPE) == CabinetModelType.LARGE_LOWER && stateAbove.getBlock() != this) {
            return state.setValue(MODEL_TYPE, CabinetModelType.SMALL);
        } else if (state.getValue(MODEL_TYPE) == CabinetModelType.LARGE_UPPER && stateBelow.getBlock() != this) {
            return state.setValue(MODEL_TYPE, CabinetModelType.SMALL);
        }

        return super.updateShape(state, level, scheduledTickAccess, pos, facing, facingPos, facingState, randomSource);
    }

    @Override
    protected boolean recolorBlock(BlockState state, LevelAccessor level, BlockPos pos, Direction facing, DyeColor color) {
        final var otherPos = switch (state.getValue(MODEL_TYPE)) {
            case SMALL -> null;
            case LARGE_LOWER -> pos.above();
            case LARGE_UPPER -> pos.below();
        };
        if (otherPos != null) {
            super.recolorBlock(level.getBlockState(otherPos), level, otherPos, facing, color);
        }
        return super.recolorBlock(state, level, pos, facing, color);
    }

    @Override
    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        return ModBlocks.cabinets.get(color)
                .defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(FLIPPED, state.getValue(FLIPPED))
                .setValue(MODEL_TYPE, state.getValue(MODEL_TYPE));
    }
}
