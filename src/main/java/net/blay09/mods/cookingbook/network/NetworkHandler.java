package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.blay09.mods.cookingbook.CookingBook;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(CookingBook.MOD_ID);

    public static void init() {
        instance.registerMessage(HandlerSort.class, MessageSort.class, 0, Side.SERVER);
        instance.registerMessage(HandlerSwitchRecipe.class, MessageSwitchRecipe.class, 1, Side.SERVER);
        instance.registerMessage(HandlerSyncList.class, MessageSyncList.class, 2, Side.CLIENT);
        instance.registerMessage(HandlerClickRecipe.class, MessageClickRecipe.class, 3, Side.SERVER);
        instance.registerMessage(HandlerRecipeInfo.class, MessageRecipeInfo.class, 4, Side.CLIENT);
        instance.registerMessage(HandlerSearch.class, MessageSearch.class, 5, Side.SERVER);
    }

}
