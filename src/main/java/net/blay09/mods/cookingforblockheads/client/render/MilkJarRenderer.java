package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.BlockMilkJar;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class MilkJarRenderer extends TileEntitySpecialRenderer<TileMilkJar> {

	protected static BlockRendererDispatcher blockRenderer;

	public static IBakedModel modelMilkLiquid;

	@Override
	public void render(TileMilkJar tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!tileEntity.hasWorld()) {
			return;
		}
		if(blockRenderer == null) {
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		}
		if(tileEntity.getMilkAmount() > 0) {
			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			GlStateManager.pushMatrix();

			GlStateManager.translate(x, y + (BlockMilkJar.isLowered(tileEntity.getWorld(), tileEntity.getPos()) ? -0.05 : 0), z);
			GlStateManager.scale(1f, tileEntity.getMilkAmount() / tileEntity.getMilkCapacity(), 1f);
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderModel(modelMilkLiquid, 0xFFFFFFFF);
			GlStateManager.popMatrix();

			RenderHelper.enableStandardItemLighting();
		}
	}

}
