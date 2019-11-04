package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class OvenRenderer extends TileEntityRenderer<OvenTileEntity> {

    @Override
    public void render(OvenTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        IBakedModel modelDoor = ModModels.milkJarLiquid; // TODO fixme
        IBakedModel modelDoorActive = ModModels.milkJarLiquid; // TODO fixme

        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        Direction facing = tileEntity.getFacing();
        float blockAngle = RenderUtils.getFacingAngle(facing);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);

        // Render the oven door
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5f, y, z + 0.5f);
        GlStateManager.rotatef(blockAngle, 0f, 1f, 0f);
        GlStateManager.translatef(-0.5f, 0f, -0.5f);
        GlStateManager.rotatef(-(float) Math.toDegrees(doorAngle), 1f, 0f, 0f);
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model = doorAngle < 0.3f && tileEntity.isBurning() ? modelDoorActive : modelDoor;
        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();

        // Render the oven tools
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        GlStateManager.translated(x + 0.5, y + 1.05, z + 0.5);
        GlStateManager.rotatef(blockAngle, 0f, 1f, 0f);
        GlStateManager.scalef(0.4f, 0.4f, 0.4f);
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

        // Render the oven content when the door is open
        if (doorAngle > 0f) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.4, z + 0.5);
            GlStateManager.rotatef(blockAngle, 0f, 1f, 0f);
            GlStateManager.scalef(0.3f, 0.3f, 0.3f);
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
