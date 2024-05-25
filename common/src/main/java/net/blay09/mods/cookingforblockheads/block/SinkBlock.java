package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.block.entity.SinkBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SinkBlock extends BaseKitchenBlock {

    public static final MapCodec<SinkBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color")
                    .orElse(null)
                    .forGetter(SinkBlock::getColor),
            propertiesCodec()).apply(it, SinkBlock::new));

    private final DyeColor color;

    public SinkBlock(DyeColor color, Properties properties) {
        super(properties.sound(SoundType.STONE).strength(5f, 10f));
        this.color = color;
    }

    @Nullable
    public DyeColor getColor() {
        return color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(FLIPPED, shouldBePlacedFlipped(context, state.getValue(FACING)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
            return ItemInteractionResult.SUCCESS;
        }

        ItemStack resultStack = cleanItem(itemStack);
        if (!resultStack.isEmpty()) {
            final var components = itemStack.getComponents();
            final var newItem = resultStack.copy();
            newItem.applyComponents(components);
            if (itemStack.getCount() <= 1) {
                player.setItemInHand(hand, newItem);
            } else {
                if (player.getInventory().add(newItem)) {
                    itemStack.shrink(1);
                }
            }
            spawnParticlesAndPlaySound(level, pos, state);
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1f, level.random.nextFloat() + 0.5f);
            return ItemInteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SinkBlockEntity sink) {
                FluidTank fluidTank = sink.getFluidTank();
                if (!Balm.getHooks().useFluidTank(state, level, pos, player, hand, blockHitResult)) {
                    // Special case for bottles, they can hold 1/3 of a bucket
                    if (itemStack.getItem() == Items.GLASS_BOTTLE) {
                        int simulated = fluidTank.drain(Fluids.WATER, 333, true);
                        if (simulated == 333) {
                            fluidTank.drain(Fluids.WATER, 333, false);
                            ItemStack filledBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
                            if (itemStack.getCount() == 1) {
                                player.setItemInHand(hand, filledBottle);
                            } else if (player.addItem(filledBottle)) {
                                itemStack.shrink(1);
                            }
                            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1f, 1f);
                        } else {
                            spawnParticlesAndPlaySound(level, pos, state);
                        }
                    } else {
                        spawnParticlesAndPlaySound(level, pos, state);
                    }
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    private void spawnParticlesAndPlaySound(Level level, BlockPos pos, BlockState state) {
        float dripWaterX = 0f;
        float dripWaterZ = 0f;
        switch (state.getValue(FACING)) {
            case NORTH -> {
                dripWaterZ = 0.25f;
                dripWaterX = -0.05f;
            }
            case SOUTH -> dripWaterX = 0.25f;
            case WEST -> {
                dripWaterX = 0.25f;
                dripWaterZ = 0.25f;
            }
            case EAST -> dripWaterZ = -0.05f;
        }

        float particleX = (float) pos.getX() + 0.5f;
        float particleY = (float) pos.getY() + 1.25f;
        float particleZ = (float) pos.getZ() + 0.5f;
        level.addParticle(ParticleTypes.SPLASH, (double) particleX + dripWaterX, (double) particleY - 0.45f, (double) particleZ + dripWaterZ, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            level.addParticle(ParticleTypes.SPLASH,
                    (double) particleX + Math.random() - 0.5f,
                    (double) particleY + Math.random() - 0.5f,
                    (double) particleZ + Math.random() - 0.5f,
                    0,
                    0,
                    0);
        }

        level.playSound(null, pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.1f, level.random.nextFloat() + 0.5f);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SinkBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.sink.get(), SinkBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.sink.description").withStyle(ChatFormatting.GRAY));
    }

    public ItemStack cleanItem(ItemStack itemStack) {
        if (itemStack.has(DataComponents.DYED_COLOR)) {
            itemStack.remove(DataComponents.DYED_COLOR);
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        final var block = color == null ? ModBlocks.sink : ModBlocks.dyedSinks[color.ordinal()];
        return block.defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(FLIPPED, state.getValue(FLIPPED));
    }
}
