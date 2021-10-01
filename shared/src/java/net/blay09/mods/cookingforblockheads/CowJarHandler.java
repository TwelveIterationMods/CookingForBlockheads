//package net.blay09.mods.cookingforblockheads; TODO
//
//import com.google.common.collect.Lists;
//import net.blay09.mods.balm.api.Balm;
//import net.blay09.mods.cookingforblockheads.block.ModBlocks;
//import net.blay09.mods.cookingforblockheads.compat.Compat;
//import net.blay09.mods.cookingforblockheads.network.message.SyncedEffectMessage;
//import net.blay09.mods.cookingforblockheads.tile.CowJarBlockEntity;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TranslatableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.animal.Cow;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//
//import java.util.List;
//
//public class CowJarHandler {
//
//    private static final List<Class<? extends LivingEntity>> additionalCowClasses = Lists.newArrayList();
//
//    @SuppressWarnings("unchecked")
//    public static void registerCowClass(Class<?> clazz) {
//        additionalCowClasses.add((Class<? extends LivingEntity>) clazz);
//    }
//
//    @SubscribeEvent
//    public void onEntityDamage(LivingAttackEvent event) {
//        if (!CookingForBlockheadsConfig.getActive().cowJarEnabled) {
//            return;
//        }
//
//        if (event.getSource() == DamageSource.ANVIL && isCow(event.getEntityLiving())) {
//            Entity entity = event.getEntity();
//            BlockPos pos = entity.getPosition();
//            Level level = entity.getEntityWorld();
//            BlockState blockBelow = level.getBlockState(pos);
//            if (blockBelow.getBlock() == ModBlocks.milkJar) {
//                level.setBlockAndUpdate(pos, ModBlocks.cowJar.defaultBlockState());
//                BlockEntity tileEntity = level.getBlockEntity(pos);
//                if (tileEntity instanceof CowJarBlockEntity && entity.getCustomName() != null) {
//                    Component textComponent = new TranslatableComponent("container.cookingforblockheads.cow_jar_custom", entity.getCustomName());
//                    ((CowJarBlockEntity) tileEntity).setCustomName(textComponent);
//                }
//
//                // Ex Compressum compat for compressed cows
//                boolean wasCompressed = event.getEntity().getPersistentData().getCompound(Compat.EX_COMPRESSUM).getBoolean("Compressed");
//                if (wasCompressed && tileEntity instanceof CowJarBlockEntity) {
//                    ((CowJarBlockEntity) tileEntity).setCompressedCow(true);
//                }
//            }
//            Balm.getNetworking().sendToTracking(entity, new SyncedEffectMessage(pos, SyncedEffectMessage.Type.COW_IN_A_JAR));
//            entity.remove(Entity.RemovalReason.DISCARDED);
//            event.setCanceled(true);
//        }
//    }
//
//    public boolean isCow(LivingEntity entity) {
//        if (entity instanceof Cow) {
//            return true;
//        }
//
//        for (Class<? extends LivingEntity> clazz : additionalCowClasses) {
//            if (clazz.isAssignableFrom(entity.getClass())) {
//                return true;
//            }
//        }
//
//        ResourceLocation registryName = entity.getType().getRegistryName();
//        return registryName != null && registryName.getPath().contains("cow");
//    }
//
//}
