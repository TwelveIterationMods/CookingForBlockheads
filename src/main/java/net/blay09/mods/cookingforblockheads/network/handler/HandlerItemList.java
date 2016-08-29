package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.ContainerRecipeBook;
import net.blay09.mods.cookingforblockheads.network.message.MessageItemList;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerItemList implements IMessageHandler<MessageItemList, IMessage> {

    @Override
    public IMessage onMessage(final MessageItemList message, MessageContext ctx) {
		CookingForBlockheads.proxy.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
				if(container instanceof ContainerRecipeBook) {
					((ContainerRecipeBook) container).setItemList(message.getRecipeList());
					((ContainerRecipeBook) container).setHasOven(message.getHasOven());
				}
			}
		});
        return null;
    }

}
