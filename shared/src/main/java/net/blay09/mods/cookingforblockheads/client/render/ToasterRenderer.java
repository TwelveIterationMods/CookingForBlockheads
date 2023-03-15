package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.cookingforblockheads.tile.ToasterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ToasterRenderer implements BlockEntityRenderer<ToasterBlockEntity> {

    public ToasterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ToasterBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (!blockEntity.hasLevel()) {
            return;
        }

        Level level = blockEntity.getLevel();
        BlockState state = blockEntity.getBlockState();

        ItemStack leftStack = blockEntity.getContainer().getItem(0);
        ItemStack rightStack = blockEntity.getContainer().getItem(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            poseStack.pushPose();
            RenderUtils.applyBlockAngle(poseStack, state);
            poseStack.translate(0f, 0.25 + (blockEntity.isActive() ? -0.075 : 0), 0);
            float shrinkage = 0.3f;
            poseStack.scale(shrinkage, shrinkage, shrinkage);
            if (!leftStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0f, 0f, 0.2f);
                RenderUtils.renderItem(leftStack, combinedLightIn, poseStack, buffer, level);
                poseStack.popPose();
            }
            if (!rightStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0f, 0f, -0.2f);
                RenderUtils.renderItem(rightStack, combinedLightIn, poseStack, buffer, level);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
    }

}
