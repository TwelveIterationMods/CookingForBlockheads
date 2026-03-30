package net.blay09.mods.cookingforblockheads.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;

public class PopulateCookingRegistryEvent extends BalmEvent {

    private final RecipeManager recipeManager;
    private final RegistryAccess registryAccess;

    public PopulateCookingRegistryEvent(RecipeManager recipeManager, RegistryAccess registryAccess) {
        this.recipeManager = recipeManager;
        this.registryAccess = registryAccess;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }
}
