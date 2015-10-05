package net.blay09.mods.cookingbook.addon;

import codechicken.nei.api.API;
import net.blay09.mods.cookingbook.container.SlotCraftMatrix;
import net.blay09.mods.cookingbook.container.SlotRecipe;

public class NotEnoughItemsAddon {

    public NotEnoughItemsAddon() {
        API.addFastTransferExemptSlot(SlotRecipe.class);
        API.addFastTransferExemptSlot(SlotCraftMatrix.class);
    }

}
