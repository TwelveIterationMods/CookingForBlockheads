package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.OvenMenu;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OvenScreen extends AbstractContainerScreen<OvenMenu> {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/oven.png");

    public OvenScreen(OvenMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        this.imageWidth += 22;
        this.imageHeight = 193;

        this.titleLabelX += 22;
        this.inventoryLabelX += 22;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);

        OvenBlockEntity tileEntity = menu.getTileEntity();
        if (tileEntity.hasPowerUpgrade() && mouseX >= leftPos + imageWidth - 25 && mouseY >= topPos + 22 && mouseX < leftPos + imageWidth - 25 + 35 + 18 && mouseY < topPos + 22 + 72) {
            EnergyStorage energyStorage = tileEntity.getEnergyStorage();
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.cookingforblockheads:energy_stored", energyStorage.getEnergy(), energyStorage.getCapacity()), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        OvenBlockEntity tileEntity = menu.getTileEntity();
        for (int i = 0; i < 9; i++) {
            Slot slot = menu.slots.get(i + 7);
            if (slot.hasItem()) {
                ItemStack itemStack = tileEntity.getSmeltingResult(slot.getItem());
                if (!itemStack.isEmpty()) {
                    // TODO At the moment, there seems to be no simple way of rendering an item transparently
                    // renderItemOverlay(minecraft, slot, itemStack, tileEntity.getCookProgress(i));
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.setColor(1f, 1f, 1f, 1f);

        // Draw background
        guiGraphics.blit(texture, leftPos + 22, topPos, 0, 0, imageWidth - 22, imageHeight);

        // Draw tool slots
        guiGraphics.blit(texture, leftPos, topPos + 10, 176, 30, 25, 87);

        OvenBlockEntity tileEntity = menu.getTileEntity();
        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;

        // Draw main slots
        guiGraphics.blit(texture, leftPos + 22 + 61 + offsetX, topPos + 18, 176, 117, 76, 76);

        // Draw fuel slot
        guiGraphics.blit(texture, leftPos + 22 + 38 + offsetX, topPos + 43, 205, 84, 18, 33);

        // Draw fuel bar
        if (tileEntity.isBurning()) {
            int burnTime = (int) (12 * tileEntity.getBurnTimeProgress());
            guiGraphics.blit(texture, leftPos + 22 + 40 + offsetX, topPos + 43 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }

        // Draw power bar
        if (tileEntity.hasPowerUpgrade()) {
            guiGraphics.blit(texture, leftPos + imageWidth - 25, topPos + 22, 205, 0, 18, 72);
            EnergyStorage energyStorage = tileEntity.getEnergyStorage();
            float energyPercentage = energyStorage.getEnergy() / (float) energyStorage.getCapacity();
            guiGraphics.blit(texture, leftPos + imageWidth - 25 + 1, topPos + 22 + 1 + 70 - (int) (energyPercentage * 70), 223, 0, 16, (int) (energyPercentage * 70));
        }
    }

}
