package net.blay09.mods.cookingforblockheads.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.List;

public class SortButton extends Button {

    private final ISortButton button;

    private final List<String> tooltipLines = Lists.newArrayList();

    public SortButton(int buttonId, int x, int y, ISortButton button) {
        super(buttonId, x, y, 20, 20, "");
        this.button = button;
        this.tooltipLines.add(I18n.format(this.button.getTooltip()));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int texY = button.getIconTextureY();
        if (!enabled) {
            texY += 40;
        } else if (hovered) {
            texY += 20;
        }

        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(this.button.getIcon());
        drawTexturedModalRect(x, y, button.getIconTextureX(), texY, width, height);
    }

    public List<String> getTooltipLines() {
        return tooltipLines;
    }

    public Comparator<FoodRecipeWithStatus> getComparator(PlayerEntity player) {
        return button.getComparator(player);
    }
}
