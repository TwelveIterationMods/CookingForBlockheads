package net.blay09.mods.cookingbook.client;

import net.blay09.mods.cookingbook.block.TileEntityCookingOven;
import net.blay09.mods.cookingbook.container.ContainerCookingOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCookingOven extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("cookingbook", "textures/gui/oven.png");
    private TileEntityCookingOven tileEntity;

    public GuiCookingOven(InventoryPlayer playerInventory, TileEntityCookingOven tileEntity) {
        super(new ContainerCookingOven(playerInventory, tileEntity));
        this.tileEntity = tileEntity;
        this.xSize += 22;
        this.ySize = 192;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format(tileEntity.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);

        drawTexturedModalRect(guiLeft + 22, guiTop, 0, 0, xSize - 22, ySize);
        drawTexturedModalRect(guiLeft, guiTop + 10, 176, 30, 25, 87);

        if(tileEntity.isBurning()) {
            int burnTime = (int) (12 * tileEntity.getBurnTimeProgress());
            drawTexturedModalRect(guiLeft + 40, guiTop + 43 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }
        drawTexturedModalRect(guiLeft + 62, guiTop + 41, 176, 14, (int) (16 * tileEntity.getCookProgress(0)), 16);
        drawTexturedModalRect(guiLeft + 80, guiTop + 41, 176, 14, (int) (16 * tileEntity.getCookProgress(1)), 16);
        drawTexturedModalRect(guiLeft + 98, guiTop + 41, 176, 14, (int) (16 * tileEntity.getCookProgress(2)), 16);
        drawTexturedModalRect(guiLeft + 62, guiTop + 59, 176, 14, (int) (16 * tileEntity.getCookProgress(3)), 16);
        drawTexturedModalRect(guiLeft + 80, guiTop + 59, 176, 14, (int) (16 * tileEntity.getCookProgress(4)), 16);
        drawTexturedModalRect(guiLeft + 98, guiTop + 59, 176, 14, (int) (16 * tileEntity.getCookProgress(5)), 16);
        drawTexturedModalRect(guiLeft + 62, guiTop + 77, 176, 14, (int) (16 * tileEntity.getCookProgress(6)), 16);
        drawTexturedModalRect(guiLeft + 80, guiTop + 77, 176, 14, (int) (16 * tileEntity.getCookProgress(7)), 16);
        drawTexturedModalRect(guiLeft + 98, guiTop + 77, 176, 14, (int) (16 * tileEntity.getCookProgress(8)), 16);
    }
}
