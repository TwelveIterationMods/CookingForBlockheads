package net.blay09.mods.cookingforblockheads.client.gui;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.List;

public class SortButton extends Button {

    private final ISortButton button;

    private final List<Component> tooltipLines = Lists.newArrayList();

    public SortButton(int x, int y, ISortButton button, OnPress onPress) {
        super(x, y, 20, 20, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.button = button;
        this.tooltipLines.add(Component.translatable(this.button.getTooltip()));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

        int texY = button.getIconTextureY();
        if (!active) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }

        guiGraphics.setColor(1f, 1f, 1f, 1f);
        guiGraphics.blit(button.getIcon(), getX(), getY(), button.getIconTextureX(), texY, width, height);
    }

    public List<Component> getTooltipLines() {
        return tooltipLines;
    }

    public Comparator<RecipeWithStatus> getComparator(Player player) {
        return button.getComparator(player);
    }
}
