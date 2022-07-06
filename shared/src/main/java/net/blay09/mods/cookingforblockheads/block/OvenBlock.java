package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.ModSounds;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.FridgeBlockEntity;
import net.blay09.mods.cookingforblockheads.tile.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class OvenBlock extends BlockKitchen {

    public static BooleanProperty POWERED = BooleanProperty.create("powered");
    public static BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final String name = "oven";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    private static final Random random = new Random();

    public OvenBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(5f, 10f), registryName);
        registerDefaultState(getStateDefinition().any().setValue(POWERED, false).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, ACTIVE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == ModItems.heatingUnit) {
            return InteractionResult.PASS;
        }

        if (rayTraceResult.getDirection() == Direction.UP) {
            if (CookingRegistry.isToolItem(heldItem)) {
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

}
