package net.blay09.mods.cookingbook.addon;

import codechicken.nei.api.API;
import codechicken.nei.api.GuiInfo;
import net.blay09.mods.cookingbook.client.GuiRecipeBook;
import net.blay09.mods.cookingbook.container.SlotCraftMatrix;
import net.blay09.mods.cookingbook.container.SlotRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotEnoughItemsAddon {

    private static final Logger logger = LogManager.getLogger();

    public NotEnoughItemsAddon() {
        logger.info("Disabling mouse wheel transfer for Cooking Book slots...");
        GuiInfo.customSlotGuis.add(GuiRecipeBook.class);
        API.addFastTransferExemptSlot(SlotRecipe.class);
        API.addFastTransferExemptSlot(SlotCraftMatrix.class);
    }

}
