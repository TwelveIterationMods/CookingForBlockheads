package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.inventory.Container;

public class HandlerRecipeInfo implements IMessageHandler<MessageRecipeInfo, IMessage> {

    @Override
    public IMessage onMessage(MessageRecipeInfo message, MessageContext ctx) {
        Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
        if(container instanceof ContainerRecipeBook) {
            ((ContainerRecipeBook) container).setSelectedRecipe(message.slotIndex, message.recipe, message.hasVariants, message.isMissingTools, message.canSmelt);
        }
        return null;
    }

}
