package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.cookingforblockheads.block.CounterBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.block.entity.CounterBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CounterRenderer<T extends CounterBlockEntity> implements BlockEntityRenderer<T, CounterRenderer.CounterRenderState> {

    public static class CounterRenderState extends BlockEntityRenderState {
        public List<ItemStackRenderState> items = Collections.emptyList();
        @Nullable
        public DyeColor dye;
        public float doorAngle;
        public boolean flipped;
        public Direction facing = Direction.NORTH;
    }

    private static final float doorOriginX = 0.84375f;
    private static final float doorOriginZ = 0.09375f;

    protected float getDoorOriginX() {
        return doorOriginX;
    }

    protected float getDoorOriginZ() {
        return doorOriginZ;
    }

    protected float getBottomShelfOffsetY() {
        return -0.85f;
    }

    protected float getTopShelfOffsetY() {
        return 0.35f;
    }

    protected DeferredBlockStateModel getDoorModel(@Nullable DyeColor blockColor, boolean isFlipped) {
        return isFlipped ? ModModels.counterDoorsFlipped.get(blockColor) : ModModels.counterDoors.get(blockColor);
    }

    private final ItemModelResolver itemModelResolver;

    public CounterRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CounterRenderState createRenderState() {
        return new CounterRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, CounterRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.dye = renderState.blockState.getBlock() instanceof CounterBlock counterBlock ? counterBlock.getColor() : null;
        renderState.facing = renderState.blockState.getValue(CounterBlock.FACING);
        renderState.doorAngle = blockEntity.getDoorAnimator().getRenderAngle(delta);
        renderState.flipped = blockEntity.isFlipped();

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
    public void submit(CounterRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        float doorOriginX = getDoorOriginX();
        float doorOriginZ = getDoorOriginZ();
        float doorDirection = -1f;
        if (renderState.flipped) {
            doorOriginX = 1 - doorOriginX;
            doorDirection = 1f;
        }

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
        poseStack.translate(-0.5f, 0f, -0.5f);

        poseStack.translate(doorOriginX, 0f, doorOriginZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(doorDirection * (float) Math.toDegrees(renderState.doorAngle)));
        poseStack.translate(-doorOriginX, 0f, -doorOriginZ);

        final var model = getDoorModel(renderState.dye, renderState.flipped);
        submitNodeCollector.submitBlockModel(poseStack, RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), model.asBlockStateModel(), 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        // Render the content if the door is open
        if (renderState.doorAngle > 0f) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            int itemsPerShelf = renderState.items.size() / 2;
            int itemsPerRow = itemsPerShelf / 2;
            for (int i = renderState.items.size() - 1; i >= 0; i--) {
                final var itemStackRenderState = renderState.items.get(i);
                if (!itemStackRenderState.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    int shelfIndex = i % itemsPerShelf;
                    int rowIndex = i % itemsPerRow;
                    float spacing = 2f / (float) itemsPerRow;
                    offsetX = (rowIndex - itemsPerRow / 2f) * -spacing + (shelfIndex >= itemsPerRow ? -0.2f : 0f);
                    offsetY = i < itemsPerShelf ? getTopShelfOffsetY() : getBottomShelfOffsetY();
                    offsetZ = shelfIndex < itemsPerRow ? 0.5f : -0.5f;
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
