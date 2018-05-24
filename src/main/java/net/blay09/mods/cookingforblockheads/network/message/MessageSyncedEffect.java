package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageSyncedEffect implements IMessage {

    public enum Type {
        COW_IN_A_JAR,
        OVEN_UPGRADE
    }

    private BlockPos pos;
    private Type type;

    public MessageSyncedEffect() {
    }

    public MessageSyncedEffect(BlockPos pos, Type type) {
        this.pos = pos;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        type = Type.values()[buf.readByte()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeByte(type.ordinal());
    }

    public BlockPos getPos() {
        return pos;
    }

    public Type getType() {
        return type;
    }

}
