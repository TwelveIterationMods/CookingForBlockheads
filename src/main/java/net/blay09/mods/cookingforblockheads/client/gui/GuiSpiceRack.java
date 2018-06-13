package net.blay09.mods.cookingforblockheads.client.gui;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.ContainerSpiceRack;
import net.blay09.mods.cookingforblockheads.tile.TileSpiceRack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSpiceRack extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/spice_rack.png");

    public GuiSpiceRack(EntityPlayer player, TileSpiceRack tileSpiceRack) {
        super(new ContainerSpiceRack(player, tileSpiceRack));
        ySize = 132;
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
        fontRenderer.drawString(I18n.format("container." + CookingForBlockheads.MOD_ID + ":spice_rack"), 8, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, 38, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

}
