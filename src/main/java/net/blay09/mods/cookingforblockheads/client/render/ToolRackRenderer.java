package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.ToolRackTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class ToolRackRenderer extends TileEntityRenderer<ToolRackTileEntity> {

    @Override
    public void render(ToolRackTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
        ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            GlStateManager.translated(x + 0.5, y + 0.6, z + 0.5);
            GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
            GlStateManager.translated(0, 0, 0.4);
            GlStateManager.scalef(0.5f, 0.5f, 0.5f);
            if (!leftStack.isEmpty()) {
                RenderUtils.renderItem(itemRenderer, leftStack, 0.45f, 0f, 0f, 0f, 0f, 0f, 0f);
            }
            if (!rightStack.isEmpty()) {
                RenderUtils.renderItem(itemRenderer, rightStack, -0.45f, 0f, 0f, 0f, 0f, 0f, 0f);
            }
            GlStateManager.popMatrix();
        }
    }

}
