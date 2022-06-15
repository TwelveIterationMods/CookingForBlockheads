package net.blay09.mods.cookingforblockheads.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.List;

public class SortButton extends Button {

    private final ISortButton button;

    private final List<Component> tooltipLines = Lists.newArrayList();

    public SortButton(int x, int y, ISortButton button, OnPress onPress) {
        super(x, y, 20, 20, Component.empty(), onPress);
        this.button = button;
        this.tooltipLines.add(Component.translatable(this.button.getTooltip()));
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int texY = button.getIconTextureY();
        if (!active) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, button.getIcon());
        blit(poseStack, x, y, button.getIconTextureX(), texY, width, height);
    }

    public List<Component> getTooltipLines() {
        return tooltipLines;
    }

    public Comparator<FoodRecipeWithStatus> getComparator(Player player) {
        return button.getComparator(player);
    }
}
