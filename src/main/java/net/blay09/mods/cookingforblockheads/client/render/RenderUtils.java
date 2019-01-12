package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class RenderUtils {

    @Deprecated
    public static float getFacingAngle(IBlockState state) {
        return getFacingAngle(state.getValue(BlockKitchen.FACING));
    }

    public static float getFacingAngle(EnumFacing facing) {
        float angle;
        switch (facing) {
            case NORTH:
                angle = 0;
                break;
            case SOUTH:
                angle = 180;
                break;
            case WEST:
                angle = 90;
                break;
            case EAST:
            default:
                angle = -90;
                break;
        }
        return angle;
    }

    public static void renderBlockAt(Minecraft mc, IBlockState state, IBlockAccess world, BlockPos pos, BufferBuilder renderer, @Nullable IBakedModel modelOverride) {
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        try {
            EnumBlockRenderType renderType = state.getRenderType();
            if (renderType == EnumBlockRenderType.MODEL) {
                if (modelOverride == null) {
                    modelOverride = dispatcher.getModelForState(state);
                }
                dispatcher.getBlockModelRenderer().renderModel(world, modelOverride, state, pos, renderer, false);
            }
        } catch (Throwable ignored) {
        }
    }

    public static void renderBlockAt(Minecraft mc, IBlockState state, IBlockAccess world, BlockPos pos, BufferBuilder renderer) {
        renderBlockAt(mc, state, world, pos, renderer, null);
    }

    public static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
        }
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

}
