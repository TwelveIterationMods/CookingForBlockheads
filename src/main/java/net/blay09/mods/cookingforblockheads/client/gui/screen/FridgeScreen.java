package net.blay09.mods.cookingforblockheads.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.container.FridgeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FridgeScreen extends ContainerScreen<FridgeContainer> {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
    private final int inventoryRows;

    public FridgeScreen(FridgeContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.inventoryRows = container.getNumRows();
        this.ySize = 114 + this.inventoryRows * 18;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        Minecraft minecraft = getMinecraft();
        minecraft.fontRenderer.drawString(getTitle().getFormattedText(), 8, 6, 4210752);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color3f(1f, 1f, 1f);
        getMinecraft().getTextureManager().bindTexture(texture);
        blit(guiLeft, guiTop, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        blit(guiLeft, guiTop + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

}
