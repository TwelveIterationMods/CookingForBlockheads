package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.Balm;

import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.block.entity.CounterBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CounterBlock extends BaseKitchenBlock {

    public static final MapCodec<CounterBlock> CODEC = simpleCodec(CounterBlock::new);

    public CounterBlock(Properties properties) {
        super(properties.sound(SoundType.STONE).strength(5f, 10f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED, COLOR, HAS_COLOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CounterBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
            return ItemInteractionResult.SUCCESS;
        }

        CounterBlockEntity counter = (CounterBlockEntity) level.getBlockEntity(pos);
        if (blockHitResult.getDirection() == state.getValue(FACING)) {
            itemStack = counter.insertItemStacked(itemStack, false);
            player.setItemInHand(hand, itemStack);
            return ItemInteractionResult.SUCCESS;
        }

        if (blockHitResult.getDirection() == Direction.UP && itemStack.getItem() instanceof BlockItem) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult rayTraceResult) {
        CounterBlockEntity counter = (CounterBlockEntity) level.getBlockEntity(pos);
        if (rayTraceResult.getDirection() == state.getValue(FACING)) {
            if (counter != null) {
                if (player.isShiftKeyDown()) {
                    counter.getDoorAnimator().toggleForcedOpen();
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (!level.isClientSide) {
            Balm.getNetworking().openGui(player, counter);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(FLIPPED, shouldBePlacedFlipped(context, state.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, ModBlockEntities.counter.get(), CounterBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.counter.get(), CounterBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.counter.description").withStyle(ChatFormatting.GRAY));
    }
}
