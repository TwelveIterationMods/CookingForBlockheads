package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.cookingforblockheads.menu.CookieJarMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class CookieJarScreen extends AbstractContainerScreen<CookieJarMenu> {

    private static final Identifier TEXTURE = id("textures/gui/container/cookie_jar.png");

    public CookieJarScreen(CookieJarMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName, DEFAULT_IMAGE_WIDTH, 133);
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
