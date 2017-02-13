package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileSpiceRack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class SpiceRackRenderer extends TileEntitySpecialRenderer<TileSpiceRack> {

	@Override
	public void renderTileEntityAt(TileSpiceRack tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
		IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
		if(state.getBlock() != ModBlocks.spiceRack) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
			return;
		}
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.translate(x + 0.5, y + 0.6, z + 0.5);
		GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
		GlStateManager.translate(0, 0, 0.4);
		GlStateManager.rotate(90f, 0f, 1f, 0f);
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		for(int i = 0; i < tileEntity.getItemHandler().getSlots(); i++) {
			ItemStack itemStack = tileEntity.getItemHandler().getStackInSlot(i);
			if(!itemStack.isEmpty()) {
				RenderUtils.renderItem(itemRenderer, itemStack, 0.15f, 0.35f, 0.8f - i * 0.2f, 0f, 0f, 0f, 0f);
			}
		}
		GlStateManager.popMatrix();
	}

}
