package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class MessageTileEntity implements IMessage {

    private int x;
    private int y;
    private int z;
    private NBTTagCompound tagCompound;

    public MessageTileEntity() {}

    public MessageTileEntity(int x, int y, int z, NBTTagCompound tagCompound) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tagCompound = tagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        tagCompound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufUtils.writeTag(buf, tagCompound);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public NBTTagCompound getTagCompound() {
        return tagCompound;
    }
}
