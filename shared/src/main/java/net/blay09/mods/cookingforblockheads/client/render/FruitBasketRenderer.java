package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.blay09.mods.cookingforblockheads.block.entity.FruitBasketBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FruitBasketRenderer implements BlockEntityRenderer<FruitBasketBlockEntity> {

    public FruitBasketRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FruitBasketBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = blockEntity.getLevel();
        if (world == null) {
            return;
        }

        Level level = blockEntity.getLevel();

        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
        poseStack.scale(0.25f, 0.25f, 0.25f);
        int itemsPerRow = 7;
        for (int i = 0; i < blockEntity.getContainer().getContainerSize(); i++) {
            ItemStack itemStack = blockEntity.getContainer().getItem(i);
            if (!itemStack.isEmpty()) {
                int rowIndex = i % itemsPerRow;
                int colIndex = i / itemsPerRow;
                float antiZFight = ((rowIndex % 2 != 0) ? 0.1f : 0f) + i * 0.01f;
                float curX = -0.75f + rowIndex * 0.25f + ((colIndex == 3) ? 0.15f : 0f);
                float curY = -1.35f;
                if (BaseKitchenBlock.shouldBlockRenderLowered(world, blockEntity.getBlockPos())) {
                    curY -= 0.2f;
                }

                float curZ = -0.75f + colIndex * 0.35f + antiZFight;
                poseStack.pushPose();
                poseStack.translate(curX, curY, curZ);
                poseStack.mulPose(Axis.XP.rotationDegrees(25f));
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

}
