package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.CounterTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;

public class CounterRenderer extends TileEntityRenderer<CounterTileEntity> {

    private static final float doorOriginX = 0.84375f;

    private static final float doorOriginZ = 0.09375f;

    protected float getDoorOriginX() {
        return doorOriginX;
    }

    protected float getDoorOriginZ() {
        return doorOriginZ;
    }

    protected float getBottomShelfOffsetY() {
        return -0.85f;
    }

    protected float getTopShelfOffsetY() {
        return 0.35f;
    }

    protected IBakedModel getDoorModel(DyeColor blockColor, boolean isFlipped) {
        return isFlipped ? ModModels.counterDoorsFlipped[blockColor.getId()] : ModModels.counterDoors[blockColor.getId()];
    }

    @Override
    public void render(CounterTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        DyeColor blockColor = tileEntity.getDyedColor();
        float blockAngle = RenderUtils.getFacingAngle(tileEntity.getFacing());
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = tileEntity.isFlipped();

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        float doorOriginX = getDoorOriginX();
        float doorOriginZ = getDoorOriginZ();
        float doorDirection = -1f;
        if (isFlipped) {
            doorOriginX = 1 - doorOriginX;
            doorDirection = 1f;
        }

        float facingAngle = RenderUtils.getFacingAngle(tileEntity.getFacing());
        GlStateManager.translatef(0.5f, 0f, 0.5f);
        GlStateManager.rotatef(facingAngle, 0f, 1f, 0f);
        GlStateManager.translatef(-0.5f, 0f, -0.5f);

        GlStateManager.translated(doorOriginX, 0f, doorOriginZ);
        GlStateManager.rotatef(doorDirection * (float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translated(-doorOriginX, 0f, -doorOriginZ);

        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model = getDoorModel(blockColor, isFlipped);
        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();

        // Render the content if the door is open
        if (doorAngle > 0f) {
            GlStateManager.pushMatrix();
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotatef(blockAngle, 0f, 1f, 0f);
            GlStateManager.scalef(0.3f, 0.3f, 0.3f);
            IItemHandler itemHandler = tileEntity.getItemHandler();
            int itemsPerShelf = itemHandler.getSlots() / 2;
            int itemsPerRow = itemsPerShelf / 2;
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    int shelfIndex = i % itemsPerShelf;
                    int rowIndex = i % itemsPerRow;
                    float spacing = 2f / (float) itemsPerRow;
                    offsetX = (rowIndex - itemsPerRow / 2f) * -spacing + (shelfIndex >= itemsPerRow ? -0.2f : 0f);
                    offsetY = i < itemsPerShelf ? getTopShelfOffsetY() : getBottomShelfOffsetY();
                    offsetZ = shelfIndex < itemsPerRow ? 0.5f : -0.5f;
                    RenderUtils.renderItem(itemRenderer, itemStack, offsetX, offsetY, offsetZ, 45f, 0f, 1f, 0f);
                }
            }

            GlStateManager.popMatrix();
        }
    }

}
