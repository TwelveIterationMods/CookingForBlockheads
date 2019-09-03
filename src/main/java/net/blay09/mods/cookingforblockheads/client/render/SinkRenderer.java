package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.sun.prism.TextureMap;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.BlockSink;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import org.lwjgl.opengl.GL11;

public class SinkRenderer extends TileEntityRenderer<TileSink> {

    public static IBakedModel modelSinkLiquid;
    private ItemStack fish;

    @Override
    public void render(TileSink tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
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

        if (!CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
            BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            if (state.getBlock() == ModBlocks.sink) {
                Direction facing = state.get(BlockSink.FACING);

                GlStateManager.pushMatrix();
                GlStateManager.translated(x + 0.5f, y + 0.78f, z + 0.5f);
                GlStateManager.rotatef(RenderUtils.getFacingAngle(facing) - 90, 0f, 1f, 0f);
                GlStateManager.translatef(-0.05f, 0f, -0.175f);
                GlStateManager.scalef(0.5f, 0.5f, 0.5f);
                GlStateManager.rotatef(135, 1f, 0f, 0f);
                Minecraft.getInstance().getItemRenderer().renderItem(fish, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }

        if (tileEntity.getWaterAmount() > 0) {
            GlStateManager.enableBlend();
            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y + 0.5f, z);
            float filledPercentage = tileEntity.getWaterAmount() / (float) tileEntity.getWaterCapacity();
            GlStateManager.scalef(1f, filledPercentage, 1f);
            GlStateManager.translatef(0f, -0.5f, 0f);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getInstance().getItemRenderer().renderModel(modelSinkLiquid, 0xFFFFFFFF);
            GlStateManager.popMatrix();
        }

        RenderHelper.enableStandardItemLighting();
    }

}
