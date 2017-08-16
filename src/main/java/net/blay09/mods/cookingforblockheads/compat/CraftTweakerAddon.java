package net.blay09.mods.cookingforblockheads.compat;

import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = Compat.CRAFTTWEAKER, iface = "minetweaker.util.IEventHandler", striprefs = true)
public class CraftTweakerAddon implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent> {

    public CraftTweakerAddon() {
        MineTweakerImplementationAPI.onPostReload(this);
    }

    @Override
    public void handle(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        CookingRegistry.initFoodRegistry();
    }
}
