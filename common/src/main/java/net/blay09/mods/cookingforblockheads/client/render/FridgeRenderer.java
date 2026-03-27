package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.block.entity.FridgeBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FridgeRenderer implements BlockEntityRenderer<FridgeBlockEntity, FridgeRenderer.FridgeRenderState> {

    public static class FridgeRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState lower = new BlockModelRenderState();
        public final BlockModelRenderState upper = new BlockModelRenderState();
        public boolean skip;
        public List<ItemStackRenderState> items = Collections.emptyList();
        public FridgeBlock.FridgeModelType modelType = FridgeBlock.FridgeModelType.SMALL;
        public DyeColor dye = DyeColor.WHITE;
        public float doorAngle;
        public boolean flipped;
        public Direction facing = Direction.NORTH;
    }

    private final ItemModelResolver itemModelResolver;

    public FridgeRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public FridgeRenderState createRenderState() {
        return new FridgeRenderState();
    }

    @Override
    public void extractRenderState(FridgeBlockEntity blockEntity, FridgeRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        DeferredBlockStateModel lowerModel;
        DeferredBlockStateModel upperModel = null;
        boolean isLarge = renderState.modelType == FridgeBlock.FridgeModelType.LARGE_LOWER;
        if (isLarge) {
            lowerModel = renderState.flipped ? ModModels.fridgeDoorsLargeLowerFlipped.get(renderState.dye) : ModModels.fridgeDoorsLargeLower.get(renderState.dye);
            upperModel = renderState.flipped ? ModModels.fridgeDoorsLargeUpperFlipped.get(renderState.dye) : ModModels.fridgeDoorsLargeUpper.get(renderState.dye);
        } else {
            lowerModel = renderState.flipped ? ModModels.fridgeDoorsFlipped.get(renderState.dye) : ModModels.fridgeDoors.get(renderState.dye);
        }

        final var lowerParts = renderState.lower.setupModel(new Matrix4f(), false);
        lowerModel.asBlockStateModel().collectParts(renderState.lower.scratchRandomSource(42), lowerParts);

        if (upperModel != null) {
            final var upperParts = renderState.upper.setupModel(new Matrix4f(), false);
            upperModel.asBlockStateModel().collectParts(renderState.upper.scratchRandomSource(42), upperParts);
        } else {
            renderState.upper.clear();
        }

        renderState.dye = blockEntity.getBlockState().getBlock() instanceof FridgeBlock fridgeBlock ? fridgeBlock.getColor() : DyeColor.WHITE;
        renderState.doorAngle = blockEntity.getDoorAnimator().getRenderAngle(delta);
        renderState.modelType = blockEntity.getBlockState().getValue(FridgeBlock.MODEL_TYPE);
        renderState.skip = renderState.modelType == FridgeBlock.FridgeModelType.LARGE_UPPER;
        renderState.flipped = blockEntity.getBlockState().getValue(FridgeBlock.FLIPPED);
        renderState.facing = blockEntity.getBlockState().getValue(FridgeBlock.FACING);

        final var id = (int) blockEntity.getBlockPos().asLong();
        renderState.items = new ArrayList<>();
        for (int i = 0; i < blockEntity.getContainer().getContainerSize(); i++) {
            final var itemStack = blockEntity.getContainer().getItem(i);
            final var itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, id + i);
            renderState.items.add(itemStackRenderState);
        }
    }

    @Override
    public void submit(FridgeRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.skip) {
            return;
        }

        // Render the fridge door
        poseStack.pushPose();

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
        poseStack.translate(-0.5f, 0f, -0.5f);

        float originX = 0.9375f - 0.5f / 16f;
        float originZ = 0.0625f + 0.5f / 16f;
        if (renderState.flipped) {
            originX = 1f - originX;
        }

        poseStack.pushPose();
        poseStack.translate(originX, 0f, originZ);
        poseStack.mulPose(Axis.YN.rotationDegrees((float) Math.toDegrees(renderState.flipped ? -renderState.doorAngle : renderState.doorAngle)));
        poseStack.translate(-originX, 0f, -originZ);

        renderState.lower.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        if (!renderState.upper.isEmpty()) {
            poseStack.translate(0, 1, 0);
            renderState.upper.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }

        poseStack.popPose();

        // Render the fridge content if the door is open
        if (renderState.doorAngle > 0f) {
            poseStack.pushPose();

            poseStack.translate(0.5f, 0.5f, 0.5f);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            float topY = renderState.modelType == FridgeBlock.FridgeModelType.LARGE_LOWER ? 3.25f : 0.45f;
            for (int i = renderState.items.size() - 1; i >= 0; i--) {
                final var itemStackRenderState = renderState.items.get(i);
                if (!itemStackRenderState.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (renderState.modelType == FridgeBlock.FridgeModelType.LARGE_LOWER) {
                        int rowIndex = i % 18;
                        offsetX = 0.7f - (rowIndex % 9) * 0.175f;
                        offsetY = topY - (int) (i / 18f) * 1.25f;
                        offsetZ = 0.5f - (int) (rowIndex / 9f) * 0.9f;
                    } else {
                        int rowIndex = i % 13;
                        offsetX = 0.7f;
                        float spacing = 0.175f;
                        if (rowIndex / 9 > 0) {
                            offsetX -= 0.2f;
                            spacing *= 2;
                        }
                        offsetX -= (rowIndex % 9) * spacing;
                        offsetY = topY - (int) (i / 14f) * 1.25f;
                        offsetZ = 0.5f - (int) (rowIndex / 9f) * 0.9f;
                    }
                    poseStack.pushPose();
                    poseStack.translate(offsetX, offsetY, offsetZ);
                    poseStack.mulPose(Axis.YP.rotationDegrees(45f));
                    itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
        }

        poseStack.popPose();
    }

}
