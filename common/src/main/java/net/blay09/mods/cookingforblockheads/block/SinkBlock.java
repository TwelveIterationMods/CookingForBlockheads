package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.tile.SinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SinkBlock extends BlockDyeableKitchen {

    public static final String name = "sink";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public SinkBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(5f, 10f), registryName);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR, HAS_COLOR, FLIPPED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(FLIPPED, shouldBePlacedFlipped(context, state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (tryRecolorBlock(state, heldItem, level, pos, player, rayTraceResult)) {
            return InteractionResult.SUCCESS;
        }

        ItemStack resultStack = CookingRegistry.getSinkOutput(heldItem);
        if (!resultStack.isEmpty()) {
            CompoundTag tagCompound = heldItem.getTag();
            ItemStack newItem = resultStack.copy();
            if (tagCompound != null) {
                newItem.setTag(tagCompound);
            }
            if (heldItem.getCount() <= 1) {
                player.setItemInHand(hand, newItem);
            } else {
                if (player.getInventory().add(newItem)) {
                    heldItem.shrink(1);
                }
            }
            spawnParticlesAndPlaySound(level, pos, state);
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1f, level.random.nextFloat() + 0.5f);
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SinkBlockEntity sink) {
                FluidTank fluidTank = sink.getFluidTank();
                if (!Balm.getHooks().useFluidTank(state, level, pos, player, hand, rayTraceResult)) {
                    // Special case for bottles, they can hold 1/3 of a bucket
                    if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                        int simulated = fluidTank.drain(Fluids.WATER, 333, true);
                        if (simulated == 333) {
                            fluidTank.drain(Fluids.WATER, 333, false);
                            ItemStack filledBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                            if (heldItem.getCount() == 1) {
                                player.setItemInHand(hand, filledBottle);
                            } else if (player.addItem(filledBottle)) {
                                heldItem.shrink(1);
                            }
                            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1f, 1f);
                        } else {
                            spawnParticlesAndPlaySound(level, pos, state);
                        }
                    } else {
                        spawnParticlesAndPlaySound(level, pos, state);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
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
            level.addParticle(ParticleTypes.SPLASH, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 0, 0);
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
}
