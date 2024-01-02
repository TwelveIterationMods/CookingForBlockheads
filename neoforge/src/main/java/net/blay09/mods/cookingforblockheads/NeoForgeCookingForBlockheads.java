package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(CookingForBlockheads.MOD_ID)
public class NeoForgeCookingForBlockheads {

    public NeoForgeCookingForBlockheads(IEventBus eventBus) {
        Balm.getEvents().onEvent(OvenItemSmeltedEvent.class, orig -> {
            PlayerEvent.ItemSmeltedEvent event = new PlayerEvent.ItemSmeltedEvent(orig.getPlayer(), orig.getResultItem());
            NeoForge.EVENT_BUS.post(event);
        });

        Balm.initialize(CookingForBlockheads.MOD_ID, CookingForBlockheads::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(CookingForBlockheads.MOD_ID, CookingForBlockheadsClient::initialize));

        eventBus.addListener(IMCHandler::processIMC);
        eventBus.addListener(this::enqueueIMC);
        NeoForge.EVENT_BUS.addListener(IMCHandler::onFoodRegistryInit);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }
}
