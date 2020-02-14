package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;

public class RenderUtils {

    public static void applyBlockAngle(MatrixStack matrixStack, BlockState state) {
        float angle = state.get(BlockKitchen.FACING).getHorizontalAngle();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.rotate(new Quaternion(0f, -angle, 0f, true));
    }

    public static void renderItem(ItemStack leftStack, int combinedLight, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        Minecraft.getInstance().getItemRenderer().renderItem(leftStack, ItemCameraTransforms.TransformType.FIXED, combinedLight, OverlayTexture.DEFAULT_LIGHT, matrixStack, buffer);
    }

    /*private static final Random random = new Random();

    public static void renderBlockAt(Minecraft mc, BlockState state, ILightReader world, BlockPos pos, BufferBuilder renderer, @Nullable IBakedModel modelOverride) {
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        try {
            BlockRenderType renderType = state.getRenderType();
            if (renderType == BlockRenderType.MODEL) {
                if (modelOverride == null) {
                    modelOverride = dispatcher.getModelForState(state);
                }
                dispatcher.getBlockModelRenderer().renderModel(world, modelOverride, state, pos, renderer, false, random, 0, EmptyModelData.INSTANCE);
            }
        } catch (Throwable ignored) {
        }
    }

    public static void renderBlockAt(Minecraft mc, BlockState state, ILightReader world, BlockPos pos, BufferBuilder renderer) {
        renderBlockAt(mc, state, world, pos, renderer, null);
    }

    public static void renderItem(ItemRenderer itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, z);
        GlStateManager.rotatef(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {
            GlStateManager.rotatef(180f, 0f, 1f, 0f);
        }
        GlStateManager.pushLightingAttributes();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttributes();
        GlStateManager.popMatrix();
    }*/

}
