package net.blay09.mods.cookingforblockheads.client.gui;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.ContainerFreezer;
import net.blay09.mods.cookingforblockheads.tile.TileFreezer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFreezer extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
    private final int inventoryRows;

    public GuiFreezer(EntityPlayer player, TileFreezer tileFreezer) {
        super(new ContainerFreezer(player, tileFreezer));
        this.inventoryRows = tileFreezer.getItemHandler().getSlots() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    /**
     * Mojang HQ, on a snowy winter day
     * "One of our new GUIs has different tooltip handling than the 14 others"
     * "Let's change the 14 others"
     * "It doesn't matter that we'll be pasting the same code into each of those 14"
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container." + CookingForBlockheads.MOD_ID + ":freezer"), 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(guiLeft, guiTop + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

}
