package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.opengl.GL11;

public class SinkRenderer extends TileEntityRenderer<TileSink> {

    private ItemStack fish;

    @Override
    public void render(TileSink tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        if (fish == null) {
            fish = new ItemStack(Items.COD);
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (tileEntity.getWaterAmount() > 0) {
            GlStateManager.enableBlend();
            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y + 0.5f, z);
            float filledPercentage = tileEntity.getWaterAmount() / (float) tileEntity.getWaterCapacity();
            GlStateManager.scalef(1f, filledPercentage, 1f);
            GlStateManager.translatef(0f, -0.51f, 0f);
            bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getInstance().getItemRenderer().renderModel(ModModels.sinkLiquid, Fluids.WATER.getAttributes().getColor());
            GlStateManager.popMatrix();
        }

        RenderHelper.enableStandardItemLighting();
    }

}
