package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.tile.TileToolRack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class ToolRackRenderer extends TileEntitySpecialRenderer<TileToolRack> {

	@Override
	public void renderTileEntityAt(TileToolRack tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
		ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
		if (leftStack != null || rightStack != null) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.translate(x + 0.5, y + 0.6, z + 0.5);
			GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
			GlStateManager.translate(0, 0, 0.4);
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			if(leftStack != null) {
				RenderUtils.renderItem(itemRenderer, leftStack, 0.45f, 0f, 0f, 0f, 0f, 0f, 0f);
			}
			if(rightStack != null) {
				RenderUtils.renderItem(itemRenderer, rightStack, -0.45f, 0f, 0f, 0f, 0f, 0f, 0f);
			}
			GlStateManager.popMatrix();
		}
	}

}
