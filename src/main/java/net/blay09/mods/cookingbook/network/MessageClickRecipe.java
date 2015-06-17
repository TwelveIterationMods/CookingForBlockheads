package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageClickRecipe implements IMessage {

    private int slotIndex;
    private int scrollOffset;
    private boolean shiftClick;

    public MessageClickRecipe() {}

    public MessageClickRecipe(int slotIndex, int scrollOffset, boolean shiftClick) {
        this.slotIndex = slotIndex;
        this.scrollOffset = scrollOffset;
        this.shiftClick = shiftClick;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slotIndex = buf.readByte();
        scrollOffset = buf.readByte();
        shiftClick = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(slotIndex);
        buf.writeByte(scrollOffset);
        buf.writeBoolean(shiftClick);
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public boolean isShiftClick() {
        return shiftClick;
    }
}
