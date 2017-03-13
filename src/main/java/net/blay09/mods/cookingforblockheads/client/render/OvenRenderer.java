package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class OvenRenderer extends TileEntitySpecialRenderer<TileOven> {

	protected static BlockRendererDispatcher blockRenderer;

	public static IBakedModel modelDoor;
	public static IBakedModel modelDoorActive;

	@Override
	public void renderTileEntityAt(TileOven tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
		BlockPos pos = tileEntity.getPos();
		IBlockState state = tileEntity.getWorld().getBlockState(pos);
		if (state.getBlock() != ModBlocks.oven) {
			return; // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
		}
		if (blockRenderer == null) {
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		}
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.translate(x + 0.5, y + 1.05, z + 0.5);
		GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
		GlStateManager.scale(0.4f, 0.4f, 0.4f);
		ItemStack itemStack = tileEntity.getToolItem(0);
		if (!itemStack.isEmpty()) {
			RenderUtils.renderItem(itemRenderer, itemStack, -0.55f, 0f, 0.5f, 45f, 1f, 0f, 0f);
		}
		itemStack = tileEntity.getToolItem(1);
		if (!itemStack.isEmpty()) {
			RenderUtils.renderItem(itemRenderer, itemStack, 0.55f, 0f, 0.5f, 45f, 1f, 0f, 0f);
		}
		itemStack = tileEntity.getToolItem(2);
		if (!itemStack.isEmpty()) {
			RenderUtils.renderItem(itemRenderer, itemStack, -0.55f, 0f, -0.5f, 45f, 1f, 0f, 0f);
		}
		itemStack = tileEntity.getToolItem(3);
		if (!itemStack.isEmpty()) {
			RenderUtils.renderItem(itemRenderer, itemStack, 0.55f, 0f, -0.5f, 45f, 1f, 0f, 0f);
		}
		GlStateManager.popMatrix();


		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5f, y, z + 0.5f);
		GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
		GlStateManager.translate(-0.5f, 0f, -0.5f);
		float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
		GlStateManager.rotate(-(float) Math.toDegrees(doorAngle), 1f, 0f, 0f);

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();

		GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		try {
			EnumBlockRenderType renderType = state.getRenderType();
			if (renderType == EnumBlockRenderType.MODEL) {
				dispatcher.getBlockModelRenderer().renderModel(tileEntity.getWorld(), modelDoor, state, pos, renderer, false);
			}
		} catch (Throwable ignored) {
		}

		tessellator.draw();

		GlStateManager.popMatrix();


		if (doorAngle > 0f) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.4, z + 0.5);
			GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
			GlStateManager.scale(0.3f, 0.3f, 0.3f);
			float offsetX = 0.825f;
			float offsetZ = 0.8f;
			for (int i = 0; i < 9; i++) {
				itemStack = tileEntity.getItemHandler().getStackInSlot(7 + i);
				if (!itemStack.isEmpty()) {
					RenderUtils.renderItem(itemRenderer, itemStack, offsetX, 0f, offsetZ, 90f, 1f, 0f, 0f);
				}
				offsetX -= 0.8f;
				if (offsetX < -0.8f) {
					offsetX = 0.825f;
					offsetZ -= 0.8f;
				}
			}
			GlStateManager.popMatrix();
		}
	}

}
