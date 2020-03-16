package net.blay09.mods.cookingforblockheads.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.OvenContainer;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OvenScreen extends ContainerScreen<OvenContainer> {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/oven.png");

    public OvenScreen(OvenContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.xSize += 22;
        this.ySize = 193;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

        OvenTileEntity tileEntity = container.getTileEntity();
        if (tileEntity.hasPowerUpgrade() && mouseX >= guiLeft + xSize - 25 && mouseY >= guiTop + 22 && mouseX < guiLeft + xSize - 25 + 35 + 18 && mouseY < guiTop + 22 + 72) {
            renderTooltip(I18n.format("tooltip.cookingforblockheads:energy_stored", tileEntity.getEnergyStored(), tileEntity.getEnergyCapacity()), mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        Minecraft minecraft = getMinecraft();
        String ovenTitle = getTitle().getFormattedText();
        minecraft.fontRenderer.drawString(ovenTitle, (this.xSize + 22) / 2f - minecraft.fontRenderer.getStringWidth(ovenTitle) / 2f, 6, 4210752);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8 + 22, this.ySize - 96 + 2, 4210752);

        OvenTileEntity tileEntity = container.getTileEntity();
        for (int i = 0; i < 9; i++) {
            Slot slot = container.inventorySlots.get(i + 7);
            if (slot.getHasStack()) {
                ItemStack itemStack = tileEntity.getSmeltingResult(slot.getStack());
                if (!itemStack.isEmpty()) {
                    // TODO At the moment, there seems to be no simple way of rendering an item transparently
                    // renderItemOverlay(minecraft, slot, itemStack, tileEntity.getCookProgress(i));
                }
            }
        }
    }

    private void renderItemOverlay(Minecraft minecraft, Slot slot, ItemStack itemStack, float alpha) {
        RenderSystem.pushMatrix();
        minecraft.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        minecraft.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1f, 0f, 0f, alpha);
        RenderSystem.translatef((float) slot.xPos, (float) slot.yPos, 250f + getBlitOffset());
        RenderSystem.translatef(8f, 8f, 0f);
        RenderSystem.scalef(1f, -1f, 1f);
        RenderSystem.scalef(16f, 16f, 16f);
        MatrixStack matrixStack = new MatrixStack();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.GUI, 15728880, OverlayTexture.DEFAULT_LIGHT, matrixStack, buffer);
        buffer.finish();
        RenderSystem.enableDepthTest();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        getMinecraft().getTextureManager().bindTexture(texture);

        // Draw background
        blit(guiLeft + 22, guiTop, 0, 0, xSize - 22, ySize);

        // Draw tool slots
        blit(guiLeft, guiTop + 10, 176, 30, 25, 87);

        OvenTileEntity tileEntity = container.getTileEntity();
        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;

        // Draw main slots
        blit(guiLeft + 22 + 61 + offsetX, guiTop + 18, 176, 117, 76, 76);

        // Draw fuel slot
        blit(guiLeft + 22 + 38 + offsetX, guiTop + 43, 205, 84, 18, 33);

        // Draw fuel bar
        if (tileEntity.isBurning()) {
            int burnTime = (int) (12 * tileEntity.getBurnTimeProgress());
            blit(guiLeft + 22 + 40 + offsetX, guiTop + 43 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }

        // Draw power bar
        if (tileEntity.hasPowerUpgrade()) {
            blit(guiLeft + xSize - 25, guiTop + 22, 205, 0, 18, 72);
            float energyPercentage = tileEntity.getEnergyStored() / (float) tileEntity.getEnergyCapacity();
            blit(guiLeft + xSize - 25 + 1, guiTop + 22 + 1 + 70 - (int) (energyPercentage * 70), 223, 0, 16, (int) (energyPercentage * 70));
        }
    }

    /*private void renderItemWithTint(ItemStack itemStack, int x, int y, int color) {
        Minecraft minecraft = getMinecraft();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        IBakedModel model = itemRenderer.getItemModelWithOverrides(itemStack, null, null);
        RenderSystem.pushMatrix();
        minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        minecraft.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1f);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1f, 1f, 1f, 1f);

        RenderSystem.translatef(x, y, 300f + getBlitOffset());
        RenderSystem.scalef(1f, -1f, 1f);
        RenderSystem.scalef(16f, 16f, 16f);
        if (model.isGui3d()) {
            RenderSystem.enableLighting();
        } else {
            RenderSystem.disableLighting();
        }

        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        for (Direction facing : Direction.values()) {
            renderQuads(vertexBuffer, model.getQuads(null, facing, random, EmptyModelData.INSTANCE), color, itemStack);
        }

        renderQuads(vertexBuffer, model.getQuads(null, null, random, EmptyModelData.INSTANCE), color, itemStack);
        tessellator.draw();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();
        RenderSystem.popMatrix();
        minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        minecraft.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        boolean useItemTint = color == -1 && !stack.isEmpty();
        int i = 0;
        for (int j = quads.size(); i < j; ++i) {
            BakedQuad quad = quads.get(i);
            int k = color;
            if (useItemTint && quad.hasTintIndex()) {
                k = getMinecraft().getItemColors().getColor(stack, quad.getTintIndex());
                k = k | -16777216;
            }
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, quad, k);
        }
    }*/

}
