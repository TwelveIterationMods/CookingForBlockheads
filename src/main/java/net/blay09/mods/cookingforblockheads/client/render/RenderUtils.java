package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderUtils {

    private static final Random random = new Random();

    @Deprecated
    public static float getFacingAngle(BlockState state) {
        return getFacingAngle(state.get(BlockKitchen.FACING));
    }

    public static float getFacingAngle(Direction facing) {
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

    public static void renderBlockAt(Minecraft mc, BlockState state, IEnviromentBlockReader world, BlockPos pos, BufferBuilder renderer, @Nullable IBakedModel modelOverride) {
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

    public static void renderBlockAt(Minecraft mc, BlockState state, IEnviromentBlockReader world, BlockPos pos, BufferBuilder renderer) {
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
    }

}
