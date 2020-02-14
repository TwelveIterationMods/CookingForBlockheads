package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.tile.SinkTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class SinkRenderer extends TileEntityRenderer<SinkTileEntity> {

    public SinkRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(SinkTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        RenderHelper.disableStandardItemLighting();
        // TODO GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // TODO GlStateManager.enableBlend();
        // TODO GlStateManager.disableCull();

        if (tileEntity.getWaterAmount() > 0) {
            // TODO GlStateManager.enableBlend();
            matrixStack.push();
            matrixStack.translate(0, 0.5f, 0);
            float filledPercentage = tileEntity.getWaterAmount() / (float) tileEntity.getWaterCapacity();
            matrixStack.scale(1f, filledPercentage, 1f);
            matrixStack.translate(0f, -0.51f, 0f);
            // TODO bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            // TODO Minecraft.getInstance().getItemRenderer().renderModel(ModModels.sinkLiquid, Fluids.WATER.getAttributes().getColor());
            matrixStack.pop();
        }

        RenderHelper.enableStandardItemLighting();
    }

}
