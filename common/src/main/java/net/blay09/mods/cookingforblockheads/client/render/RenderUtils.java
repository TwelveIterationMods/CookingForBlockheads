package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RenderUtils {

    public static void applyBlockAngle(PoseStack poseStack, BlockState state) {
        applyBlockAngle(poseStack, state, 180f);
    }

    public static void applyBlockAngle(PoseStack poseStack, BlockState state, float angleOffset) {
        float angle = state.getValue(BlockKitchen.FACING).toYRot();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(angleOffset - angle));
    }

    public static void renderItem(ItemStack itemStack, int combinedLight, PoseStack poseStack, MultiBufferSource buffer, Level level) {
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, level, 0);
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
    }*/

}
