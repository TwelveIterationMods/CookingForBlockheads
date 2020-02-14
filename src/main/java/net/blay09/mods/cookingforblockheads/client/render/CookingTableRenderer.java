package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.tile.CookingTableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class CookingTableRenderer extends TileEntityRenderer<CookingTableTileEntity> {

    public CookingTableRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CookingTableTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        float angle = state.get(BlockKitchen.FACING).getHorizontalAngle();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = tileEntity.getNoFilterBook();
        if (!itemStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5, 1, 0.5);
            matrixStack.rotate(new Quaternion(0f, angle, 0f, true));
            matrixStack.rotate(new Quaternion(90f, 0f, 0f, true));
            matrixStack.scale(0.5f, 0.5f, 0.5f);
            // TODO RenderUtils.renderItem(itemRenderer, itemStack, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
            matrixStack.pop();
        }
    }

}
