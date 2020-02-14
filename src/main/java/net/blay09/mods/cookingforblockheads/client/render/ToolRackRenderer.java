package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.tile.ToolRackTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
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

        BlockState state = tileEntity.getBlockState();
        float angle = state.get(BlockKitchen.FACING).getHorizontalAngle();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
        ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.6, 0.5);
            matrixStack.rotate(new Quaternion(0f, angle, 0f, true));
            matrixStack.translate(0, 0, 0.4);
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            if (!leftStack.isEmpty()) {
                // TODO RenderUtils.renderItem(itemRenderer, leftStack, 0.45f, 0f, 0f, 0f, 0f, 0f, 0f);
            }
            if (!rightStack.isEmpty()) {
                // TODO RenderUtils.renderItem(itemRenderer, rightStack, -0.45f, 0f, 0f, 0f, 0f, 0f, 0f);
            }
            matrixStack.pop();
        }
    }

}
