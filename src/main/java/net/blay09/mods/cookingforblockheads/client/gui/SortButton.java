package net.blay09.mods.cookingforblockheads.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Comparator;
import java.util.List;

public class SortButton extends Button {

    private final ISortButton button;

    private final List<String> tooltipLines = Lists.newArrayList();

    public SortButton(int x, int y, ISortButton button, IPressable onPress) {
        super(x, y, 20, 20, "", onPress);
        this.button = button;
        this.tooltipLines.add(I18n.format(this.button.getTooltip()));
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int texY = button.getIconTextureY();
        if (!active) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }

        RenderSystem.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(this.button.getIcon());
        blit(x, y, button.getIconTextureX(), texY, width, height);
    }

    public List<String> getTooltipLines() {
        return tooltipLines;
    }

    public Comparator<FoodRecipeWithStatus> getComparator(PlayerEntity player) {
        return button.getComparator(player);
    }
}
