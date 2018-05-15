package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.ModConfig;
import net.blay09.mods.cookingforblockheads.block.BlockSink;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class SinkRenderer extends TileEntitySpecialRenderer<TileSink> {

    public static IBakedModel modelSinkLiquid;

    @Override
    public void render(TileSink tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (!ModConfig.general.sinkRequiresWater) {
            IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            if (state.getBlock() == ModBlocks.sink) {
                EnumFacing facing = state.getValue(BlockSink.FACING);

                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5f, y + 0.78f, z + 0.5f);
                GlStateManager.rotate(RenderUtils.getFacingAngle(facing) - 90, 0f, 1f, 0f);
                GlStateManager.translate(-0.05f, 0f, -0.175f);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                GlStateManager.rotate(135, 1f, 0f, 0f);
                ItemStack fish = new ItemStack(Items.FISH);
                Minecraft.getMinecraft().getRenderItem().renderItem(fish, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.enableBlend();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.5f, z);
        float filledPercentage = tileEntity.getWaterAmount() / (float) tileEntity.getWaterCapacity();
        GlStateManager.scale(1f, filledPercentage, 1f);
        GlStateManager.translate(0f, -0.5f, 0f);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderModel(modelSinkLiquid, 0xFFFFFFFF);
        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

}
