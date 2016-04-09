package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageCraftRecipe implements IMessage {

    private int id;
    private boolean stack;

    public MessageCraftRecipe() {}

    public MessageCraftRecipe(int id, boolean stack) {
        this.id = id;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        stack = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeBoolean(stack);
    }

    public int getId() {
        return id;
    }

    public boolean isStack() {
        return stack;
    }
}
