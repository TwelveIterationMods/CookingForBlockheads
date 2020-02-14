package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class OvenRenderer extends TileEntityRenderer<OvenTileEntity> {

    public OvenRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(OvenTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        Direction facing = tileEntity.getFacing();
        float blockAngle = facing.getHorizontalAngle();
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);

        // Render the oven door
        matrixStack.push();
        matrixStack.translate(0.5f, 0, 0.5f);
        matrixStack.rotate(new Quaternion(0f, blockAngle, 0f, true));
        matrixStack.translate(-0.5f, 0f, -0.5f);
        matrixStack.rotate(new Quaternion(-(float) Math.toDegrees(doorAngle), 0f, 0f, true));
        // TODO bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model = doorAngle < 0.3f && tileEntity.isBurning() ? ModModels.ovenDoorActive : ModModels.ovenDoor;
        // TODO dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, 1f, 1f, 1f);
        matrixStack.pop();

        // Render the oven tools
        matrixStack.push();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        matrixStack.translate(0.5, 1.05, 0.5);
        matrixStack.rotate(new Quaternion(0f, blockAngle, 0f, true));
        matrixStack.scale(0.4f, 0.4f, 0.4f);
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
        matrixStack.pop();

        // Render the oven content when the door is open
        if (doorAngle > 0f) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.4, 0.5);
            matrixStack.rotate(new Quaternion(0, blockAngle, 0f, true));
            matrixStack.scale(0.3f, 0.3f, 0.3f);
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
            matrixStack.pop();
        }
    }

}
