package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerSyncedEffect implements IMessageHandler<MessageSyncedEffect, IMessage> {

    @Override
    @Nullable
    public IMessage onMessage(final MessageSyncedEffect message, MessageContext ctx) {
        NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> {
            EnumParticleTypes particleTypes = EnumParticleTypes.EXPLOSION_LARGE;
            int particleCount = 1;
            float particleRandomOffset = 1f;
            Vec3i particleOffset = Vec3i.NULL_VECTOR;
            SoundEvent soundEvent = null;
            float volume = 1f;
            switch (message.getType()) {
                case COW_IN_A_JAR:
                    soundEvent = SoundEvents.ENTITY_CHICKEN_EGG;
                    particleOffset = new Vec3i(0, 1, 0);
                    break;
                case OVEN_UPGRADE:
                case FRIDGE_UPGRADE:
                    soundEvent = SoundEvents.BLOCK_ANVIL_USE;
                    particleTypes = EnumParticleTypes.SMOKE_LARGE;
                    particleCount = 10;
                    volume = 0.5f;
                    break;
            }

            if (soundEvent != null) {
                Minecraft.getMinecraft().world.playSound(message.getPos().getX(), message.getPos().getY(), message.getPos().getZ(), soundEvent, SoundCategory.BLOCKS, volume, 1f, false);
            }

            for (int i = 0; i < particleCount; i++) {
                float offsetX = particleOffset.getX() + 0.5f + (float) (Math.random() - 0.5f) * particleRandomOffset;
                float offsetY = particleOffset.getX() + 0.5f + (float) (Math.random() - 0.5f) * particleRandomOffset;
                float offsetZ = particleOffset.getX() + 0.5f + (float) (Math.random() - 0.5f) * particleRandomOffset;
                Minecraft.getMinecraft().world.spawnParticle(particleTypes, true, message.getPos().getX() + offsetX, message.getPos().getY() + offsetY, message.getPos().getZ() + offsetZ, 0f, 0f, 0f);
            }
        });

        return null;
    }

}
