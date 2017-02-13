package net.blay09.mods.cookingforblockheads.client.gui;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.ContainerFridge;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFridge extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
    private int inventoryRows;

    public GuiFridge(EntityPlayer player, TileFridge tileFridge) {
        super(new ContainerFridge(player, tileFridge));
        this.inventoryRows = tileFridge.getCombinedItemHandler().getSlots() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.fontRenderer.drawString(I18n.format("container." + CookingForBlockheads.MOD_ID + ":fridge"), 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GlStateManager.color(1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(guiLeft, guiTop + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

}
