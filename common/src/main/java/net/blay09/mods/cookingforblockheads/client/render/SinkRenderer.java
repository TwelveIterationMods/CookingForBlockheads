
package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.block.entity.SinkBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SinkRenderer implements BlockEntityRenderer<SinkBlockEntity, SinkRenderer.SinkRenderState> {

    public static class SinkRenderState extends BlockEntityRenderState {
        public float fluidLevel;
    }

    public SinkRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public SinkRenderState createRenderState() {
        return new SinkRenderState();
    }

    @Override
    public void extractRenderState(SinkBlockEntity blockEntity, SinkRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.fluidLevel = blockEntity.getFluidTank().getAmount() / (float) blockEntity.getFluidTank().getCapacity();
    }

    @Override
    public void submit(SinkRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.fluidLevel > 0f) {
            poseStack.pushPose();
            float filledPercentage = renderState.fluidLevel;
            poseStack.translate(0f, 0.5f - 0.5f * filledPercentage, 0f);
            poseStack.scale(1f, filledPercentage, 1f);
            final var model = ModModels.sinkLiquid.get();
            submitNodeCollector.submitBlockModel(poseStack, RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS), model, 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

}
