package net.blay09.mods.cookingbook.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonSort extends GuiButton {

    private static final ResourceLocation guiTexture = new ResourceLocation("cookingbook", "textures/gui/gui.png");

    private final int texCoordX;
    private final int texCoordY;
    private final int texCoordHoverX;
    private final int texCoordHoverY;
    private final int texCoordDisabledX;
    private final int texCoordDisabledY;
    private final List<String> tooltipLines = new ArrayList<String>();

    public GuiButtonSort(int buttonId, int x, int y, int texCoordX, String tooltipName) {
        super(buttonId, x, y, 20, 20, "");
        this.texCoordX = texCoordX;
        this.texCoordHoverX = texCoordX;
        this.texCoordDisabledX = texCoordX;
        this.texCoordY = 0;
        this.texCoordHoverY = 20;
        this.texCoordDisabledY = 40;
        this.tooltipLines.add(I18n.format(tooltipName));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

        int texX = texCoordX;
        int texY = texCoordY;
        if(!enabled) {
            texX = texCoordDisabledX;
            texY = texCoordDisabledY;
        } else if(field_146123_n) {
            texX = texCoordHoverX;
            texY = texCoordHoverY;
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(xPosition, yPosition, texX, texY, width, height);
    }

    public List<String> getTooltipLines() {
        return tooltipLines;
    }
}
