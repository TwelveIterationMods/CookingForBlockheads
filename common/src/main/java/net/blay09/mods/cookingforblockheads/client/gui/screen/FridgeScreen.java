package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.cookingforblockheads.menu.FridgeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FridgeScreen extends AbstractContainerScreen<FridgeMenu> {

    private static final Identifier texture = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
    private final int inventoryRows;

    public FridgeScreen(FridgeMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName, DEFAULT_IMAGE_WIDTH, 114 + container.getNumRows() * 18);
        this.inventoryRows = container.getNumRows();
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        extractTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0, 0, this.imageWidth, this.inventoryRows * 18 + 17, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos + this.inventoryRows * 18 + 17, 0, 126, this.imageWidth, 96, 256, 256);
    }

}
