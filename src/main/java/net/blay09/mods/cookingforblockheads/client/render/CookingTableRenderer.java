package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class CookingTableRenderer extends TileEntitySpecialRenderer<TileCookingTable> {

    @Override
    public void renderTileEntityAt(TileCookingTable tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        ItemStack itemStack = tileEntity.getNoFilterBook();
        if (itemStack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(x + 0.5, y + 0.825, z + 0.5);
            GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
            GlStateManager.rotate(90f, 1f, 0f, 0f);
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            RenderUtils.renderItem(itemRenderer, itemStack, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
            GlStateManager.popMatrix();
        }
    }

}
