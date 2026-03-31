package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.cookingforblockheads.block.CabinetBlock;
import net.blay09.mods.cookingforblockheads.block.CounterBlock;
import net.blay09.mods.cookingforblockheads.block.entity.CabinetBlockEntity;
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

public class CabinetRenderer implements BlockEntityRenderer<CabinetBlockEntity, CabinetRenderer.CabinetRenderState> {

    public static class CabinetRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState lower = new BlockModelRenderState();
        public final BlockModelRenderState upper = new BlockModelRenderState();
        public boolean skip;
        public List<ItemStackRenderState> items = Collections.emptyList();
        @Nullable
        public DyeColor dye;
        public float doorAngle;
        public boolean flipped;
        public Direction facing = Direction.NORTH;
        public CabinetBlock.CabinetModelType modelType = CabinetBlock.CabinetModelType.SMALL;
    }

    private static final float DOOR_ORIGIN_X = 0.84375f;
    private static final float DOOR_ORIGIN_Z = 0.1875f;

    private final ItemModelResolver itemModelResolver;

    public CabinetRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CabinetRenderState createRenderState() {
        return new CabinetRenderState();
    }

    @Override
    public void extractRenderState(CabinetBlockEntity blockEntity, CabinetRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        final var state = blockEntity.getBlockState();
        renderState.dye = state.getBlock() instanceof CounterBlock counterBlock ? counterBlock.getColor() : null;
        renderState.facing = state.getValue(CounterBlock.FACING);
        renderState.doorAngle = blockEntity.getDoorAnimator().getRenderAngle(delta);
        renderState.flipped = blockEntity.isFlipped();
        renderState.modelType = state.getValue(CabinetBlock.MODEL_TYPE);
        renderState.skip = renderState.modelType == CabinetBlock.CabinetModelType.LARGE_UPPER;

        final var lowerParts = renderState.lower.setupModel(new Matrix4f(), false);
        final var lowerModel = getLowerDoorModel(renderState.dye, renderState.flipped, renderState.modelType);
        lowerModel.asBlockStateModel().collectParts(renderState.lower.scratchRandomSource(42), lowerParts);

        if (renderState.modelType == CabinetBlock.CabinetModelType.LARGE_LOWER) {
            final var upperParts = renderState.upper.setupModel(new Matrix4f(), false);
            final var upperModel = getUpperDoorModel(renderState.dye, renderState.flipped);
            upperModel.asBlockStateModel().collectParts(renderState.upper.scratchRandomSource(42), upperParts);
        } else {
            renderState.upper.clear();
        }

        final var id = (int) blockEntity.getBlockPos().asLong();
        renderState.items = new ArrayList<>();
        final var container = blockEntity.getContainer();
        for (int i = 0; i < container.getContainerSize(); i++) {
            final var itemStack = container.getItem(i);
            final var itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, id + i);
            renderState.items.add(itemStackRenderState);
        }
    }

    @Override
    public void submit(CabinetRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.skip) {
            return;
        }

        poseStack.pushPose();

        float doorOriginX = DOOR_ORIGIN_X;
        float doorDirection = -1f;
        if (renderState.flipped) {
            doorOriginX = 1f - doorOriginX;
            doorDirection = 1f;
        }

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
        poseStack.translate(-0.5f, 0f, -0.5f);

        poseStack.translate(doorOriginX, 0f, DOOR_ORIGIN_Z);
        poseStack.mulPose(Axis.YP.rotationDegrees(doorDirection * (float) Math.toDegrees(renderState.doorAngle)));
        poseStack.translate(-doorOriginX, 0f, -DOOR_ORIGIN_Z);

        renderState.lower.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        if (!renderState.upper.isEmpty()) {
            poseStack.translate(0f, 1f, 0f);
            renderState.upper.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();

        if (renderState.doorAngle <= 0f) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.3f, 0.3f, 0.3f);

        final int shelfCount = renderState.modelType == CabinetBlock.CabinetModelType.LARGE_LOWER ? 4 : 2;
        final int itemsPerShelf = Math.max(1, renderState.items.size() / shelfCount);
        final int itemsPerRow = Math.max(1, itemsPerShelf / 2);
        for (int i = renderState.items.size() - 1; i >= 0; i--) {
            final var itemStackRenderState = renderState.items.get(i);
            if (itemStackRenderState.isEmpty()) {
                continue;
            }

            final int shelfIndex = Math.min(shelfCount - 1, i / itemsPerShelf);
            final int indexInShelf = i % itemsPerShelf;
            final int rowIndex = indexInShelf % itemsPerRow;
            final float spacing = 2f / itemsPerRow;
            final float offsetX = (rowIndex - itemsPerRow / 2f) * -spacing + (indexInShelf >= itemsPerRow ? -0.2f : 0f);
            final float offsetY = switch (shelfIndex) {
                case 0 -> renderState.modelType == CabinetBlock.CabinetModelType.LARGE_LOWER ? 2.0f : 0.9f;
                case 1 -> renderState.modelType == CabinetBlock.CabinetModelType.LARGE_LOWER ? 0.9f : -0.45f;
                case 2 -> -0.45f;
                default -> -1.55f;
            };
            final float offsetZ = indexInShelf < itemsPerRow ? 0.5f : -0.5f;

            poseStack.pushPose();
            poseStack.translate(offsetX, offsetY, offsetZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(45f));
            itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private DeferredBlockStateModel getLowerDoorModel(@Nullable DyeColor blockColor, boolean flipped, CabinetBlock.CabinetModelType modelType) {
        if (modelType == CabinetBlock.CabinetModelType.LARGE_LOWER) {
            return flipped ? ModModels.cabinetDoorsLargeLowerFlipped.get(blockColor) : ModModels.cabinetDoorsLargeLower.get(blockColor);
        }
        return flipped ? ModModels.cabinetDoorsFlipped.get(blockColor) : ModModels.cabinetDoors.get(blockColor);
    }

    private DeferredBlockStateModel getUpperDoorModel(@Nullable DyeColor blockColor, boolean flipped) {
        return flipped ? ModModels.cabinetDoorsLargeUpperFlipped.get(blockColor) : ModModels.cabinetDoorsLargeUpper.get(blockColor);
    }
}
