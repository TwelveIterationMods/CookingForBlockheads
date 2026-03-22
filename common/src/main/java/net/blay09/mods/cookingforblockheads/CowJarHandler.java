package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.tag.ModEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CowJarHandler {

    public static float onLivingDamage(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (!CookingForBlockheadsConfig.getActive().cowJarEnabled) {
            return damageAmount;
        }

        if (damageSource.getMsgId().equals("anvil") && entity.is(ModEntityTypeTags.COW)) {
            final var level = entity.level();
            return findMilkJar(level, entity.blockPosition()).map(pos -> {
                level.setBlockAndUpdate(pos, ModBlocks.cowJar.defaultBlockState());
                final var blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CowJarBlockEntity cowJar) {
                    final var cowVariant = entity.get(DataComponents.COW_VARIANT);
                    cowJar.setVariant(cowVariant);

                    if (entity.getCustomName() != null) {
                        final var textComponent = Component.translatable("container.cookingforblockheads.cow_jar_custom", entity.getCustomName());
                        cowJar.setCustomName(textComponent);
                    }

                    // Ex Compressum compat for compressed cows
                    boolean wasCompressed = Balm.hooks().getPersistentData(entity).getCompound("excompressum")
                            .flatMap(it -> it.getBoolean("Compressed")).orElse(false);
                    if (wasCompressed) {
                        cowJar.setCompressedCow(true);
                    }
                }

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0);
                    level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1f, 1f);
                }
                entity.remove(Entity.RemovalReason.DISCARDED);
                return 0f;
            }).orElse(damageAmount);
        }

        return damageAmount;
    }

    private static Optional<BlockPos> findMilkJar(Level level, BlockPos pos) {
        if (level.getBlockState(pos).is(ModBlocks.milkJar)) {
            return Optional.of(pos);
        }
        final var posBelow = pos.below();
        if (level.getBlockState(posBelow).is(ModBlocks.milkJar)) {
            return Optional.of(posBelow);
        }

        final var mutablePos = pos.mutable();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    mutablePos.setWithOffset(pos, x, y, z);
                    if (level.getBlockState(mutablePos).is(ModBlocks.milkJar)) {
                        return Optional.of(mutablePos.immutable());
                    }
                }
            }
        }

        return Optional.empty();
    }

}
