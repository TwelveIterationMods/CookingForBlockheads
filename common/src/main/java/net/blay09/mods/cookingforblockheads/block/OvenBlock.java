package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class OvenBlock extends BaseKitchenBlock {

    public static final MapCodec<OvenBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color").forGetter(OvenBlock::getColor),
            propertiesCodec()).apply(it, OvenBlock::new));

    public static BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final Random random = new Random();
    private final DyeColor color;

    public OvenBlock(DyeColor color, Properties properties) {
        super(properties.sound(SoundType.METAL).strength(5f, 10f));
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
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        final var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof OvenBlockEntity oven)) {
            return ItemInteractionResult.FAIL;
        }

        if (itemStack.getItem() == ModItems.heatingUnit) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
            return ItemInteractionResult.SUCCESS;
        }

        if (blockHitResult.getDirection() == Direction.UP) {
            if (itemStack.is(ModItemTags.UTENSILS)) {
                Direction stateFacing = state.getValue(FACING);
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
                if (index != -1) {
                    OvenBlockEntity tileOven = (OvenBlockEntity) level.getBlockEntity(pos);
                    if (tileOven != null && tileOven.getToolItem(index).isEmpty()) {
                        ItemStack toolItem = itemStack.split(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (blockHitResult.getDirection() == state.getValue(FACING)) {
            if (oven.getDoorAnimator().isForcedOpen()) {
                itemStack = ContainerUtils.insertItemStacked(oven.getInputContainer(), itemStack, false);
                if (!itemStack.isEmpty()) {
                    itemStack = ContainerUtils.insertItemStacked(oven.getFuelContainer(), itemStack, false);
                }
                player.setItemInHand(hand, itemStack);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
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

        if (!level.isClientSide) {
            Balm.getNetworking().openGui(player, oven);
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OvenBlockEntity(pos, state);
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

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof OvenBlockEntity) {
                if (((OvenBlockEntity) tileEntity).hasPowerUpgrade()) {
                    ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.heatingUnit));
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, ModBlockEntities.oven.get(), OvenBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.oven.get(), OvenBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.oven.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        final var block = color == null ? ModBlocks.ovens[0] : ModBlocks.ovens[color.ordinal()];
        return block.defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(ACTIVE, state.getValue(ACTIVE));
    }
}
