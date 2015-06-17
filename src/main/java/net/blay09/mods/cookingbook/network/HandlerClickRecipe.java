package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.inventory.Container;

public class HandlerClickRecipe implements IMessageHandler<MessageClickRecipe, IMessage> {

    @Override
    public IMessage onMessage(MessageClickRecipe message, MessageContext ctx) {
        Container container = ctx.getServerHandler().playerEntity.openContainer;
        if(container instanceof ContainerRecipeBook) {
            ((ContainerRecipeBook) container).setScrollOffset(message.getScrollOffset());
            ((ContainerRecipeBook) container).clickRecipe(message.getSlotIndex(), message.isShiftClick());
        }
        return null;
    }

}
