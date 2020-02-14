package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.tile.SpiceRackTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class SpiceRackRenderer extends TileEntityRenderer<SpiceRackTileEntity> {

    public SpiceRackRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(SpiceRackTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        matrixStack.push();
        RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
        matrixStack.translate(-0.4, 0.75, -0.3);
        matrixStack.rotate(new Quaternion(0f, 90f, 0f, true));
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < tileEntity.getItemHandler().getSlots(); i++) {
            ItemStack itemStack = tileEntity.getItemHandler().getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                matrixStack.push();
                matrixStack.translate(0f, 0f, 0.2f * i);
                matrixStack.rotate(new Quaternion(0f, -20f, 0f, true));
                RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
                matrixStack.pop();
            }
        }

        matrixStack.pop();
    }

}
