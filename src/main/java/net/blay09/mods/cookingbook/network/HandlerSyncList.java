package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.blay09.mods.cookingbook.container.ComparatorHunger;
import net.blay09.mods.cookingbook.container.ComparatorName;
import net.blay09.mods.cookingbook.container.ComparatorSaturation;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.inventory.Container;

public class HandlerSyncList implements IMessageHandler<MessageSyncList, IMessage> {

    @Override
    public IMessage onMessage(MessageSyncList message, MessageContext ctx) {
        Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
        if(container instanceof ContainerRecipeBook) {
            ((ContainerRecipeBook) container).setAvailableItems(message.sortedRecipes, message.availableRecipes, message.getCurrentRecipeIndex());
        }
        return null;
    }

}
