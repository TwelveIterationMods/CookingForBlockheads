package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.entity.FruitBasketBlockEntity;
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

import java.util.Collections;
import java.util.List;

public class FruitBasketRenderer implements BlockEntityRenderer<FruitBasketBlockEntity, FruitBasketRenderer.FruitBasketRenderState> {

    public static class FruitBasketRenderState extends BlockEntityRenderState {
        public List<ItemStackRenderState> items = Collections.emptyList();
    }

    private final ItemModelResolver itemModelResolver;

    public FruitBasketRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public FruitBasketRenderState createRenderState() {
        return new FruitBasketRenderState();
    }

    @Override
    public void extractRenderState(FruitBasketBlockEntity blockEntity, FruitBasketRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        final var id = (int) blockEntity.getBlockPos().asLong();
        for (int i = 0; i < blockEntity.getContainer().getContainerSize(); i++) {
            final var itemStack = blockEntity.getContainer().getItem(i);
            final var itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, id + i);
            renderState.items.add(itemStackRenderState);
        }
    }

    @Override
    public void submit(FruitBasketRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        RenderUtils.applyBlockAngle(poseStack, renderState.blockState);
        poseStack.scale(0.25f, 0.25f, 0.25f);
        int itemsPerRow = 7;
        for (int i = 0; i < renderState.items.size(); i++) {
            final var itemStackRenderState = renderState.items.get(i);
            if (!itemStackRenderState.isEmpty()) {
                int rowIndex = i % itemsPerRow;
                int colIndex = i / itemsPerRow;
                float antiZFight = ((rowIndex % 2 != 0) ? 0.1f : 0f) + i * 0.01f;
                float curX = -0.75f + rowIndex * 0.25f + ((colIndex == 3) ? 0.15f : 0f);
                float curY = -1.35f;
                float curZ = -0.75f + colIndex * 0.35f + antiZFight;
                poseStack.pushPose();
                poseStack.translate(curX, curY, curZ);
                poseStack.mulPose(Axis.XP.rotationDegrees(25f));
                itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

}
