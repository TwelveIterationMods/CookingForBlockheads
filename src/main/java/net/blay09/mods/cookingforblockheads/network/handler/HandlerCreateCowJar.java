package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageCreateCowJar;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerCreateCowJar implements IMessageHandler<MessageCreateCowJar, IMessage> {

	@Override
	@Nullable
	public IMessage onMessage(final MessageCreateCowJar message, MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				Minecraft.getMinecraft().world.playSound(message.getPos().getX(), message.getPos().getY(), message.getPos().getZ(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f, false);
				Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, true, message.getPos().getX(), message.getPos().getY() + 1, message.getPos().getZ(), 0f, 0f, 0f);
			}
		});
		return null;
	}

}
