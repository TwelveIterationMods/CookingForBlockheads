package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.blaycommon.RenderUtils;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class CounterRenderer extends TileEntitySpecialRenderer<TileCounter> {

    public static IBakedModel modelDoor;
    public static IBakedModel modelDoorFlipped;

    @Override
    public void renderTileEntityAt(TileCounter tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

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
        float o = -1f;
        if(isFlipped) {
            n = 1 - n;
//            m = 1 - m;
            o = 1f;
        }
        GlStateManager.translate(n, 0f, m);
        GlStateManager.rotate(o * (float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translate(-n, 0f, -m);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderModel(isFlipped ? modelDoorFlipped : modelDoor, 0xFFFFFFFF);
        GlStateManager.popMatrix();

        // Render the content if the door is open
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
