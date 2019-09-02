package net.blay09.mods.cookingforblockheads.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncedEffect {

    public enum Type {
        COW_IN_A_JAR,
        OVEN_UPGRADE,
        FRIDGE_UPGRADE
    }

    private final BlockPos pos;
    private final Type type;

    public MessageSyncedEffect(BlockPos pos, Type type) {
        this.pos = pos;
        this.type = type;
    }

    public static void encode(MessageSyncedEffect message, PacketBuffer buf) {
        buf.writeLong(message.pos.toLong());
        buf.writeByte(message.type.ordinal());
    }

    public static MessageSyncedEffect decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        Type type = Type.values()[buf.readByte()];
        return new MessageSyncedEffect(pos, type);
    }

    public static void handle(MessageSyncedEffect message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            BasicParticleType particleType = ParticleTypes.EXPLOSION;
            int particleCount = 1;
            float particleRandomOffset = 1f;
            Vec3i particleOffset = Vec3i.NULL_VECTOR;
            SoundEvent soundEvent = null;
            float volume = 1f;
            switch (message.type) {
                case COW_IN_A_JAR:
                    soundEvent = SoundEvents.ENTITY_CHICKEN_EGG;
                    particleOffset = new Vec3i(0, 1, 0);
                    break;
                case OVEN_UPGRADE:
                case FRIDGE_UPGRADE:
                    soundEvent = SoundEvents.BLOCK_ANVIL_USE;
                    particleType = ParticleTypes.LARGE_SMOKE;
                    particleCount = 10;
                    volume = 0.5f;
                    break;
            }

            if (soundEvent != null) {
                Minecraft.getInstance().world.playSound(message.pos.getX(), message.pos.getY(), message.pos.getZ(), soundEvent, SoundCategory.BLOCKS, volume, 1f, false);
            }

            for (int i = 0; i < particleCount; i++) {
                float offsetX = particleOffset.getX() + 0.5f + (float) (Math.random() - 0.5f) * particleRandomOffset;
                float offsetY = particleOffset.getX() + 0.5f + (float) (Math.random() - 0.5f) * particleRandomOffset;
                float offsetZ = particleOffset.getX() + 0.5f + (float) (Math.random() - 0.5f) * particleRandomOffset;
                Minecraft.getInstance().world.addParticle(particleType, true, message.pos.getX() + offsetX, message.pos.getY() + offsetY, message.pos.getZ() + offsetZ, 0f, 0f, 0f);
            }
        });
    }

}
