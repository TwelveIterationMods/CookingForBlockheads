package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class OvenRenderer implements BlockEntityRenderer<OvenBlockEntity> {

    private static final Random random = new Random();

    public OvenRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(OvenBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        Direction facing = blockEntity.getFacing();
        float doorAngle = blockEntity.getDoorAnimator().getRenderAngle(partialTicks);

        // Render the oven door
        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
        poseStack.translate(-0.5f, 0f, -0.5f);
        poseStack.mulPose(new Quaternion(-(float) Math.toDegrees(doorAngle), 0f, 0f, true));
        BakedModel model = doorAngle < 0.3f && blockEntity.isBurning() ? ModModels.ovenDoorActive : ModModels.ovenDoor;
        dispatcher.getModelRenderer().tesselateBlock(level, model, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.solid()), false, random, 0, 0);
        poseStack.translate(0f, 0f, -1f);
        dispatcher.getModelRenderer().tesselateBlock(level, ModModels.ovenDoorHandle, blockEntity.getBlockState(), blockEntity.getBlockPos().relative(facing), poseStack, buffer.getBuffer(RenderType.solid()), false, random, 0, 0);
        poseStack.popPose();

        // Render the oven tools
        poseStack.pushPose();
        poseStack.translate(0f, 1.05, 0f);
        RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
        poseStack.scale(0.4f, 0.4f, 0.4f);
        ItemStack itemStack = blockEntity.getToolItem(0);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55f, 0f, 0.5f);
            poseStack.mulPose(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
            poseStack.popPose();
        }

        itemStack = blockEntity.getToolItem(1);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55f, 0f, 0.5f);
            poseStack.mulPose(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
            poseStack.popPose();
        }

        itemStack = blockEntity.getToolItem(2);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55f, 0f, -0.5f);
            poseStack.mulPose(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
            poseStack.popPose();
        }

        itemStack = blockEntity.getToolItem(3);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55f, 0f, -0.5f);
            poseStack.mulPose(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
            poseStack.popPose();
        }
        poseStack.popPose();

        // Render the oven content when the door is open
        if (doorAngle > 0f) {
            poseStack.pushPose();
            poseStack.translate(0, 0.4, 0);
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
            poseStack.scale(0.3f, 0.3f, 0.3f);
            float offsetX = 0.825f;
            float offsetZ = 0.8f;
            for (int i = 0; i < 9; i++) {
                itemStack = blockEntity.getContainer().getItem(7 + i);
                if (!itemStack.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.translate(offsetX, 0f, offsetZ);
                    poseStack.mulPose(new Quaternion(90f, 0f, 0f, true));
                    RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
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
    }

}
