package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.cookingforblockheads.block.MilkJarBlock;
import net.blay09.mods.cookingforblockheads.block.entity.MilkJarBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MilkJarRenderer<TBlockEntity extends MilkJarBlockEntity> implements BlockEntityRenderer<TBlockEntity, MilkJarRenderer.MilkJarRenderState> {

    public static class MilkJarRenderState extends BlockEntityRenderState {
        public float fluidLevel;
        public Direction facing = Direction.NORTH;
    }

    private static final RandomSource random = RandomSource.create();

    public MilkJarRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MilkJarRenderState createRenderState() {
        return new MilkJarRenderState();
    }

    @Override
    public void extractRenderState(TBlockEntity blockEntity, MilkJarRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

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
            submitNodeCollector.submitBlockModel(poseStack, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS), getLiquidModel().asBlockStateModel(), 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    protected DeferredBlockStateModel getLiquidModel() {
        return ModModels.milkJarLiquid;
    }

}
