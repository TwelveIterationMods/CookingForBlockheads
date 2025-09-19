package net.blay09.mods.cookingforblockheads.client.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.CowVariants;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CowJarRenderer extends MilkJarRenderer<CowJarBlockEntity> {

    public static class CowJarRenderState extends MilkJarRenderState {
        public final LivingEntityRenderState cow = new LivingEntityRenderState();
        @Nullable
        public CowVariant variant;
    }

    private final Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel>> models;
    private final MaterialSet materials;

    public CowJarRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        materials = context.materials();
        models = bakeModels(context);
    }

    private static Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel>> bakeModels(BlockEntityRendererProvider.Context context) {
        return Maps.newEnumMap(
                Map.of(CowVariant.ModelType.NORMAL, new AdultAndBabyModelPair<>(new CowModel(context.bakeLayer(ModelLayers.COW)), new CowModel(context.bakeLayer(ModelLayers.COW_BABY))),
                        CowVariant.ModelType.WARM, new AdultAndBabyModelPair<>(new CowModel(context.bakeLayer(ModelLayers.WARM_COW)), new CowModel(context.bakeLayer(ModelLayers.WARM_COW_BABY))),
                        CowVariant.ModelType.COLD, new AdultAndBabyModelPair<>(new CowModel(context.bakeLayer(ModelLayers.COLD_COW)), new CowModel(context.bakeLayer(ModelLayers.COLD_COW_BABY)))));
    }

    @Override
    public MilkJarRenderState createRenderState() {
        return new CowJarRenderState();
    }

    @Override
    public void extractRenderState(CowJarBlockEntity blockEntity, MilkJarRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        if (renderState instanceof CowJarRenderState cowJarRenderState) {
            cowJarRenderState.variant = blockEntity.getLevel().registryAccess().lookup(Registries.COW_VARIANT)
                    .flatMap(it -> it.getOptional(CowVariants.DEFAULT))
                    .orElse(null);
        }
    }

    @Override
    public void submit(MilkJarRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);

        if (renderState instanceof CowJarRenderState cowJarRenderState && cowJarRenderState.variant != null) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180));
            poseStack.translate(-0.5f, -0.5f, -0.5f);
            RenderUtils.applyBlockAngle(poseStack, renderState.blockState, 0f);
            poseStack.translate(0, 0.675f, 0);
            float scale = 0.2f;
            poseStack.scale(scale, scale, scale);
            final var modelAndTexture = cowJarRenderState.variant.modelAndTexture();
            final var model = models.get(modelAndTexture.model()).getModel(cowJarRenderState.cow.isBaby);
            final var textureAsset = modelAndTexture.asset();
            submitNodeCollector.submitModel(model, cowJarRenderState.cow, poseStack, RenderType.entityCutout(textureAsset.texturePath()), renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0, renderState.breakProgress);
            poseStack.popPose();
        }
    }

    @Override
    protected BlockStateModel getLiquidModel() {
        return ModModels.cowJarLiquid.get();
    }
}
