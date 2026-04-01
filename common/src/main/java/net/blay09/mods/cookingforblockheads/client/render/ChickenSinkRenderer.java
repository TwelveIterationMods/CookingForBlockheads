package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.ChickenSinkBlock;
import net.blay09.mods.cookingforblockheads.block.entity.ChickenSinkBlockEntity;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ChickenSinkRenderer implements BlockEntityRenderer<ChickenSinkBlockEntity, ChickenSinkRenderer.ChickenSinkRenderState> {

    private static final Identifier DEFAULT_CHICKEN_TEXTURE = Identifier.withDefaultNamespace("entity/chicken/chicken_temperate");

    public static class ChickenSinkRenderState extends BlockEntityRenderState {
        public final ChickenRenderState chicken = new ChickenRenderState();
        public Direction facing = Direction.NORTH;
        @Nullable
        public ChickenVariant variant;
    }

    private final ChickenModel model;

    public ChickenSinkRenderer(BlockEntityRendererProvider.Context context) {
        model = new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN));
    }

    @Override
    public ChickenSinkRenderState createRenderState() {
        return new ChickenSinkRenderState();
    }

    @Override
    public void extractRenderState(ChickenSinkBlockEntity blockEntity, ChickenSinkRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.facing = blockEntity.getBlockState().getValue(ChickenSinkBlock.FACING);
        renderState.variant = blockEntity.getLevel() instanceof Level level
                ? level.registryAccess().lookup(Registries.CHICKEN_VARIANT)
                  .flatMap(it -> it.getOptional(ChickenVariants.DEFAULT))
                  .orElse(null)
                : null;

        long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
        float animationTime = (gameTime + delta) * 0.08f;
        renderState.chicken.xRot = -8f + Mth.sin(animationTime * 0.7f) * 4f;
        renderState.chicken.yRot = Mth.cos(animationTime * 0.55f) * 10f;
    }

    @Override
    public void submit(ChickenSinkRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
        poseStack.translate(-0.5f, 0f, -0.5f);

        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.translate(-0.5f, -0.5f, -0.5f);

        poseStack.translate(0.4f, -1f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-10f));

        final var scale = 0.9f;
        poseStack.scale(scale, scale, scale);
        final var renderType = renderState.variant != null ? RenderTypes.entityCutout(renderState.variant.modelAndTexture().asset().texturePath()) : RenderTypes.entityCutout(DEFAULT_CHICKEN_TEXTURE);
        submitNodeCollector.submitModel(model, renderState.chicken, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0, renderState.breakProgress);

        poseStack.popPose();
    }

}
