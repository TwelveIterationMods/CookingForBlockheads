package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConnectorBlock extends BaseKitchenBlock {
    public static final MapCodec<ConnectorBlock> CODEC = simpleCodec(ConnectorBlock::new);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    protected ConnectorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(SHAPE, StairsShape.STRAIGHT));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
            return ItemInteractionResult.CONSUME_PARTIAL;
        }

        return ItemInteractionResult.FAIL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        final var clickedFace = context.getClickedFace();
        final var clickedPos = context.getClickedPos();
        final var state = (BlockState) this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HALF,
                        clickedFace != Direction.DOWN && (clickedFace == Direction.UP || !(context.getClickLocation().y - (double) clickedPos.getY() > 0.5)) ? Half.BOTTOM : Half.TOP);
        return state.setValue(SHAPE, getStairsShape(state, context.getLevel(), clickedPos));
    }

    private static StairsShape getStairsShape(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        final var facing = state.getValue(FACING);
        final var half = state.getValue(HALF);
        final var behind = blockGetter.getBlockState(pos.relative(facing));
        if (isValidConnector(behind) && half == getHalfFromNonStair(behind)) {
            final var behindFacing = getFacingFromNonStair(behind);
            if (behindFacing.getAxis() != facing.getAxis() && canTakeShape(state, blockGetter, pos, behindFacing.getOpposite())) {
                if (behindFacing == facing.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        final var front = blockGetter.getBlockState(pos.relative(facing.getOpposite()));
        if (isValidConnector(front) && half == getHalfFromNonStair(front)) {
            final var frontFacing = getFacingFromNonStair(front);
            if (frontFacing.getAxis() != facing.getAxis() && canTakeShape(state, blockGetter, pos, frontFacing)) {
                if (frontFacing == facing.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        BlockState facingState = blockGetter.getBlockState(pos.relative(direction));
        return !isValidConnector(facingState) || getFacingFromNonStair(facingState) != state.getValue(FACING) || getHalfFromNonStair(facingState) != state.getValue(HALF);
    }

    private static boolean isValidConnector(BlockState state) {
        return state.getBlock() instanceof ConnectorBlock || (state.getBlock() instanceof BaseKitchenBlock && state.hasProperty(FACING));
    }

    private static Half getHalfFromNonStair(BlockState state) {
        if (state.hasProperty(HALF)) {
            return state.getValue(HALF);
        }

        // Not pretty, but only the cabinet currently should be considered a top half for connectors. Should probably use an interface or tag for this for better addon support.
        if (state.getBlock() instanceof CabinetBlock) {
            return Half.TOP;
        }

        return Half.BOTTOM;
    }

    private static Direction getFacingFromNonStair(BlockState state) {
        // Not pretty, but stairs have opposite facing and I don't want to rewrite the vanilla logic to account for it - s o we flip our other kitchen blocks to match.
        if (!(state.getBlock() instanceof ConnectorBlock)) {
            return state.getValue(FACING).getOpposite();
        }

        return state.getValue(FACING);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        final var facing = state.getValue(FACING);
        final var shape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (facing.getAxis() == Direction.Axis.Z) {
                    return switch (shape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            case FRONT_BACK:
                if (facing.getAxis() == Direction.Axis.X) {
                    return switch (shape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
        }

        return super.mirror(state, mirror);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.connector.description").withStyle(ChatFormatting.GRAY));
    }
}
