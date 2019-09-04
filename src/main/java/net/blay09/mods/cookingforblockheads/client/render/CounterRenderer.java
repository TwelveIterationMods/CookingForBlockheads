package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
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

    protected float getDoorOriginX(Direction facing) {
        return doorOriginsX[facing.getHorizontalIndex()];
    }

    protected float getDoorOriginZ(Direction facing) {
        return doorOriginsZ[facing.getHorizontalIndex()];
    }

    protected float getBottomShelfOffsetY() {
        return -0.85f;
    }

    protected float getTopShelfOffsetY() {
        return 0.35f;
    }

    protected IBakedModel getDoorModel(Direction facing, DyeColor blockColor, boolean isFlipped) {
        return isFlipped ? modelsFlipped[facing.getHorizontalIndex()][blockColor.getId()] : models[facing.getHorizontalIndex()][blockColor.getId()];
    }

    @Override
    public void render(CounterTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        Direction facing = tileEntity.getFacing();
        DyeColor blockColor = tileEntity.getDyedColor();
        float blockAngle = RenderUtils.getFacingAngle(tileEntity.getFacing());
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = tileEntity.isFlipped();

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        float doorOriginX = getDoorOriginX(facing);
        float doorOriginZ = getDoorOriginZ(facing);
        float doorDirection = -1f;
        if (isFlipped) {
            if (facing.getAxis() == Direction.Axis.X) {
                doorOriginZ = 1 - doorOriginZ;
            } else {
                doorOriginX = 1 - doorOriginX;
            }
            doorDirection = 1f;
        }

        /*GlStateManager.pushMatrix();
        GlStateManager.translate(doorOriginX, 0.25f, doorOriginZ);
        GlStateManager.scale(0.1f, 0.1f, 0.1f);
        RenderUtils.renderItem(itemRenderer, new ItemStack(Items.APPLE), 0f, 0f, 0f, 0f, 0f, 1f, 0f);
        GlStateManager.popMatrix();*/

        GlStateManager.translatef(doorOriginX, 0f, doorOriginZ);
        GlStateManager.rotatef(doorDirection * (float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translatef(-doorOriginX, 0f, -doorOriginZ);
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model = getDoorModel(facing, blockColor, isFlipped);
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
