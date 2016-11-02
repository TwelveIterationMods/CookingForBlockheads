package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.container.ContainerRecipeBook;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageCraftRecipe;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerCraftRecipe implements IMessageHandler<MessageCraftRecipe, IMessage> {

    @Override
    public IMessage onMessage(final MessageCraftRecipe message, final MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				Container container = ctx.getServerHandler().playerEntity.openContainer;
				if(container instanceof ContainerRecipeBook) {
					((ContainerRecipeBook) container).tryCraft(message.getOutputItem(), message.getRecipeType(), message.getCraftMatrix(), message.isStack());
				}
			}
		});
        return null;
    }

}
