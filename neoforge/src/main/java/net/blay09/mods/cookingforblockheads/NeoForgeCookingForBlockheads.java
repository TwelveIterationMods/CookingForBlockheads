package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(CookingForBlockheads.MOD_ID)
public class NeoForgeCookingForBlockheads {

    public static Capability<IKitchenConnector> KITCHEN_CONNECTOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static Capability<IKitchenItemProvider> KITCHEN_ITEM_PROVIDER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static Capability<IKitchenSmeltingProvider> KITCHEN_SMELTING_PROVIDER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public NeoForgeCookingForBlockheads() {
        Balm.getEvents().onEvent(OvenItemSmeltedEvent.class, orig -> {
            PlayerEvent.ItemSmeltedEvent event = new PlayerEvent.ItemSmeltedEvent(orig.getPlayer(), orig.getResultItem());
            NeoForge.EVENT_BUS.post(event);
        });

        Balm.initialize(CookingForBlockheads.MOD_ID, CookingForBlockheads::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(CookingForBlockheads.MOD_ID, CookingForBlockheadsClient::initialize));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCapabilities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        NeoForge.EVENT_BUS.addListener(IMCHandler::onFoodRegistryInit);

        NeoForgeBalmProviders providers = (NeoForgeBalmProviders) Balm.getProviders();
        providers.register(IKitchenItemProvider.class, new CapabilityToken<>() {
        });
        providers.register(IKitchenSmeltingProvider.class, new CapabilityToken<>() {
        });
        providers.register(IKitchenConnector.class, new CapabilityToken<>() {
        });
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IKitchenConnector.class);
        event.register(IKitchenItemProvider.class);
        event.register(IKitchenSmeltingProvider.class);
    }
}
