package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.tile.ToasterTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class ToasterRenderer extends TileEntityRenderer<ToasterTileEntity> {

    public ToasterRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ToasterTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getBlockState();

        ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
        ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            matrixStack.push();
            RenderUtils.applyBlockAngle(matrixStack, state);
            matrixStack.translate(0f, 0.25 + (tileEntity.isActive() ? -0.075 : 0), 0);
            float shrinkage = 0.3f;
            matrixStack.scale(shrinkage, shrinkage, shrinkage);
            if (!leftStack.isEmpty()) {
                matrixStack.push();
                matrixStack.translate(0f, 0f, 0.2f);
                RenderUtils.renderItem(leftStack, combinedLightIn, matrixStack, buffer);
                matrixStack.pop();
            }
            if (!rightStack.isEmpty()) {
                matrixStack.push();
                matrixStack.translate(0f, 0f, -0.2f);
                RenderUtils.renderItem(rightStack, combinedLightIn, matrixStack, buffer);
                matrixStack.pop();
            }
            matrixStack.pop();
        }
    }

}
