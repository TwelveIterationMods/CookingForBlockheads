package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = CookingForBlockheads.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeCookingForBlockheadsClient {

    public NeoForgeCookingForBlockheadsClient(IEventBus eventBus) {
        final var context = new NeoForgeLoadContext(eventBus);
       BalmClient.initialize(CookingForBlockheads.MOD_ID, context, CookingForBlockheadsClient::initialize);
    }
}
