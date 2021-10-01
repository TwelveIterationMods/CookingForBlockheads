package net.blay09.mods.cookingforblockheads.client;

import net.fabricmc.api.ClientModInitializer;

public class FabricCookingForBlockheadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CookingForBlockheadsClient.initialize();
    }
}
