package net.blay09.mods.cookingforblockheads.block;


import com.mojang.serialization.MapCodec;
import net.blay09.mods.cookingforblockheads.block.entity.ToolRackBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToolRackBlock extends BaseKitchenBlock {

    public static final MapCodec<ToolRackBlock> CODEC = simpleCodec(ToolRackBlock::new);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(0, 4, 14, 16, 16, 16),
            Block.box(0, 4, 0, 16, 16, 2),
            Block.box(14, 4, 0, 16, 16, 16),
            Block.box(0, 4, 0, 2, 16, 16),
    };

    private static final VoxelShape[] RENDER_SHAPES = new VoxelShape[]{
            Block.box(0, 11, 14, 16, 14, 16),
            Block.box(0, 11, 0, 16, 14, 2),
            Block.box(14, 11, 0, 16, 14, 16),
            Block.box(0, 11, 0, 2, 14, 16),
    };

    public ToolRackBlock(Properties properties) {
        super(properties.sound(SoundType.WOOD).strength(2.5f));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToolRackBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return SHAPES[facing.get3DDataValue() - 2];
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        final var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ToolRackBlockEntity toolRack)) {
            return InteractionResult.FAIL;
        }

        int hitSlot = getHitSlot(state, pos, blockHitResult);
        final var clickedItemStack = toolRack.getContainer().getItem(hitSlot);
        if (!clickedItemStack.isEmpty()) {
            toolRack.getContainer().setItem(hitSlot, ItemStack.EMPTY);
            player.setItemInHand(InteractionHand.MAIN_HAND, clickedItemStack);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        final var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ToolRackBlockEntity toolRack)) {
            return ItemInteractionResult.FAIL;
        }

        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (itemStack.getItem() instanceof BlockItem) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int hitSlot = getHitSlot(state, pos, blockHitResult);
        final var oldToolItem = toolRack.getContainer().getItem(hitSlot);
        final var toolItem = itemStack.split(1);
        if (!oldToolItem.isEmpty()) {
            if (!player.getInventory().add(oldToolItem)) {
                player.drop(oldToolItem, false);
            }
            toolRack.getContainer().setItem(hitSlot, toolItem);
        } else {
            toolRack.getContainer().setItem(hitSlot, toolItem);
        }

        return ItemInteractionResult.SUCCESS;
    }

    private static int getHitSlot(BlockState state, BlockPos pos, BlockHitResult blockHitResult) {
        Direction stateFacing = state.getValue(FACING);

        double hitX = blockHitResult.getLocation().x - pos.getX();
        double hitZ = blockHitResult.getLocation().z - pos.getZ();
        double hit = switch (stateFacing) {
            case NORTH -> hitX;
            case SOUTH -> 1f - hitX;
            case WEST -> 1f - hitZ;
            case EAST -> hitZ;
            default -> hitX;
        };

        int hitSlot = hit > 0.5f ? 0 : 1;
        return hitSlot;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.tool_rack.description").withStyle(ChatFormatting.GRAY));
    }
}
