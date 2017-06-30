package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileToaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class ToasterRenderer extends TileEntitySpecialRenderer<TileToaster> {

	@Override
	public void render(TileToaster tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
		if(state.getBlock() != ModBlocks.toaster) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
			return;
		}
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		ItemStack leftStack = tileEntity.getItemHandler().getStackInSlot(0);
		ItemStack rightStack = tileEntity.getItemHandler().getStackInSlot(1);
		if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.translate(x + 0.5, y + 0.25 + (tileEntity.isActive() ? -0.075 : 0), z + 0.5);
			GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
			GlStateManager.scale(0.4f, 0.4f, 0.4f);
			if(!leftStack.isEmpty()) {
				RenderUtils.renderItem(itemRenderer, leftStack, -0.025f, 0f, 0.15f, 0f, 0f, 0f, 0f);
			}
			if(!rightStack.isEmpty()) {
				RenderUtils.renderItem(itemRenderer, rightStack, -0.025f, 0f, -0.15f, 0f, 0f, 0f, 0f);
			}
			GlStateManager.popMatrix();
		}
	}

}
