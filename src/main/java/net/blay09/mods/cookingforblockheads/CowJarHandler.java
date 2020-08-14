package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class CowJarHandler {

    private static final List<Class<? extends LivingEntity>> additionalCowClasses = Lists.newArrayList();

    @SuppressWarnings("unchecked")
    public static void registerCowClass(Class<?> clazz) {
        additionalCowClasses.add((Class<? extends LivingEntity>) clazz);
    }

    @SubscribeEvent
    public void onEntityDamage(LivingAttackEvent event) {
        if (!CookingForBlockheadsConfig.COMMON.cowJarEnabled.get()) {
            return;
        }
        if (event.getSource() == DamageSource.ANVIL && isCow(event.getEntityLiving())) {
            BlockPos pos = event.getEntity().getPosition();
            BlockState blockBelow = event.getEntity().getEntityWorld().getBlockState(pos);
            if (blockBelow.getBlock() == ModBlocks.milkJar) {
                event.getEntity().getEntityWorld().setBlockState(pos, ModBlocks.cowJar.getDefaultState());
            }
            NetworkHandler.sendToAllTracking(new MessageSyncedEffect(pos, MessageSyncedEffect.Type.COW_IN_A_JAR), event.getEntity());
            event.getEntity().remove();
            event.setCanceled(true);
        }
    }

    public boolean isCow(LivingEntity entity) {
        if (entity instanceof CowEntity) {
            return true;
        }

        for (Class<? extends LivingEntity> clazz : additionalCowClasses) {
            if (clazz.isAssignableFrom(entity.getClass())) {
                return true;
            }
        }

        ResourceLocation registryName = entity.getType().getRegistryName();
        return registryName != null && registryName.getPath().contains("cow");
    }

}
