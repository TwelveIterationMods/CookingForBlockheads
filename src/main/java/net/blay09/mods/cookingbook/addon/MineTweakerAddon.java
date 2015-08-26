package net.blay09.mods.cookingbook.addon;

import cpw.mods.fml.common.Optional;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;
import net.blay09.mods.cookingbook.food.FoodRegistry;

@Optional.Interface(modid = "MineTweaker3", iface = "minetweaker.util.IEventHandler", striprefs = true)
public class MineTweakerAddon implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent> {

    public MineTweakerAddon() {
        MineTweakerImplementationAPI.onReloadEvent(this);
    }

    @Override
    public void handle(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        FoodRegistry.init();
    }
}
