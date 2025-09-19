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
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockState;
import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.CowVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class CowJarRenderer extends MilkJarRenderer<CowJarBlockEntity> {

    public static class CowJarRenderState extends MilkJarRenderState {
        public final LivingEntityRenderState cow = new LivingEntityRenderState();
        @Nullable
        public CowVariant variant;
    }

    private final Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel>> models;

    public CowJarRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
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

    private Optional<CowVariant> getDefaultCowVariant(@Nullable Level level) {
        if (level == null) {
            return Optional.empty();
        }

        return level.registryAccess().lookup(Registries.COW_VARIANT)
                .flatMap(it -> it.getOptional(CowVariants.DEFAULT));
    }


    private boolean shouldCreepyStare(@Nullable Level level) {
        return level != null && level.getMoonPhase() == 0 && level.isDarkOutside();
    }

    private void headbang(@Nullable Level level, CowJarRenderState renderState, int bpm) {
        long gameTime = level != null ? level.getGameTime() : 0;
        float headbangIntensity = 25f;
        float headbangSpeed = bpm * (float) Math.PI / 600f;
        renderState.cow.xRot = (float) Math.sin(gameTime * headbangSpeed) * headbangIntensity;
        renderState.cow.yRot = (float) Math.cos(gameTime * headbangSpeed * 0.7f) * 10f;
    }

    private void creepyStare(Level level, BlockPos pos, BlockState state, float delta, CowJarRenderState renderState) {
        final var nearestPlayer = level.getNearestPlayer(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 16f, false);
        if (nearestPlayer != null) {
            final var cowPos = new Vec3(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
            final var playerPos = nearestPlayer.getEyePosition(delta);
            final var direction = playerPos.subtract(cowPos);

            float blockYaw = state.getValue(BaseKitchenBlock.FACING).toYRot();
            double yaw = Math.atan2(-direction.x, direction.z);
            float yawDegrees = (float) Math.toDegrees(yaw);
            float relativeYaw = yawDegrees - blockYaw;
            while (relativeYaw > 180f) relativeYaw -= 360f;
            while (relativeYaw < -180f) relativeYaw += 360f;
            renderState.cow.yRot = Mth.clamp(relativeYaw, -90f, 90f);

            double horizontalDistance = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
            double pitch = Math.atan2(-direction.y, horizontalDistance);
            float pitchDegrees = (float) Math.toDegrees(pitch);
            renderState.cow.xRot = Mth.clamp(pitchDegrees, -60f, 60f);
        } else {
            renderState.cow.xRot = 0f;
            renderState.cow.yRot = 0f;
        }
    }

    private void theCowIsAlive(@Nullable Level level, BlockPos pos, BlockState state, float delta, CowJarRenderState renderState, CowJarBlockEntity blockEntity) {
        if (blockEntity.isPartying()) {
            headbang(level, renderState, 85);
            return;
        }
        
        if (shouldCreepyStare(level)) {
            creepyStare(level, pos, state, delta, renderState);
            return;
        }
        
        renderState.cow.xRot = 0f;
        renderState.cow.yRot = 0f;
    }

    @Override
    public void extractRenderState(CowJarBlockEntity blockEntity, MilkJarRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        if (renderState instanceof CowJarRenderState cowJarRenderState) {
            theCowIsAlive(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), delta, cowJarRenderState, blockEntity);

            cowJarRenderState.variant = blockEntity.getVariant() != null ? blockEntity.getVariant().value() : getDefaultCowVariant(blockEntity.getLevel()).orElse(null);
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
            RenderUtils.applyBlockAngle(poseStack, renderState.blockState, 180f);
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
