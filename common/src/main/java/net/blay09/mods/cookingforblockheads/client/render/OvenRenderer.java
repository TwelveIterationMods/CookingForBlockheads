package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OvenRenderer implements BlockEntityRenderer<OvenBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    public OvenRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(OvenBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }
        BlockState state = blockEntity.getBlockState();

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        Direction facing = blockEntity.getFacing();
        float doorAngle = blockEntity.getDoorAnimator().getRenderAngle(partialTicks);

        // Render the oven door
        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
        poseStack.translate(-0.5f, 0f, -0.5f);
        poseStack.mulPose(Axis.XN.rotationDegrees((float) Math.toDegrees(doorAngle)));
        DyeColor blockColor = state.getBlock() instanceof OvenBlock oven ? oven.getColor() : DyeColor.WHITE;
        int colorIndex = blockColor.getId();
        BakedModel model = doorAngle < 0.3f && blockEntity.isBurning() ? ModModels.ovenDoorsActive.get(colorIndex).get() : ModModels.ovenDoors.get(colorIndex)
                .get();
        dispatcher.getModelRenderer()
                .tesselateBlock(level,
                        model,
                        blockEntity.getBlockState(),
                        blockEntity.getBlockPos(),
                        poseStack,
                        buffer.getBuffer(RenderType.solid()),
                        false,
                        random,
                        0,
                        0);
        poseStack.translate(0f, 0f, -1f);
        dispatcher.getModelRenderer()
                .tesselateBlock(level,
                        ModModels.ovenDoorHandles.get(colorIndex).get(),
                        blockEntity.getBlockState(),
                        blockEntity.getBlockPos().relative(facing),
                        poseStack,
                        buffer.getBuffer(RenderType.solid()),
                        false,
                        random,
                        0,
                        0);
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
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
            poseStack.popPose();
        }

        itemStack = blockEntity.getToolItem(1);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55f, 0f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
            poseStack.popPose();
        }

        itemStack = blockEntity.getToolItem(2);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.55f, 0f, -0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
            poseStack.popPose();
        }

        itemStack = blockEntity.getToolItem(3);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.55f, 0f, -0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(45f));
            RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
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
                itemStack = blockEntity.getInternalContainer().getItem(7 + i);
                if (!itemStack.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.translate(offsetX, 0f, offsetZ);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90f));
                    RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
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
