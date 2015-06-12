package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageSwitchRecipe implements IMessage {

    private int direction;

    public MessageSwitchRecipe() {}

    public MessageSwitchRecipe(int direction) {
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        direction = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(direction);
    }


    public int getDirection() {
        return direction;
    }
}
