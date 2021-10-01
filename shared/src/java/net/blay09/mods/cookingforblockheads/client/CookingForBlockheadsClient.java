package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;

public class CookingForBlockheadsClient {
    public static void initialize() {
        BalmClient.initialize(CookingForBlockheads.MOD_ID);

        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModTextures.initialize(BalmClient.getTextures());
    }
}
