package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.blay09.mods.cookingbook.CookingBook;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(CookingBook.MOD_ID);

    public static void init() {
        instance.registerMessage(HandlerSort.class, MessageSort.class, 0, Side.CLIENT);
    }

}
