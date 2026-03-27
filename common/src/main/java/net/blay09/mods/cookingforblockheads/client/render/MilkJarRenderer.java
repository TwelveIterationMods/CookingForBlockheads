package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.cookingforblockheads.block.MilkJarBlock;
import net.blay09.mods.cookingforblockheads.block.entity.MilkJarBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4f;

public class MilkJarRenderer<TBlockEntity extends MilkJarBlockEntity> implements BlockEntityRenderer<TBlockEntity, MilkJarRenderer.MilkJarRenderState> {

    public static class MilkJarRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState milk = new BlockModelRenderState();
        public float fluidLevel;
        public Direction facing = Direction.NORTH;
    }

    public MilkJarRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MilkJarRenderState createRenderState() {
        return new MilkJarRenderState();
    }

    @Override
    public void extractRenderState(TBlockEntity blockEntity, MilkJarRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        final var milkParts = renderState.milk.setupModel(new Matrix4f(), false);
        final var milkModel = getLiquidModel().asBlockStateModel();
        milkModel.collectParts(renderState.milk.scratchRandomSource(42), milkParts);

        renderState.facing = blockEntity.getBlockState().getValue(MilkJarBlock.FACING);
        renderState.fluidLevel = blockEntity.getFluidTank().getAmount() / (float) blockEntity.getFluidTank().getCapacity();
    }

    @Override
    public void submit(MilkJarRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.fluidLevel > 0) {
            poseStack.pushPose();

            poseStack.translate(0.5f, 0f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
            poseStack.translate(-0.5f, 0f, -0.5f);

            poseStack.scale(1f, renderState.fluidLevel, 1f);
            renderState.milk.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    protected DeferredBlockStateModel getLiquidModel() {
        return ModModels.milkJarLiquid;
    }

}
