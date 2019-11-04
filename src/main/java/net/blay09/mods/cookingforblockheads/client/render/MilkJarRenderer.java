package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import org.lwjgl.opengl.GL11;

public class MilkJarRenderer extends TileEntityRenderer<TileMilkJar> {

    protected static BlockRendererDispatcher blockRenderer;

    @Override
    public void render(TileMilkJar tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
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
            GlStateManager.pushMatrix();

            GlStateManager.translated(x, y + (BlockKitchen.shouldBlockRenderLowered(tileEntity.getWorld(), tileEntity.getPos()) ? -0.05 : 0), z);
            GlStateManager.scalef(1f, tileEntity.getMilkAmount() / tileEntity.getMilkCapacity(), 1f);
            bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getInstance().getItemRenderer().renderModel(ModModels.milkJarLiquid, 0xFFFFFFFF);
            GlStateManager.popMatrix();

            RenderHelper.enableStandardItemLighting();
        }
    }

}
