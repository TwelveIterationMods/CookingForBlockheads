package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.FridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FridgeRenderer extends TileEntityRenderer<FridgeTileEntity> {

    @Override
    public void render(FridgeTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        FridgeBlock.FridgeModelType fridgeModelType = state.get(FridgeBlock.MODEL_TYPE);
        if (fridgeModelType == FridgeBlock.FridgeModelType.INVISIBLE) {
            return;
        }

        // Render the oven door
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        float blockAngle = RenderUtils.getFacingAngle(state);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = state.get(FridgeBlock.FLIPPED);
        boolean isLarge = fridgeModelType == FridgeBlock.FridgeModelType.LARGE;
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5f, y, z + 0.5f);
        GlStateManager.rotatef(blockAngle, 0f, 1f, 0f);
        GlStateManager.translatef(-0.5f, 0f, -0.5f);

        float originX = 0.9375f - 0.5f / 16f;
        float originZ = 0.0625f + 0.5f / 16f;
        if (isFlipped) {
            originX = 1f - originX;
            doorAngle *= -1f;
        }

        GlStateManager.translatef(originX, 0f, originZ);
        GlStateManager.rotatef(-(float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translatef(-originX, 0f, -originZ);

        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model;
        if (isLarge) {
            model = isFlipped ? ModModels.fridgeDoorLargeFlipped : ModModels.fridgeDoorLarge;
        } else {
            model = isFlipped ? ModModels.fridgeDoorFlipped : ModModels.fridgeDoor;
        }

        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();

        // Render the fridge content if the door is open
        if (doorAngle != 0f) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            GlStateManager.pushMatrix();
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
            GlStateManager.scalef(0.3f, 0.3f, 0.3f);
            float topY = fridgeModelType == FridgeBlock.FridgeModelType.LARGE ? 3.25f : 0.35f;
            IItemHandler itemHandler = tileEntity.getCombinedItemHandler();
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (fridgeModelType == FridgeBlock.FridgeModelType.LARGE) {
                        int rowIndex = i % 18;
                        offsetX = 0.7f - (rowIndex % 9) * 0.175f;
                        offsetY = topY - i / 18 * 1.25f;
                        offsetZ = 0.5f - (rowIndex / 9) * 0.9f;
                    } else {
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
                    }
                    RenderUtils.renderItem(itemRenderer, itemStack, offsetX, offsetY, offsetZ, 45f, 0f, 1f, 0f);
                }
            }
            GlStateManager.popMatrix();
        }
    }

}
