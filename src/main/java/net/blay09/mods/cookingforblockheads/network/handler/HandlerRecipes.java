package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.container.ContainerRecipeBook;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageRecipes;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerRecipes implements IMessageHandler<MessageRecipes, IMessage> {

    @Override
    @Nullable
    public IMessage onMessage(final MessageRecipes message, MessageContext ctx) {
        NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> {
            Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
            if (container instanceof ContainerRecipeBook) {
                ((ContainerRecipeBook) container).setRecipeList(message.getOutputItem(), message.getRecipeList());
            }
        });
        return null;
    }

}
