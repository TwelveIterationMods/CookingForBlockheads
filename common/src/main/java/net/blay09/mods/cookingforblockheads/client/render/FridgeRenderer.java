package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.CounterBlock;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.block.entity.FridgeBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FridgeRenderer implements BlockEntityRenderer<FridgeBlockEntity, FridgeRenderer.FridgeRenderState> {

    public static class FridgeRenderState extends BlockEntityRenderState {
        public boolean skip;
        public List<ItemStackRenderState> items = Collections.emptyList();
        public FridgeBlock.FridgeModelType modelType;
        public DyeColor dye;
        public float doorAngle;
        public boolean flipped;
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
    public void extractRenderState(FridgeBlockEntity blockEntity, FridgeRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.dye = renderState.blockState.getBlock() instanceof CounterBlock counterBlock ? counterBlock.getColor() : null;
        renderState.doorAngle = blockEntity.getDoorAnimator().getRenderAngle(delta);
        renderState.modelType = blockEntity.getBlockState().getValue(FridgeBlock.MODEL_TYPE);
        renderState.flipped = blockEntity.getBlockState().getValue(FridgeBlock.FLIPPED);

        final var id = (int) blockEntity.getBlockPos().asLong();
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
        boolean isLarge = renderState.modelType == FridgeBlock.FridgeModelType.LARGE_LOWER;
        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, renderState.blockState);
        poseStack.translate(-0.5f, 0f, -0.5f);

        float originX = 0.9375f - 0.5f / 16f;
        float originZ = 0.0625f + 0.5f / 16f;
        if (renderState.flipped) {
            originX = 1f - originX;
        }

        poseStack.translate(originX, 0f, originZ);
        poseStack.mulPose(Axis.YN.rotationDegrees((float) Math.toDegrees(renderState.flipped ? -renderState.doorAngle : renderState.doorAngle)));
        poseStack.translate(-originX, 0f, -originZ);

        int colorIndex = renderState.dye.getId();
        BlockStateModel lowerModel;
        BlockStateModel upperModel = null;
        if (isLarge) {
            lowerModel = renderState.flipped ? ModModels.fridgeDoorsLargeLowerFlipped.get(colorIndex).get() : ModModels.fridgeDoorsLargeLower.get(colorIndex).get();
            upperModel = renderState.flipped ? ModModels.fridgeDoorsLargeUpperFlipped.get(colorIndex).get() : ModModels.fridgeDoorsLargeUpper.get(colorIndex).get();
        } else {
            lowerModel = renderState.flipped ? ModModels.fridgeDoorsFlipped.get(colorIndex).get() : ModModels.fridgeDoors.get(colorIndex).get();
        }

        submitNodeCollector.submitBlockModel(poseStack, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS), lowerModel, 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        if (upperModel != null) {
            poseStack.translate(0, 1, 0);
            submitNodeCollector.submitBlockModel(poseStack, RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS), upperModel, 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }

        poseStack.popPose();

        // Render the fridge content if the door is open
        if (renderState.doorAngle > 0f) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            RenderUtils.applyBlockAngle(poseStack, renderState.blockState);
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
    }

}
