package net.blay09.mods.cookingforblockheads.client.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.blay09.mods.cookingforblockheads.block.ChickenSinkBlock;
import net.blay09.mods.cookingforblockheads.block.entity.ChickenSinkBlockEntity;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.BabyChickenModel;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class ChickenSinkRenderer implements BlockEntityRenderer<ChickenSinkBlockEntity, ChickenSinkRenderer.ChickenSinkRenderState> {

    public static class ChickenSinkRenderState extends BlockEntityRenderState {
        public final ChickenRenderState chicken = new ChickenRenderState();
        public final ItemStackRenderState egg = new ItemStackRenderState();
        public Direction facing = Direction.NORTH;
        public float wiggle;
        @Nullable
        public ChickenVariant variant;
    }

    private static final int WIGGLE_INTERVAL_TICKS = 160;
    private static final int WIGGLE_DURATION_TICKS = 18;

    private final Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> models;
    private final ItemModelResolver itemModelResolver;

    public ChickenSinkRenderer(BlockEntityRendererProvider.Context context) {
        models = bakeModels(context);
        itemModelResolver = context.itemModelResolver();
    }

    private static Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> bakeModels(BlockEntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(
                ChickenVariant.ModelType.NORMAL, new AdultAndBabyModelPair<>(
                        new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN)),
                        new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY))),
                ChickenVariant.ModelType.COLD, new AdultAndBabyModelPair<>(
                        new ColdChickenModel(context.bakeLayer(ModelLayers.COLD_CHICKEN)),
                        new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY)))));
    }

    @Override
    public ChickenSinkRenderState createRenderState() {
        return new ChickenSinkRenderState();
    }

    private boolean shouldCreepyStare(@Nullable Level level, BlockPos pos) {
        if (level == null) {
            return false;
        }

        final var moonPhase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos);
        final var moonAngle = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_ANGLE, pos) % 360;
        boolean moonVisible = (moonAngle >= 270 || moonAngle <= 87.5);
        return moonPhase == MoonPhase.FULL_MOON && moonVisible;
    }

    private void headbang(@Nullable Level level, ChickenSinkRenderState renderState, int bpm) {
        long gameTime = level != null ? level.getGameTime() : 0;
        float headbangIntensity = 25f;
        float headbangSpeed = bpm * (float) Math.PI / 600f;
        renderState.chicken.xRot = (float) Math.sin(gameTime * headbangSpeed) * headbangIntensity;
        renderState.chicken.yRot = (float) Math.cos(gameTime * headbangSpeed * 0.7f) * 10f;
    }

    private void creepyStare(Level level, BlockPos pos, BlockState state, float delta, ChickenSinkRenderState renderState) {
        final var nearestPlayer = level.getNearestPlayer(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 16f, false);
        if (nearestPlayer != null) {
            final var chickenPos = new Vec3(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
            final var playerPos = nearestPlayer.getEyePosition(delta);
            final var direction = playerPos.subtract(chickenPos);

            float blockYaw = state.getValue(BaseKitchenBlock.FACING).toYRot();
            double yaw = Math.atan2(-direction.x, direction.z);
            float yawDegrees = (float) Math.toDegrees(yaw);
            float relativeYaw = yawDegrees - blockYaw;
            while (relativeYaw > 180f) relativeYaw -= 360f;
            while (relativeYaw < -180f) relativeYaw += 360f;
            renderState.chicken.yRot = Mth.clamp(relativeYaw, -90f, 90f);

            double horizontalDistance = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
            double pitch = Math.atan2(-direction.y, horizontalDistance);
            float pitchDegrees = (float) Math.toDegrees(pitch);
            renderState.chicken.xRot = Mth.clamp(pitchDegrees, -60f, 60f);
        } else {
            applyIdleAnimation(level, delta, renderState);
        }
    }

    private void applyIdleAnimation(@Nullable Level level, float delta, ChickenSinkRenderState renderState) {
        long gameTime = level != null ? level.getGameTime() : 0L;
        float animationTime = (gameTime + delta) * 0.08f;
        renderState.chicken.xRot = -8f + Mth.sin(animationTime * 0.7f) * 4f;
        renderState.chicken.yRot = Mth.cos(animationTime * 0.55f) * 10f;
    }

    private void updateChickenPose(ChickenSinkBlockEntity blockEntity, float delta, ChickenSinkRenderState renderState) {
        final var level = blockEntity.getLevel();
        final var pos = blockEntity.getBlockPos();
        final var state = blockEntity.getBlockState();

        if (blockEntity.isPartying()) {
            headbang(level, renderState, 85);
            return;
        }

        if (shouldCreepyStare(level, pos)) {
            creepyStare(level, pos, state, delta, renderState);
            return;
        }

        applyIdleAnimation(level, delta, renderState);
    }

    @Override
    public void extractRenderState(ChickenSinkBlockEntity blockEntity, ChickenSinkRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.facing = blockEntity.getBlockState().getValue(ChickenSinkBlock.FACING);
        renderState.variant = blockEntity.getChickenType() != null ? blockEntity.getChickenType().value() : null;
        renderState.chicken.isBaby = blockEntity.getChickenAge() < 0;
        itemModelResolver.updateForTopItem(renderState.egg, blockEntity.getIncubatingEgg(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        if (!renderState.egg.isEmpty()) {
            renderState.wiggle = getWiggle(blockEntity.getLevel(), blockEntity.getBlockPos(), delta, 0.1f);
        } else if (renderState.chicken.isBaby) {
            renderState.wiggle = getWiggle(blockEntity.getLevel(), blockEntity.getBlockPos(), delta, 0.5f);
        } else {
            renderState.wiggle = 0f;
        }
        updateChickenPose(blockEntity, delta, renderState);
    }

    private float getWiggle(@Nullable Level level, BlockPos pos, float delta, float scale) {
        long gameTime = level != null ? level.getGameTime() : 0L;
        float wiggleTime = Math.floorMod(gameTime + pos.asLong(), WIGGLE_INTERVAL_TICKS) + delta;
        if (wiggleTime >= WIGGLE_DURATION_TICKS) {
            return 0f;
        }

        float progress = wiggleTime / WIGGLE_DURATION_TICKS;
        float envelope = Mth.sin(progress * Mth.PI);
        return Mth.sin(wiggleTime * 1.4f) * envelope * scale;
    }

    @Override
    public void submit(ChickenSinkRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.variant == null) {
            if (!renderState.egg.isEmpty()) {
                poseStack.pushPose();

                poseStack.translate(0.5f, 0f, 0.5f);
                poseStack.rotateDegrees(Axis.YP, -renderState.facing.toYRot() + 190f);
                if (renderState.wiggle != 0f) {
                    poseStack.rotateDegrees(Axis.ZP, renderState.wiggle);
                }
                poseStack.translate(-0.5f, 0f, -0.5f);

                poseStack.translate(0.68f, 0.95f, 0.5f);
                poseStack.rotateDegrees(Axis.XP, 45f);
                poseStack.scale(0.35f, 0.35f, 0.35f);
                renderState.egg.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
            return;
        }

        poseStack.pushPose();

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.rotateDegrees(Axis.YP, -renderState.facing.toYRot() + 180f);
        if (renderState.wiggle != 0f) {
            poseStack.rotateDegrees(Axis.ZP, renderState.wiggle);
        }
        poseStack.translate(-0.5f, 0f, -0.5f);

        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.rotateDegrees(Axis.ZP, 180);
        poseStack.translate(-0.5f, -0.5f, -0.5f);

        poseStack.translate(renderState.chicken.isBaby ? 0.35f : 0.4f, renderState.chicken.isBaby ? -1.1f : -1f, 0.5f);
        poseStack.rotateDegrees(Axis.YP, -10f);

        final var scale = 0.9f;
        poseStack.scale(scale, scale, scale);
        final var model = models.get(renderState.variant.modelAndTexture().model()).getModel(renderState.chicken.isBaby);
        final var texture = renderState.chicken.isBaby
                ? renderState.variant.babyTexture().texturePath()
                : renderState.variant.modelAndTexture().asset().texturePath();
        final var renderType = RenderTypes.entityCutout(texture);
        submitNodeCollector.submitModel(model, renderState.chicken, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }

}
