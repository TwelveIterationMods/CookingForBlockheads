package net.blay09.mods.craftingtweaks;

import net.blay09.mods.craftingtweaks.client.CraftingTweaksClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

@Mod(CraftingTweaks.MOD_ID)
public class ForgeCraftingTweaks {
    public ForgeCraftingTweaks() {
        CraftingTweaks.initialize();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CraftingTweaksClient::initialize);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processInterMod);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

}
