package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.cookingforblockheads.block.entity.ToolRackBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ToolRackRenderer implements BlockEntityRenderer<ToolRackBlockEntity> {

    public ToolRackRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ToolRackBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!blockEntity.hasLevel()) {
            return;
        }

        Level level = blockEntity.getLevel();

        ItemStack leftStack = blockEntity.getContainer().getItem(0);
        ItemStack rightStack = blockEntity.getContainer().getItem(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            poseStack.pushPose();
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
            poseStack.translate(0f, 0.6f, 0.4f);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            if (!leftStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.4, 0f, 0f);
                RenderUtils.renderItem(leftStack, combinedLight, poseStack, buffer, level);
                poseStack.popPose();
            }

            if (!rightStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(-0.4, 0f, 0f);
                RenderUtils.renderItem(rightStack, combinedLight, poseStack, buffer, level);
                poseStack.popPose();
            }

            poseStack.popPose();
        }
    }

}
