package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.message.SyncedEffectMessage;
import net.blay09.mods.cookingforblockheads.tile.CowJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public class CowJarHandler {

    private static final List<Class<? extends LivingEntity>> additionalCowClasses = Lists.newArrayList();

    @SuppressWarnings("unchecked")
    public static void registerCowClass(Class<?> clazz) {
        additionalCowClasses.add((Class<? extends LivingEntity>) clazz);
    }

    public static void onLivingDamage(LivingDamageEvent event) {
        if (!CookingForBlockheadsConfig.getActive().cowJarEnabled) {
            return;
        }

        if (event.getDamageSource().getMsgId().equals("anvil") && isCow(event.getEntity())) {
            Entity entity = event.getEntity();
            Level level = entity.level();
            findMilkJar(level, entity.blockPosition()).ifPresent(pos -> {
                level.setBlockAndUpdate(pos, ModBlocks.cowJar.defaultBlockState());
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof CowJarBlockEntity && entity.getCustomName() != null) {
                    Component textComponent = Component.translatable("container.cookingforblockheads.cow_jar_custom", entity.getCustomName());
                    ((CowJarBlockEntity) tileEntity).setCustomName(textComponent);
                }

                // Ex Compressum compat for compressed cows
                boolean wasCompressed = Balm.getHooks().getPersistentData(event.getEntity()).getCompound(Compat.EX_COMPRESSUM).getBoolean("Compressed");
                if (wasCompressed && tileEntity instanceof CowJarBlockEntity) {
                    ((CowJarBlockEntity) tileEntity).setCompressedCow(true);
                }

                Balm.getNetworking().sendToTracking(entity, new SyncedEffectMessage(pos, SyncedEffectMessage.Type.COW_IN_A_JAR));
                entity.remove(Entity.RemovalReason.DISCARDED);
                event.setCanceled(true);
            });
        }
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

    public static boolean isCow(LivingEntity entity) {
        if (entity instanceof Cow) {
            return true;
        }

        for (Class<? extends LivingEntity> clazz : additionalCowClasses) {
            if (clazz.isAssignableFrom(entity.getClass())) {
                return true;
            }
        }

        ResourceLocation registryName = Balm.getRegistries().getKey(entity.getType());
        return registryName != null && registryName.getPath().contains("cow");
    }

}
