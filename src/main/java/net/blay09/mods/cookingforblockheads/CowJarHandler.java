package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageCreateCowJar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class CowJarHandler {

	private static final List<Class<? extends EntityLivingBase>> additionalCowClasses = Lists.newArrayList();

	@SuppressWarnings("unchecked")
	public static void registerCowClass(Class<?> clazz) {
		additionalCowClasses.add((Class<? extends EntityLivingBase>) clazz);
	}

	@SubscribeEvent
	public void onEntityDamage(LivingAttackEvent event) {
		if(!ModConfig.general.cowJarEnabled) {
			return;
		}
		if(event.getSource() == DamageSource.ANVIL && isCow(event.getEntityLiving())) {
			BlockPos pos = event.getEntity().getPosition().down();
			IBlockState blockBelow = event.getEntity().getEntityWorld().getBlockState(pos);
			if(blockBelow.getBlock() == ModBlocks.milkJar) {
				event.getEntity().getEntityWorld().setBlockState(pos, ModBlocks.cowJar.getDefaultState());
			}
			NetworkHandler.instance.sendToAllAround(new MessageCreateCowJar(pos), new NetworkRegistry.TargetPoint(event.getEntity().world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
			event.getEntity().setDead();
			event.setCanceled(true);
		}
	}

	public boolean isCow(EntityLivingBase entity) {
		if(entity instanceof EntityCow) {
			return true;
		}
		for(Class<? extends EntityLivingBase> clazz : additionalCowClasses) {
			if(clazz.isAssignableFrom(entity.getClass())) {
				return true;
			}
		}
		String registryName = EntityList.getEntityString(entity);
		return registryName != null && registryName.contains("cow");
	}

}
