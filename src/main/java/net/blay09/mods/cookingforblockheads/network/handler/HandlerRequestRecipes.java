package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.container.ContainerRecipeBook;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageRequestRecipes;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerRequestRecipes implements IMessageHandler<MessageRequestRecipes, IMessage> {

    @Override
    @Nullable
    public IMessage onMessage(final MessageRequestRecipes message, final MessageContext ctx) {
        NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> {
            Container container = ctx.getServerHandler().player.openContainer;
            if (container instanceof ContainerRecipeBook) {
                ((ContainerRecipeBook) container).findAndSendRecipes(message.getOutputItem());
            }
        });
        return null;
    }

}
