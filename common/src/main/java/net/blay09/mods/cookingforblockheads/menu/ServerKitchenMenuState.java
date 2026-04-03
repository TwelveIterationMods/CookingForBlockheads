package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;

public class ServerKitchenMenuState {
    private final CraftingContext craftingContext;
    private final boolean showUncraftable;

    public ServerKitchenMenuState(CraftingContext craftingContext, boolean showUncraftable) {
        this.craftingContext = craftingContext;
        this.showUncraftable = showUncraftable;
    }

    public CraftingContext craftingContext() {
        return craftingContext;
    }

    public boolean showUncraftable() {
        return showUncraftable;
    }
}
