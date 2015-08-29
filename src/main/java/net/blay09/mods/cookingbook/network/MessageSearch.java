package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageSearch implements IMessage {

    private String term;

    public MessageSearch() {}

    public MessageSearch(String term) {
        this.term = term;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        term = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, term);
    }

    public String getTerm() {
        return term;
    }
}
