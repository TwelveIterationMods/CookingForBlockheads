package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

@Mod(CookingForBlockheads.MOD_ID)
public class ForgeCookingForBlockheads {

    public ForgeCookingForBlockheads(FMLJavaModLoadingContext context) {
        final var loadContext = new ForgeLoadContext(context.getModEventBus());
        Balm.getEvents().onEvent(OvenItemSmeltedEvent.class, orig -> {
            PlayerEvent.ItemSmeltedEvent event = new PlayerEvent.ItemSmeltedEvent(orig.getPlayer(), orig.getResultItem());
            MinecraftForge.EVENT_BUS.post(event);
        });

        final var forgeCapabilities = (ForgeBalmCapabilities) Balm.getCapabilities();
        forgeCapabilities.preRegisterType(id("kitchen_item_provider"), CapabilityManager.get(new CapabilityToken<KitchenItemProvider>() {
        }));
        forgeCapabilities.preRegisterType(id("kitchen_item_processor"), CapabilityManager.get(new CapabilityToken<KitchenItemProcessor>() {
        }));

        Balm.initializeMod(CookingForBlockheads.MOD_ID, loadContext, CookingForBlockheads::initialize);
        if (FMLEnvironment.dist.isClient()) {
            BalmClient.initializeMod(CookingForBlockheads.MOD_ID, loadContext, CookingForBlockheadsClient::initialize);
        }

        context.getModEventBus().addListener(this::enqueueIMC);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }

}
