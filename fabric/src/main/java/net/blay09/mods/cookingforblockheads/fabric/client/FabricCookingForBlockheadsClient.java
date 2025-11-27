package net.blay09.mods.cookingforblockheads.fabric.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.fabricmc.api.ClientModInitializer;

public class FabricCookingForBlockheadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(CookingForBlockheads.MOD_ID, FabricLoadContext.INSTANCE, CookingForBlockheadsClient::initialize);
    }
}
