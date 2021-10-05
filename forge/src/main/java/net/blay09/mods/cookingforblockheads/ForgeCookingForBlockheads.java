package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.hooks.BasicEventHooks;
import net.minecraftforge.items.IItemHandler;

@Mod(CookingForBlockheads.MOD_ID)
public class ForgeCookingForBlockheads {

    @CapabilityInject(IKitchenConnector.class)
    public static Capability<IKitchenConnector> KITCHEN_CONNECTOR_CAPABILITY = null;

    @CapabilityInject(IKitchenItemProvider.class)
    public static Capability<IKitchenItemProvider> KITCHEN_ITEM_PROVIDER_CAPABILITY = null;

    @CapabilityInject(IKitchenSmeltingProvider.class)
    public static Capability<IKitchenSmeltingProvider> KITCHEN_SMELTING_PROVIDER_CAPABILITY = null;

    public ForgeCookingForBlockheads() {
        Balm.getEvents().onEvent(OvenItemSmeltedEvent.class, orig -> {
            PlayerEvent.ItemSmeltedEvent event = new PlayerEvent.ItemSmeltedEvent(orig.getPlayer(), orig.getResultItem());
            MinecraftForge.EVENT_BUS.post(event);
        });

        CookingForBlockheads.initialize();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CookingForBlockheadsClient::initialize);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCapabilities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.addListener(IMCHandler::onFoodRegistryInit);

        ForgeMod.enableMilkFluid();

        ForgeBalmProviders providers = (ForgeBalmProviders) Balm.getProviders();
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
