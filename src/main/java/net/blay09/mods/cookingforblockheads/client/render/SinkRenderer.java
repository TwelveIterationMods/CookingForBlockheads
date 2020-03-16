
package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.SinkTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SinkRenderer extends TileEntityRenderer<SinkTileEntity> {

    public SinkRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(SinkTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        int waterAmount = tileEntity.getWaterAmount();
        int capacity = tileEntity.getWaterCapacity();
        if (waterAmount > 0) {
            matrixStack.push();
            float filledPercentage = waterAmount / (float) capacity;
            matrixStack.translate(0f, 0.5f - 0.5f * filledPercentage, 0f);
            matrixStack.scale(1f, filledPercentage, 1f);
            dispatcher.getBlockModelRenderer().renderModel(world, ModModels.sinkLiquid, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.solid()), false, world.rand, 0, Integer.MAX_VALUE, EmptyModelData.INSTANCE);
            matrixStack.pop();
        }
    }

}
