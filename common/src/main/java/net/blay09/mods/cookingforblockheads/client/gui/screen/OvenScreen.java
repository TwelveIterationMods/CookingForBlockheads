package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.OvenMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OvenScreen extends AbstractContainerScreen<OvenMenu> {

    private static final Identifier texture = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "textures/gui/oven.png");

    public OvenScreen(OvenMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName, DEFAULT_IMAGE_WIDTH + 22, 193);
        this.titleLabelX += 22;
        this.inventoryLabelX += 22;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractContents(guiGraphics, mouseX, mouseY, partialTicks);

        OvenBlockEntity tileEntity = menu.getOven();
        if (tileEntity.hasPowerUpgrade() && mouseX >= leftPos + imageWidth - 25 && mouseY >= topPos + 22 && mouseX < leftPos + imageWidth - 25 + 35 + 18 && mouseY < topPos + 22 + 72) {
            final var energyStorage = tileEntity.getEnergyStorage();
            guiGraphics.setTooltipForNextFrame(font, Component.translatable("tooltip.cookingforblockheads.energy_stored", energyStorage.getEnergy(), energyStorage.getCapacity()), mouseX, mouseY);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractLabels(guiGraphics, mouseX, mouseY);

        final var oven = menu.getOven();
        for (int i = 0; i < 9; i++) {
            Slot slot = menu.slots.get(i + 7);
            if (slot.hasItem()) {
                ItemStack itemStack = menu.getResultItems().get(i);
                if (!itemStack.isEmpty()) {
                    final var pose = guiGraphics.pose();
                    pose.pushMatrix();
                    // TODO 1.21.6: RenderSystem.setShaderColor(1f, 1f, 1f, oven.getCookProgress(i));
                    // TODO 1.21.6: guiGraphics.renderItem(itemStack, slot.x, slot.y);
                    // TODO 1.21.6: RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                    pose.popMatrix();
                }
            }
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        // Draw background
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 22, topPos, 0, 0, imageWidth - 22, imageHeight, 256, 256);

        // Draw tool slots
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos + 10, 176, 30, 25, 87, 256, 256);

        OvenBlockEntity tileEntity = menu.getOven();
        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;

        // Draw main slots
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 22 + 61 + offsetX, topPos + 18, 176, 117, 76, 76, 256, 256);

        // Draw fuel slot
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 22 + 38 + offsetX, topPos + 43, 205, 84, 18, 33, 256, 256);

        // Draw fuel bar
        if (tileEntity.isBurning()) {
            int burnTime = (int) (12 * tileEntity.getBurnTimeProgress());
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + 22 + 40 + offsetX, topPos + 43 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1, 256, 256);
        }

        // Draw power bar
        if (tileEntity.hasPowerUpgrade()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + imageWidth - 25, topPos + 22, 205, 0, 18, 72, 256, 256);
            final var energyStorage = tileEntity.getEnergyStorage();
            float energyPercentage = energyStorage.getEnergy() / (float) energyStorage.getCapacity();
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos + imageWidth - 25 + 1, topPos + 22 + 1 + 70 - (int) (energyPercentage * 70), 223, 0, 16, (int) (energyPercentage * 70), 256, 256);
        }
    }

}
