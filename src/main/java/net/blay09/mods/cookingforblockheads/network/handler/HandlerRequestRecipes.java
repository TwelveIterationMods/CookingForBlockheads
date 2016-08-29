package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.MessageRequestRecipes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerRequestRecipes implements IMessageHandler<MessageRequestRecipes, IMessage> {

    @Override
    public IMessage onMessage(final MessageRequestRecipes message, MessageContext ctx) {
		CookingForBlockheads.proxy.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				// TODO send MessageRecipes to client
			}
		});
        return null;
    }

}
