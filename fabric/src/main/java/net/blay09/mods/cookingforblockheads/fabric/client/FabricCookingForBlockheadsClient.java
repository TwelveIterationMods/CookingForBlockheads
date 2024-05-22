package net.blay09.mods.cookingforblockheads.fabric.client;

import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.fabricmc.api.ClientModInitializer;

public class FabricCookingForBlockheadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BalmClient.initialize(CookingForBlockheads.MOD_ID, EmptyLoadContext.INSTANCE, CookingForBlockheadsClient::initialize);
    }
}
