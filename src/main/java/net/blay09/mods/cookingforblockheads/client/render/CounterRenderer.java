package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class CounterRenderer extends TileEntitySpecialRenderer<TileCounter> {

    public static IBakedModel[][] models;
    public static IBakedModel[][] modelsFlipped;

    private static final float[] doorOriginsX = new float[]{
            1 - 0.84375f,
            0.09375f,
            0.84375f,
            1 - 0.09375f
    };

    private static final float[] doorOriginsZ = new float[]{
            1 - 0.09375f,
            1 - 0.84375f,
            0.09375f,
            0.84375f
    };

    @Override
    public void render(TileCounter tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

        EnumFacing facing = tileEntity.getFacing();
        EnumDyeColor blockColor = tileEntity.getDyedColor();
        float blockAngle = RenderUtils.getFacingAngle(tileEntity.getFacing());
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = tileEntity.isFlipped();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        float doorOriginX = doorOriginsX[facing.getHorizontalIndex()];
        float doorOriginZ = doorOriginsZ[facing.getHorizontalIndex()];
        float doorDirection = -1f;
        if (isFlipped) {
            if (facing.getAxis() == EnumFacing.Axis.X) {
                doorOriginZ = 1 - doorOriginZ;
            } else {
                doorOriginX = 1 - doorOriginX;
            }
            doorDirection = 1f;
        }

        GlStateManager.translate(doorOriginX, 0f, doorOriginZ);
        GlStateManager.rotate(doorDirection * (float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translate(-doorOriginX, 0f, -doorOriginZ);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model = isFlipped ? modelsFlipped[facing.getHorizontalIndex()][blockColor.getMetadata()] : models[tileEntity.getFacing().getHorizontalIndex()][blockColor.getMetadata()];
        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();

        // Render the content if the door is open
        if (doorAngle > 0f) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotate(blockAngle, 0f, 1f, 0f);
            GlStateManager.scale(0.3f, 0.3f, 0.3f);
            float topY = 0.35f;
            IItemHandler itemHandler = tileEntity.getItemHandler();
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    int rowIndex = i % 13;
                    offsetX = 0.7f;
                    float spacing = 0.175f;
                    if (rowIndex / 9 > 0) {
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
