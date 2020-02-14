package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.ToasterTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class ToasterRenderer extends TileEntityRenderer<ToasterTileEntity> {

    public ToasterRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ToasterTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        if (state.getBlock() != ModBlocks.toaster) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
        ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate( 0.5, 0.25 + (tileEntity.isActive() ? -0.075 : 0),  0.5);
            matrixStack.rotate(new Quaternion(0f, RenderUtils.getFacingAngle(state), 0f, true));
            matrixStack.scale(0.4f, 0.4f, 0.4f);
            if (!leftStack.isEmpty()) {
                RenderUtils.renderItem(itemRenderer, leftStack, -0.025f, 0f, 0.15f, 0f, 0f, 0f, 0f);
            }
            if (!rightStack.isEmpty()) {
                RenderUtils.renderItem(itemRenderer, rightStack, -0.025f, 0f, -0.15f, 0f, 0f, 0f, 0f);
            }
            matrixStack.pop();
        }
    }

}
