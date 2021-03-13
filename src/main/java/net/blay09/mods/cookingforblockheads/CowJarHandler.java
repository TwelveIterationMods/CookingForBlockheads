package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.blay09.mods.cookingforblockheads.tile.CowJarTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
            Entity entity = event.getEntity();
            BlockPos pos = entity.getPosition();
            World world = entity.getEntityWorld();
            BlockState blockBelow = world.getBlockState(pos);
            if (blockBelow.getBlock() == ModBlocks.milkJar) {
                world.setBlockState(pos, ModBlocks.cowJar.getDefaultState());
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof CowJarTileEntity && entity.getCustomName() != null) {
                    ITextComponent textComponent = new TranslationTextComponent("container.cookingforblockheads.cow_jar_custom", entity.getCustomName());
                    ((CowJarTileEntity) tileEntity).setCustomName(textComponent);
                }

                // Ex Compressum compat for compressed cows
                boolean wasCompressed = event.getEntity().getPersistentData().getCompound(Compat.EX_COMPRESSUM).getBoolean("Compressed");
                if (wasCompressed && tileEntity instanceof CowJarTileEntity) {
                    ((CowJarTileEntity) tileEntity).setCompressedCow(true);
                }
            }
            NetworkHandler.sendToAllTracking(new MessageSyncedEffect(pos, MessageSyncedEffect.Type.COW_IN_A_JAR), entity);
            entity.remove();
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
