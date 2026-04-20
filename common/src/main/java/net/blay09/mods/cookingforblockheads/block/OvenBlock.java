package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.ContainerUtils;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.Random;

public class OvenBlock extends BaseKitchenBlock {

    public static final MapCodec<OvenBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color").forGetter(OvenBlock::getColor),
            propertiesCodec()).apply(it, OvenBlock::new));

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final Random random = new Random();
    private final DyeColor color;

    public OvenBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
        registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false));
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        final var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof OvenBlockEntity oven)) {
            return InteractionResult.FAIL;
        }

        if (itemStack.is(ModItems.heatingUnit)) {
            return InteractionResult.PASS;
        }

        if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
            return InteractionResult.SUCCESS;
        }

        if (blockHitResult.getDirection() == Direction.UP) {
            if (itemStack.is(ModItemTags.UTENSILS)) {
                Direction stateFacing = state.getValue(FACING);
                int index = resolveToolHitIndex(blockHitResult, stateFacing);
                if (index != -1) {
                    OvenBlockEntity tileOven = (OvenBlockEntity) level.getBlockEntity(pos);
                    if (tileOven != null && tileOven.getToolItem(index).isEmpty()) {
                        ItemStack toolItem = itemStack.split(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (blockHitResult.getDirection() == state.getValue(FACING)) {
            if (oven.getDoorAnimator().isForcedOpen()) {
                itemStack = ContainerUtils.insertItemStacked(oven.getInputContainer(), itemStack, false);
                if (!itemStack.isEmpty()) {
                    itemStack = ContainerUtils.insertItemStacked(oven.getFuelContainer(), itemStack, false);
                }
                player.setItemInHand(hand, itemStack);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    private static int resolveToolHitIndex(BlockHitResult blockHitResult, Direction stateFacing) {
        double hx = blockHitResult.getLocation().x;
        double hz = blockHitResult.getLocation().z;
        switch (stateFacing) {
            case NORTH -> {
                hx = 1f - blockHitResult.getLocation().x;
                hz = 1f - blockHitResult.getLocation().z;
            }
//                    case SOUTH: hx = hitX; hz = hitZ; break;
            case WEST -> {
                hz = 1f - blockHitResult.getLocation().x;
                hx = blockHitResult.getLocation().z;
            }
            case EAST -> {
                hz = blockHitResult.getLocation().x;
                hx = 1f - blockHitResult.getLocation().z;
            }
        }
        int index = -1;
        if (hx < 0.5f && hz < 0.5f) {
            index = 1;
        } else if (hx >= 0.5f && hz < 0.5f) {
            index = 0;
        } else if (hx < 0.5f && hz >= 0.5f) {
            index = 3;
        } else if (hx >= 0.5f && hz >= 0.5f) {
            index = 2;
        }
        return index;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult rayTraceResult) {
        final var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof OvenBlockEntity oven)) {
            return InteractionResult.FAIL;
        }

        if (rayTraceResult.getDirection() == state.getValue(FACING)) {
            if (player.isShiftKeyDown()) {
                oven.getDoorAnimator().toggleForcedOpen();
                return InteractionResult.SUCCESS;
            }
        }

        if (!level.isClientSide()) {
            Balm.networking().openMenu(player, oven);
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OvenBlockEntity(pos, state);
    }

    @Override
    protected boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
        return oldState.is(ModBlockTags.OVENS);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        OvenBlockEntity tileEntity = (OvenBlockEntity) level.getBlockEntity(pos);
        if (tileEntity != null && tileEntity.isBurning()) {
            Direction facing = state.getValue(FACING);
            float x = (float) pos.getX() + 0.5f;
            float y = (float) pos.getY() + 0f + OvenBlock.random.nextFloat() * 6f / 16f;
            float z = (float) pos.getZ() + 0.5f;
            float f3 = 0.52f;
            float f4 = OvenBlock.random.nextFloat() * 0.6f - 0.3f;

            if (facing == Direction.WEST) {
                level.addParticle(ParticleTypes.SMOKE, x - f3, y, z + f4, 0, 0, 0);
            } else if (facing == Direction.EAST) {
                level.addParticle(ParticleTypes.SMOKE, x + f3, y, z + f4, 0, 0, 0);
            } else if (facing == Direction.NORTH) {
                level.addParticle(ParticleTypes.SMOKE, x + f4, y, z - f3, 0, 0, 0);
            } else if (facing == Direction.SOUTH) {
                level.addParticle(ParticleTypes.SMOKE, x + f4, y, z + f3, 0, 0, 0);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, ModBlockEntities.oven.value(), OvenBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.oven.value(), OvenBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        return ModBlocks.ovens.get(color)
                .defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(ACTIVE, state.getValue(ACTIVE));
    }
}
