package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

public class CounterRenderer extends TileEntitySpecialRenderer<TileCounter> {

    public static IBakedModel modelDoor;

    @Override
    public void renderTileEntityAt(TileCounter tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        BlockPos pos = tileEntity.getPos();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();

        float blockAngle = RenderUtils.getFacingAngle(tileEntity.getFacing());
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = tileEntity.isFlipped();

        // Render the door
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y, z + 0.5f);
        GlStateManager.rotate(blockAngle, 0f, 1f, 0f);
        GlStateManager.translate(-0.5f, 0f, -0.5f);
        float n = 0.84375f;
        float m = 0.09375f;
        GlStateManager.translate(n, 0f, m);
        GlStateManager.rotate(-(float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translate(-n, 0f, -m);
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        dispatcher.getBlockModelRenderer().renderModel(tileEntity.getWorld(), modelDoor, ModBlocks.oven.getDefaultState(), pos, renderer, false);
        tessellator.draw();
        GlStateManager.popMatrix();

        // Render the content if hte door is open
        if(doorAngle > 0f) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotate(blockAngle, 0f, 1f, 0f);
            GlStateManager.scale(0.3f, 0.3f, 0.3f);
            float topY = 0.25f;
            IItemHandler itemHandler = tileEntity.getItemHandler();
            for(int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if(!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    int rowIndex = i % 13;
                    offsetX = 0.7f;
                    float spacing = 0.175f;
                    if(rowIndex / 9 > 0) {
                        offsetX -= 0.2f;
                        spacing *= 2;
                    }
                    offsetX -= (rowIndex % 9) * spacing;
                    offsetY = topY - i / 13 * 1.25f;
                    offsetZ = 0.5f - (rowIndex / 9) * 0.9f;
                    RenderUtils.renderItem(itemRenderer, itemStack, offsetX, offsetY, offsetZ, 45f, 0f, 1f, 0f);
                }
            }
            GlStateManager.popMatrix();
        }
    }

}
