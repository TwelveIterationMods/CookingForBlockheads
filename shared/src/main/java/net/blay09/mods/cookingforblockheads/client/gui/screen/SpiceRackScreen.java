package net.blay09.mods.cookingforblockheads.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.SpiceRackMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpiceRackScreen extends AbstractContainerScreen<SpiceRackMenu> {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/spice_rack.png");

    public SpiceRackScreen(SpiceRackMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        imageHeight = 132;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

}
