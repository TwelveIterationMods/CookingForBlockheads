package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.tile.ToolRackTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class ToolRackRenderer extends TileEntityRenderer<ToolRackTileEntity> {

    public ToolRackRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ToolRackTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
        ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            matrixStack.push();
            RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
            matrixStack.translate(0f, 0.6f, -0.4f);
            matrixStack.scale(0.5f, 0.5f, 0.5f);

            if (!leftStack.isEmpty()) {
                matrixStack.push();
                matrixStack.translate(-0.4, 0f, 0f);
                RenderUtils.renderItem(leftStack, combinedLight, matrixStack, buffer);
                matrixStack.pop();
            }

            if (!rightStack.isEmpty()) {
                matrixStack.push();
                matrixStack.translate(0.4, 0f, 0f);
                RenderUtils.renderItem(rightStack, combinedLight, matrixStack, buffer);
                matrixStack.pop();
            }

            matrixStack.pop();
        }
    }

}
