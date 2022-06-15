
package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.SinkBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.Random;

public class SinkRenderer implements BlockEntityRenderer<SinkBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    public SinkRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SinkBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        FluidTank fluidTank = blockEntity.getFluidTank();
        int waterAmount = fluidTank.getAmount();
        int capacity = fluidTank.getCapacity();
        if (waterAmount > 0) {
            poseStack.pushPose();
            float filledPercentage = waterAmount / (float) capacity;
            poseStack.translate(0f, 0.5f - 0.5f * filledPercentage, 0f);
            poseStack.scale(1f, filledPercentage, 1f);
            dispatcher.getModelRenderer().tesselateBlock(level, ModModels.sinkLiquid.get(), blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.translucent()), false, random, 0, Integer.MAX_VALUE);
            poseStack.popPose();
        }
    }

}
