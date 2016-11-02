package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.*;
import net.blay09.mods.cookingforblockheads.network.message.MessageCraftRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageCreateCowJar;
import net.blay09.mods.cookingforblockheads.network.message.MessageItemList;
import net.blay09.mods.cookingforblockheads.network.message.MessageRecipes;
import net.blay09.mods.cookingforblockheads.network.message.MessageRequestRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(CookingForBlockheads.MOD_ID);

    public static void init() {
        instance.registerMessage(HandlerItemList.class, MessageItemList.class, 0, Side.CLIENT);
        instance.registerMessage(HandlerCraftRecipe.class, MessageCraftRecipe.class, 1, Side.SERVER);
        instance.registerMessage(HandlerCreateCowJar.class, MessageCreateCowJar.class, 2, Side.CLIENT);
        instance.registerMessage(HandlerRequestRecipes.class, MessageRequestRecipes.class, 3, Side.SERVER);
        instance.registerMessage(HandlerRecipes.class, MessageRecipes.class, 4, Side.CLIENT);
    }

    public static IThreadListener getThreadListener(MessageContext ctx) {
        return ctx.side == Side.SERVER ? (WorldServer) ctx.getServerHandler().playerEntity.worldObj : getClientThreadListener();
    }

    @SideOnly(Side.CLIENT)
    public static IThreadListener getClientThreadListener() {
        return Minecraft.getMinecraft();
    }

}
