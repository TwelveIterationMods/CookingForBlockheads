package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageCreateCowJar;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CowJarHandler {

	@SubscribeEvent
	public void onEntityDamage(LivingAttackEvent event) {
		if(event.getSource() == DamageSource.anvil && event.getEntity() instanceof EntityCow) {
			BlockPos pos = event.getEntity().getPosition().down();
			IBlockState blockBelow = event.getEntity().getEntityWorld().getBlockState(pos);
			if(blockBelow.getBlock() == ModBlocks.milkJar) {
				event.getEntity().getEntityWorld().setBlockState(pos, ModBlocks.cowJar.getDefaultState());
			}
			NetworkHandler.instance.sendToAllAround(new MessageCreateCowJar(pos), new NetworkRegistry.TargetPoint(event.getEntity().worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
			event.getEntity().setDead();
			event.setCanceled(true);
		}
	}

}
