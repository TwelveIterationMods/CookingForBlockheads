package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.block.entity.CounterBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import org.jspecify.annotations.Nullable;

public class CounterBlock extends BaseKitchenBlock {

    public static final MapCodec<CounterBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color")
                    .orElse(null)
                    .forGetter(CounterBlock::getColor),
            propertiesCodec()).apply(it, CounterBlock::new));

    private final @Nullable DyeColor color;

    public CounterBlock(Properties properties) {
        this(null, properties);
    }

    public CounterBlock(@Nullable DyeColor color, Properties properties) {
        super(properties.sound(SoundType.STONE).strength(5f, 10f));
        this.color = color;
    }

    public @Nullable DyeColor getColor() {
        return color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CounterBlockEntity(pos, state);
    }

    @Override
    protected boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
        return oldState.is(ModBlockTags.COUNTERS);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
            return InteractionResult.SUCCESS;
        }

        if (itemStack.is(ModItems.preservationChamber.asItem())) {
            return InteractionResult.PASS;
        }

        CounterBlockEntity counter = (CounterBlockEntity) level.getBlockEntity(pos);
        if (blockHitResult.getDirection() == state.getValue(FACING) && counter.getDoorAnimator().isForcedOpen()) {
            itemStack = counter.insertItemStacked(itemStack, false);
            player.setItemInHand(hand, itemStack);
            return InteractionResult.SUCCESS;
        }

        if (blockHitResult.getDirection() == Direction.UP && itemStack.getItem() instanceof BlockItem) {
            return InteractionResult.PASS;
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

        if (!level.isClientSide()) {
            Balm.networking().openMenu(player, counter);
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
        return level.isClientSide()
                ? createTickerHelper(type, ModBlockEntities.counter.value(), CounterBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.counter.value(), CounterBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        return ModBlocks.counters.get(color)
                .defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(FLIPPED, state.getValue(FLIPPED));
    }
}
