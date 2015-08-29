package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

public class HandlerTileEntity implements IMessageHandler<MessageTileEntity, IMessage> {

    @Override
    public IMessage onMessage(MessageTileEntity message, MessageContext ctx) {
        TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.getX(), message.getY(), message.getZ());
        tileEntity.readFromNBT(message.getTagCompound());
        return null;
    }

}
