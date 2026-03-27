package net.blay09.mods.cookingforblockheads.neoforge.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = CookingForBlockheads.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeCookingForBlockheadsClient {

    public NeoForgeCookingForBlockheadsClient(ModContainer modContainer, IEventBus eventBus) {
        final var context = new NeoForgeLoadContext(modContainer, eventBus);
       BalmClient.initializeMod(CookingForBlockheads.MOD_ID, context, CookingForBlockheadsClient::initialize);
    }
}
