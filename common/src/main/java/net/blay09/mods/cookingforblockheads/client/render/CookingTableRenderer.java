package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.entity.CookingTableBlockEntity;
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

public class CookingTableRenderer implements BlockEntityRenderer<CookingTableBlockEntity, CookingTableRenderer.CookingTableRenderState> {

    public static class CookingTableRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
    }

    private final ItemModelResolver itemModelResolver;

    public CookingTableRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CookingTableRenderState createRenderState() {
        return new CookingTableRenderState();
    }

    @Override
    public void extractRenderState(CookingTableBlockEntity blockEntity, CookingTableRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        itemModelResolver.updateForTopItem(renderState.item, blockEntity.getNoFilterBook(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(CookingTableRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (!renderState.item.isEmpty()) {
            poseStack.pushPose();
            RenderUtils.applyBlockAngle(poseStack, renderState.blockState);
            poseStack.translate(0, 1.0725f, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

}
