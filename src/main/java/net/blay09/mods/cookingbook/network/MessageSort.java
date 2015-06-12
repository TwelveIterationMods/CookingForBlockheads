package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageSort implements IMessage {

    private int sortingId;

    public MessageSort() {}

    public MessageSort(int sortingId) {
        this.sortingId = sortingId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sortingId = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(sortingId);
    }


    public int getSortingId() {
        return sortingId;
    }
}
