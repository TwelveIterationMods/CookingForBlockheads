package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.*;
import net.blay09.mods.cookingforblockheads.network.message.MessageCraftRecipe;
import net.blay09.mods.cookingforblockheads.network.message.MessageCreateCowJar;
import net.blay09.mods.cookingforblockheads.network.message.MessageItemList;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(CookingForBlockheads.MOD_ID);

    public static void init() {
        instance.registerMessage(HandlerItemList.class, MessageItemList.class, 0, Side.CLIENT);
        instance.registerMessage(HandlerCraftRecipe.class, MessageCraftRecipe.class, 1, Side.SERVER);
        instance.registerMessage(HandlerCreateCowJar.class, MessageCreateCowJar.class, 2, Side.CLIENT);
    }

}
