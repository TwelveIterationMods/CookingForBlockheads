package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.entity.SpiceRackBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpiceRackRenderer implements BlockEntityRenderer<SpiceRackBlockEntity, SpiceRackRenderer.SpiceRackRenderState> {

    public static class SpiceRackRenderState extends BlockEntityRenderState {
        public List<ItemStackRenderState> items = Collections.emptyList();
    }

    private final ItemModelResolver itemModelResolver;

    public SpiceRackRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public SpiceRackRenderState createRenderState() {
        return new SpiceRackRenderState();
    }

    @Override
    public void extractRenderState(SpiceRackBlockEntity blockEntity, SpiceRackRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

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
    public void submit(SpiceRackRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, renderState.blockState);
        poseStack.translate(-0.4, 0.75, 0.3);
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < renderState.items.size(); i++) {
            final var itemStackRenderState = renderState.items.get(i);
            if (!itemStackRenderState.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0f, 0f, 0.2f * i);
                poseStack.mulPose(Axis.YN.rotationDegrees(20));
                itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

}
