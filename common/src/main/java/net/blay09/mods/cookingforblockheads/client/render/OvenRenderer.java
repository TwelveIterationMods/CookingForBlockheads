package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
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

public class OvenRenderer implements BlockEntityRenderer<OvenBlockEntity, OvenRenderer.OvenRenderState> {

    public static class OvenRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState firstTool = new ItemStackRenderState();
        public final ItemStackRenderState secondTool = new ItemStackRenderState();
        public final ItemStackRenderState thirdTool = new ItemStackRenderState();
        public final ItemStackRenderState fourthTool = new ItemStackRenderState();
        public List<ItemStackRenderState> items = Collections.emptyList();
        public float doorAngle;
        public DyeColor dye = DyeColor.WHITE;
        public boolean active;
        public Direction facing = Direction.NORTH;
    }

    private final ItemModelResolver itemModelResolver;

    public OvenRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public OvenRenderState createRenderState() {
        return new OvenRenderState();
    }

    @Override
    public void extractRenderState(OvenBlockEntity blockEntity, OvenRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        itemModelResolver.updateForTopItem(renderState.firstTool, blockEntity.getToolItem(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.secondTool, blockEntity.getToolItem(1), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.thirdTool, blockEntity.getToolItem(2), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.fourthTool, blockEntity.getToolItem(3), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);

        renderState.doorAngle = blockEntity.getDoorAnimator().getRenderAngle(delta);
        renderState.dye = blockEntity.getBlockState().getBlock() instanceof OvenBlock oven ? oven.getColor() : DyeColor.WHITE;
        renderState.facing = blockEntity.getBlockState().getValue(OvenBlock.FACING);
        renderState.active = blockEntity.isBurning();

        final var id = (int) blockEntity.getBlockPos().asLong();
        renderState.items = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            final var itemStack = blockEntity.getInternalContainer().getItem(7 + i);
            final var itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, id + i);
            renderState.items.add(itemStackRenderState);
        }
    }

    @Override
    public void submit(OvenRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot() + 180));
        poseStack.translate(-0.5f, 0f, -0.5f);

        // Render the oven door
        poseStack.pushPose();
        poseStack.mulPose(Axis.XN.rotationDegrees((float) Math.toDegrees(renderState.doorAngle)));
        final var model = renderState.doorAngle < 0.3f && renderState.active ? ModModels.ovenDoorsActive.get(renderState.dye) : ModModels.ovenDoors.get(renderState.dye);
        submitNodeCollector.submitBlockModel(poseStack, RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), model.asBlockStateModel(), 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.translate(0f, 0f, -1f);
        final var handleModel = ModModels.ovenDoorHandles.get(renderState.dye);
        submitNodeCollector.submitBlockModel(poseStack, RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), handleModel.asBlockStateModel(), 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        // Render the oven tools
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.05, 0.5f);
        poseStack.scale(0.4f, 0.4f, 0.4f);

        if (!renderState.firstTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55f, 0f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            renderState.firstTool.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.secondTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55f, 0f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            renderState.secondTool.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.thirdTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55f, 0f, -0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            renderState.thirdTool.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.fourthTool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55f, 0f, -0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            renderState.fourthTool.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();

        // Render the oven content when the door is open
        if (renderState.doorAngle > 0f) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.4, 0.5);

            poseStack.scale(0.3f, 0.3f, 0.3f);
            float offsetX = 0.825f;
            float offsetZ = 0.8f;
            for (int i = 0; i < renderState.items.size(); i++) {
                final var itemStackRenderState = renderState.items.get(i);
                if (!itemStackRenderState.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.translate(offsetX, 0f, offsetZ);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90f));
                    itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    poseStack.popPose();
                }
                offsetX -= 0.8f;
                if (offsetX < -0.8f) {
                    offsetX = 0.825f;
                    offsetZ -= 0.8f;
                }
            }
            poseStack.popPose();
        }

        poseStack.popPose();
    }

}
