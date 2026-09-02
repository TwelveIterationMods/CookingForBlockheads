package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.CookieJarBlock;
import net.blay09.mods.cookingforblockheads.block.entity.CookieJarBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CookieJarRenderer implements BlockEntityRenderer<CookieJarBlockEntity, CookieJarRenderer.CookieJarRenderState> {

    private static final float[][] SLOT_OFFSETS = {
            {-0.4f, -0.42f, 0f},
            {-0.4f, -0.38f, -0.4f},
            {0.4f, -0.42f, -0.4f},
            {0.4f, -0.38f, 0f},
            {-0.4f, -0.02f, -0.4f},
            {-0.4f, 0.02f, 0f},
            {0.4f, -0.02f, 0f},
            {0.4f, 0.02f, -0.4f},
            {-0.4f, 0.28f, 0f},
            {-0.4f, 0.42f, -0.4f},
            {0.4f, 0.28f, -0.4f},
            {0.4f, 0.42f, 0f},
    };

    public static class CookieJarRenderState extends BlockEntityRenderState {
        public List<ItemStackRenderState> items = Collections.emptyList();
        public Direction facing = Direction.NORTH;
    }

    private final ItemModelResolver itemModelResolver;

    public CookieJarRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CookieJarRenderState createRenderState() {
        return new CookieJarRenderState();
    }

    @Override
    public void extractRenderState(CookieJarBlockEntity blockEntity, CookieJarRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.facing = blockEntity.getBlockState().getValue(CookieJarBlock.FACING);

        final var id = (int) blockEntity.getBlockPos().asLong();
        final var container = blockEntity.getContainer();
        renderState.items = new ArrayList<>(container.getContainerSize());
        for (int i = 0; i < container.getContainerSize(); i++) {
            final var itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemStackRenderState, container.getItem(i), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, id + i);
            renderState.items.add(itemStackRenderState);
        }
    }

    @Override
    public void submit(CookieJarRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.rotateDegrees(Axis.YP, -renderState.facing.toYRot() + 180f);
        poseStack.translate(-0.5f, 0f, -0.5f);

        poseStack.translate(0.5f, 0.2f, 0.5f);
        poseStack.scale(0.22f, 0.22f, 0.22f);
        final int itemCount = renderState.items.size();
        if (itemCount != 0) {
            for (int i = 0; i < SLOT_OFFSETS.length; i++) {
                final int slotIndex = i * itemCount / SLOT_OFFSETS.length;

                final var itemStackRenderState = renderState.items.get(slotIndex);
                if (itemStackRenderState.isEmpty()) {
                    continue;
                }

                final var slotOffset = SLOT_OFFSETS[i];
                poseStack.pushPose();
                poseStack.translate(slotOffset[0], slotOffset[1], slotOffset[2] + i * 0.03f);
                poseStack.rotateDegrees(Axis.XP, 50f);
                poseStack.rotateDegrees(Axis.YP, i % 2 == 0 ? 20f : -20f);
                itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

}
