package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CookingForBlockheads.MOD_ID)
public class ForgeCookingForBlockheads {
    public ForgeCookingForBlockheads() {
        CookingForBlockheads.initialize();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CookingForBlockheadsClient::initialize);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCapabilities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.addListener(IMCHandler::onFoodRegistryInit);

        ForgeMod.enableMilkFluid();
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if(Balm.isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IKitchenConnector.class);
        event.register(IKitchenItemProvider.class);
        event.register(IKitchenSmeltingProvider.class);
    }
}
