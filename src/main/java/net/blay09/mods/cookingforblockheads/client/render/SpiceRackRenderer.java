package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.tile.SpiceRackTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
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

        BlockState state = tileEntity.getBlockState();
        float angle = state.get(BlockKitchen.FACING).getHorizontalAngle();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        matrixStack.push();
        matrixStack.translate(0.5, 0.6, 0.5);
        matrixStack.rotate(new Quaternion(0f, angle, 0f, true));
        matrixStack.translate(0, 0, 0.4);
        matrixStack.rotate(new Quaternion(0f, 90f, 0f, true));
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < tileEntity.getItemHandler().getSlots(); i++) {
            ItemStack itemStack = tileEntity.getItemHandler().getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                // TODO RenderUtils.renderItem(itemRenderer, itemStack, 0.15f, 0.35f, 0.8f - i * 0.2f, -30f, 0f, 1f, 0f);
            }
        }

        matrixStack.pop();
    }

}
