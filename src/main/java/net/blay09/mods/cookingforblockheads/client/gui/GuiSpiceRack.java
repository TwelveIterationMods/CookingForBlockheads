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

	private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/spiceRack.png");

	public GuiSpiceRack(EntityPlayer player, TileSpiceRack tileSpiceRack) {
		super(new ContainerSpiceRack(player, tileSpiceRack));
		ySize = 132;
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(I18n.format("container." + CookingForBlockheads.MOD_ID + ":spiceRack"), 8, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, 38, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1f, 1f, 1f);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
