package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.block.entity.IMutableNameable;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseKitchenBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final BooleanProperty HAS_COLOR = BooleanProperty.create("has_color");
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private static final VoxelShape BOUNDING_BOX_X = Block.box(0.5, 0, 0, 15.5, 16.0, 16);
    private static final VoxelShape BOUNDING_BOX_Z = Block.box(0, 0, 0.5, 16, 16.0, 15.5);

    protected BaseKitchenBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
            return BOUNDING_BOX_X;
        } else {
            return BOUNDING_BOX_Z;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        return state.hasProperty(HAS_COLOR) ? state.setValue(HAS_COLOR, false) : state;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.multiblock_kitchen").withStyle(ChatFormatting.YELLOW));

        appendHoverDescriptionText(itemStack, context, tooltip, flag);
    }

    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    }

    public boolean shouldBePlacedFlipped(BlockPlaceContext context, Direction facing) {
        BlockPos pos = context.getClickedPos();
        Player placer = context.getPlayer();
        if (placer == null) {
            return Math.random() < 0.5;
        }

        boolean flipped;
        double dir = 0;
        if (facing.getAxis() == Direction.Axis.Z) {
            dir = pos.getX() + 0.5f - placer.getX();
            dir *= -1;
        } else if (facing.getAxis() == Direction.Axis.X) {
            dir = pos.getZ() + 0.5f - placer.getZ();
        }
        if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            flipped = dir < 0;
        } else {
            flipped = dir > 0;
        }
        return flipped;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BalmContainerProvider containerProvider && !state.is(newState.getBlock())) {
            containerProvider.dropItems(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    public boolean tryRecolorBlock(BlockState state, ItemStack heldItem, Level level, BlockPos pos, Player player, BlockHitResult rayTraceResult) {
        if (heldItem.getItem() == Items.BONE_MEAL) {
            if (removeColor(state, level, pos, rayTraceResult.getDirection())) {
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }

                return true;
            }
        }

        DyeColor color = Balm.getHooks().getColor(heldItem);
        if (color != null) {
            if (recolorBlock(state, level, pos, rayTraceResult.getDirection(), color)) {
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }
            }

            return true;
        }

        return false;
    }

    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        if (state.hasProperty(COLOR) && state.hasProperty(HAS_COLOR)) {
            if (color != null) {
                return state.setValue(COLOR, color).setValue(HAS_COLOR, true);
            } else {
                return state.setValue(HAS_COLOR, false);
            }
        }

        return state;
    }

    private boolean removeColor(BlockState state, LevelAccessor world, BlockPos pos, Direction facing) {
        final var removedColorState = getDyedStateOf(state, null);
        if (!removedColorState.equals(state)) {
            final var blockEntity = world.getBlockEntity(pos);
            Object transferData = null;
            if (blockEntity instanceof TransferableBlockEntity transferableBlockEntity) {
                transferData = transferableBlockEntity.snapshotDataForTransfer();
            }
            world.setBlock(pos, removedColorState, 3);
            final var newBlockEntity = world.getBlockEntity(pos);
            if (transferData != null && newBlockEntity instanceof TransferableBlockEntity transferableBlockEntity) {
                transferableBlockEntity.restoreFromTransferSnapshot(transferData);
            }
            return true;
        }
        return false;
    }

    protected boolean recolorBlock(BlockState state, LevelAccessor world, BlockPos pos, Direction facing, DyeColor color) {
        final var recoloredState = getDyedStateOf(state, color);
        if (!recoloredState.equals(state)) {
            final var blockEntity = world.getBlockEntity(pos);
            Object transferData = null;
            if (blockEntity instanceof TransferableBlockEntity transferableBlockEntity) {
                transferData = transferableBlockEntity.snapshotDataForTransfer();
            }
            world.setBlock(pos, recoloredState, 3);
            final var newBlockEntity = world.getBlockEntity(pos);
            if (transferData != null && newBlockEntity instanceof TransferableBlockEntity transferableBlockEntity) {
                transferableBlockEntity.restoreFromTransferSnapshot(transferData);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity != null) {
            return tileEntity.triggerEvent(id, param);
        }

        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.has(DataComponents.CUSTOM_NAME)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IMutableNameable) {
                ((IMutableNameable) blockEntity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

}
