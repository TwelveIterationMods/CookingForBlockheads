package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.tile.SpiceRackBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class SpiceRackRenderer implements BlockEntityRenderer<SpiceRackBlockEntity> {

    public SpiceRackRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SpiceRackBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasLevel()) {
            return;
        }

        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, tileEntity.getBlockState());
        poseStack.translate(-0.4, 0.75, 0.3);
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < tileEntity.getContainer().getContainerSize(); i++) {
            ItemStack itemStack = tileEntity.getContainer().getItem(i);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0f, 0f, 0.2f * i);
                poseStack.mulPose(Axis.YN.rotationDegrees(20));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

}
