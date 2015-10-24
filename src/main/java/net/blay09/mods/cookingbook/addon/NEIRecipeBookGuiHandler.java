package net.blay09.mods.cookingbook.addon;

import codechicken.nei.api.INEIGuiAdapter;
import net.blay09.mods.cookingbook.client.GuiButtonSort;
import net.blay09.mods.cookingbook.client.GuiRecipeBook;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.util.Rectangle;

public class NEIRecipeBookGuiHandler extends INEIGuiAdapter {

    public static NEIRecipeBookGuiHandler instance = new NEIRecipeBookGuiHandler();
    private static Rectangle tmpPanelRect = new Rectangle();
    private static Rectangle tmpButtonRect = new Rectangle();

    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        if(gui instanceof GuiRecipeBook) {
            GuiButtonSort[] sortButtons = ((GuiRecipeBook) gui).getSortButtons();
            if(sortButtons != null) {
                tmpPanelRect.setBounds(x, y, w, h);
                for(GuiButtonSort button : sortButtons) {
                    tmpButtonRect.setBounds(button.xPosition, button.yPosition, button.width, button.height);
                    if(tmpPanelRect.intersects(tmpButtonRect)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
