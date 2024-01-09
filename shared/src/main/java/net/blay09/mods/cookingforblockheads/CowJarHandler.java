package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.message.SyncedEffectMessage;
import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.tag.ModEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CowJarHandler {

    public static void onLivingDamage(LivingDamageEvent event) {
        if (!CookingForBlockheadsConfig.getActive().cowJarEnabled) {
            return;
        }

        if (event.getDamageSource().getMsgId().equals("anvil") && event.getEntity().getType().is(ModEntityTypeTags.COW)) {
            Entity entity = event.getEntity();
            BlockPos pos = entity.blockPosition();
            Level level = entity.level();
            BlockState blockBelow = level.getBlockState(pos);
            if (blockBelow.getBlock() == ModBlocks.milkJar) {
                level.setBlockAndUpdate(pos, ModBlocks.cowJar.defaultBlockState());
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof CowJarBlockEntity && entity.getCustomName() != null) {
                    Component textComponent = Component.translatable("container.cookingforblockheads.cow_jar_custom", entity.getCustomName());
                    ((CowJarBlockEntity) tileEntity).setCustomName(textComponent);
                }

                // Ex Compressum compat for compressed cows
                boolean wasCompressed = Balm.getHooks().getPersistentData(event.getEntity()).getCompound("excompressum").getBoolean("Compressed");
                if (wasCompressed && tileEntity instanceof CowJarBlockEntity) {
                    ((CowJarBlockEntity) tileEntity).setCompressedCow(true);
                }
            }
            Balm.getNetworking().sendToTracking(entity, new SyncedEffectMessage(pos, SyncedEffectMessage.Type.COW_IN_A_JAR));
            entity.remove(Entity.RemovalReason.DISCARDED);
            event.setCanceled(true);
        }
    }

}
