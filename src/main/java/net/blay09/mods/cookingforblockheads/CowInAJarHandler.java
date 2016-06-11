package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CowInAJarHandler {

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		if(event.getSource() == DamageSource.anvil && event.getEntity() instanceof EntityCow) {
			IBlockState blockBelow = event.getEntity().getEntityWorld().getBlockState(event.getEntity().getPosition().down());
			if(blockBelow.getBlock() == ModBlocks.fridge) {
				event.getEntity().getEntityWorld().setBlockState(event.getEntity().getPosition().down(), ModBlocks.oven.getDefaultState());
			}
		}
	}

}
