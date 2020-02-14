package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.tile.MilkJarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.lwjgl.opengl.GL11;

public class MilkJarRenderer extends TileEntityRenderer<MilkJarTileEntity> {

    protected static BlockRendererDispatcher blockRenderer;

    public MilkJarRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MilkJarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        if (blockRenderer == null) {
            blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        }

        if (tileEntity.getMilkAmount() > 0) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            matrixStack.push();

            matrixStack.translate(0, (BlockKitchen.shouldBlockRenderLowered(tileEntity.getWorld(), tileEntity.getPos()) ? -0.05 : 0), 0);
            matrixStack.scale(1f, tileEntity.getMilkAmount() / tileEntity.getMilkCapacity(), 1f);
            // TODO bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            // TODO Minecraft.getInstance().getItemRenderer().renderModel(ModModels.milkJarLiquid, 0xFFFFFFFF);
            matrixStack.pop();

            RenderHelper.enableStandardItemLighting();
        }
    }

}
