package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.SpiceRackMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class SpiceRackScreen extends AbstractContainerScreen<SpiceRackMenu> {

    private static final Identifier texture = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "textures/gui/spice_rack.png");

    public SpiceRackScreen(SpiceRackMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName, DEFAULT_IMAGE_WIDTH, 132);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        extractTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

}
