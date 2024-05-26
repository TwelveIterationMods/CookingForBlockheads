package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.block.entity.FridgeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FridgeRenderer implements BlockEntityRenderer<FridgeBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    public FridgeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FridgeBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = tileEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        FridgeBlock.FridgeModelType fridgeModelType = state.getValue(FridgeBlock.MODEL_TYPE);
        if (fridgeModelType == FridgeBlock.FridgeModelType.LARGE_UPPER) {
            return;
        }

        // Render the oven door
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        renderDoor(level, tileEntity, poseStack, buffer, state, fridgeModelType, doorAngle);

        // Render the fridge content if the door is open
        if (doorAngle > 0f) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            RenderUtils.applyBlockAngle(poseStack, tileEntity.getBlockState());
            poseStack.scale(0.3f, 0.3f, 0.3f);
            float topY = fridgeModelType == FridgeBlock.FridgeModelType.LARGE_LOWER ? 3.25f : 0.45f;
            Container container = tileEntity.getCombinedContainer();
            for (int i = container.getContainerSize() - 1; i >= 0; i--) {
                ItemStack itemStack = container.getItem(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (fridgeModelType == FridgeBlock.FridgeModelType.LARGE_LOWER) {
                        int rowIndex = i % 18;
                        offsetX = 0.7f - (rowIndex % 9) * 0.175f;
                        offsetY = topY - (int) (i / 18f) * 1.25f;
                        offsetZ = 0.5f - (int) (rowIndex / 9f) * 0.9f;
                    } else {
                        int rowIndex = i % 13;
                        offsetX = 0.7f;
                        float spacing = 0.175f;
                        if (rowIndex / 9 > 0) {
                            offsetX -= 0.2f;
                            spacing *= 2;
                        }
                        offsetX -= (rowIndex % 9) * spacing;
                        offsetY = topY - (int) (i / 14f) * 1.25f;
                        offsetZ = 0.5f - (int) (rowIndex / 9f) * 0.9f;
                    }
                    poseStack.pushPose();
                    poseStack.translate(offsetX, offsetY, offsetZ);
                    poseStack.mulPose(Axis.YP.rotationDegrees(45f));
                    RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, level);
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
        }
    }

    private void renderDoor(Level level, FridgeBlockEntity tileEntity, PoseStack poseStack, MultiBufferSource buffer, BlockState state, FridgeBlock.FridgeModelType fridgeModelType, float doorAngle) {
        boolean isFlipped = state.getValue(FridgeBlock.FLIPPED);
        boolean isLarge = fridgeModelType == FridgeBlock.FridgeModelType.LARGE_LOWER;
        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, tileEntity.getBlockState());
        poseStack.translate(-0.5f, 0f, -0.5f);

        float originX = 0.9375f - 0.5f / 16f;
        float originZ = 0.0625f + 0.5f / 16f;
        if (isFlipped) {
            originX = 1f - originX;
            doorAngle *= -1f;
        }

        poseStack.translate(originX, 0f, originZ);
        poseStack.mulPose(Axis.YN.rotationDegrees((float) Math.toDegrees(doorAngle)));
        poseStack.translate(-originX, 0f, -originZ);

        final var blockColor = state.getBlock() instanceof FridgeBlock fridge ? fridge.getColor() : DyeColor.WHITE;
        int colorIndex = blockColor.getId();
        BakedModel lowerModel;
        BakedModel upperModel = null;
        if (isLarge) {
            lowerModel = isFlipped ? ModModels.fridgeDoorsLargeLowerFlipped.get(colorIndex).get() : ModModels.fridgeDoorsLargeLower.get(colorIndex).get();
            upperModel = isFlipped ? ModModels.fridgeDoorsLargeUpperFlipped.get(colorIndex).get() : ModModels.fridgeDoorsLargeUpper.get(colorIndex).get();
        } else {
            lowerModel = isFlipped ? ModModels.fridgeDoorsFlipped.get(colorIndex).get() : ModModels.fridgeDoors.get(colorIndex).get();
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        dispatcher.getModelRenderer()
                .tesselateBlock(level,
                        lowerModel,
                        tileEntity.getBlockState(),
                        tileEntity.getBlockPos(),
                        poseStack,
                        buffer.getBuffer(RenderType.solid()),
                        false,
                        random,
                        0,
                        0);
        if (upperModel != null) {
            poseStack.translate(0, 1, 0);
            dispatcher.getModelRenderer()
                    .tesselateBlock(level,
                            upperModel,
                            tileEntity.getBlockState(),
                            tileEntity.getBlockPos().above(),
                            poseStack,
                            buffer.getBuffer(RenderType.solid()),
                            false,
                            random,
                            0,
                            0);
        }

        poseStack.popPose();
    }

}
