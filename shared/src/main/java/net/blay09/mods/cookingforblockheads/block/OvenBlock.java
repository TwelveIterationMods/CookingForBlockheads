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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == ModItems.heatingUnit) {
            return InteractionResult.PASS;
        }

        if (tryRecolorBlock(state, heldItem, level, pos, player, rayTraceResult)) {
            return InteractionResult.SUCCESS;
        }

        if (rayTraceResult.getDirection() == Direction.UP) {
            if (heldItem.is(ModItemTags.UTENSILS)) {
                Direction stateFacing = state.getValue(FACING);
                double hx = rayTraceResult.getLocation().x;
                double hz = rayTraceResult.getLocation().z;
                switch (stateFacing) {
                    case NORTH -> {
                        hx = 1f - rayTraceResult.getLocation().x;
                        hz = 1f - rayTraceResult.getLocation().z;
                    }
//                    case SOUTH: hx = hitX; hz = hitZ; break;
                    case WEST -> {
                        hz = 1f - rayTraceResult.getLocation().x;
                        hx = rayTraceResult.getLocation().z;
                    }
                    case EAST -> {
                        hz = rayTraceResult.getLocation().x;
                        hx = 1f - rayTraceResult.getLocation().z;
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
                        ItemStack toolItem = heldItem.split(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        OvenBlockEntity oven = (OvenBlockEntity) level.getBlockEntity(pos);
        if (rayTraceResult.getDirection() == state.getValue(FACING)) {
            if (oven != null) {
                if (player.isShiftKeyDown()) {
                    oven.getDoorAnimator().toggleForcedOpen();
                    return InteractionResult.SUCCESS;
                } else if (!heldItem.isEmpty() && oven.getDoorAnimator().isForcedOpen()) {
                    heldItem = ContainerUtils.insertItemStacked(oven.getInputContainer(), heldItem, false);
                    if (!heldItem.isEmpty()) {
                        heldItem = ContainerUtils.insertItemStacked(oven.getFuelContainer(), heldItem, false);
                    }
                    player.setItemInHand(hand, heldItem);
                    return InteractionResult.SUCCESS;
                }
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
    protected void appendHoverDescriptionText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.oven.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected boolean recolorBlock(BlockState state, LevelAccessor world, BlockPos pos, Direction facing, DyeColor color) {
        // TODO map to the correct block since it's not a state anymore for the oven
        return super.recolorBlock(state, world, pos, facing, color);
    }
}
