package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.ToasterBlock;
import net.blay09.mods.cookingforblockheads.block.entity.ToasterBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ToasterRenderer implements BlockEntityRenderer<ToasterBlockEntity, ToasterRenderer.ToasterRenderState> {

    public static class ToasterRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState leftItem = new ItemStackRenderState();
        public final ItemStackRenderState rightItem = new ItemStackRenderState();
        public boolean active;
        public Direction facing = Direction.NORTH;
    }

    private final ItemModelResolver itemModelResolver;

    public ToasterRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public ToasterRenderState createRenderState() {
        return new ToasterRenderState();
    }

    @Override
    public void extractRenderState(ToasterBlockEntity blockEntity, ToasterRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.facing = blockEntity.getBlockState().getValue(ToasterBlock.FACING);
        renderState.active = blockEntity.isActive();
        itemModelResolver.updateForTopItem(renderState.leftItem, blockEntity.getContainer().getItem(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.rightItem, blockEntity.getContainer().getItem(1), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(ToasterRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (!renderState.leftItem.isEmpty() || !renderState.rightItem.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.5f, 0f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180f));
            poseStack.translate(-0.5f, 0f, -0.5f);

            poseStack.translate(0.5f, 0.25 + (renderState.active ? -0.075 : 0), 0.5f);
            float shrinkage = 0.3f;
            poseStack.scale(shrinkage, shrinkage, shrinkage);
            if (!renderState.leftItem.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0f, 0f, 0.2f);
                renderState.leftItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
            if (!renderState.rightItem.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0f, 0f, -0.2f);
                renderState.rightItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
    }

}
