package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RenderUtils {

	public static float getFacingAngle(TileEntity tileEntity) {
		IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
		float angle;
		switch (state.getValue(BlockKitchen.FACING)) {
			case NORTH:
				angle = 0;
				break;
			case SOUTH:
				angle = 180;
				break;
			case WEST:
				angle = 90;
				break;
			case EAST:
			default:
				angle = -90;
				break;
		}
		return angle;
	}

	public static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(angle, xr, yr, zr);
		if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {
			GlStateManager.rotate(180f, 0f, 1f, 0f);
		}
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();
		itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

}
