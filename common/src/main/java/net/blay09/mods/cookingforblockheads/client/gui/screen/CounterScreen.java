package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.cookingforblockheads.menu.CounterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CounterScreen extends AbstractContainerScreen<CounterMenu> {

    private static final ResourceLocation texture = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private final int inventoryRows;

    public CounterScreen(CounterMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        this.inventoryRows = container.getNumRows();
        this.imageHeight = 114 + this.inventoryRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.setColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(texture, leftPos, topPos, 0, 0, this.imageWidth, this.inventoryRows * 18 + 17);
        guiGraphics.blit(texture, leftPos, topPos + this.inventoryRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

}
