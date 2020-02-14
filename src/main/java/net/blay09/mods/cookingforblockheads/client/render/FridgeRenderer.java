package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.FridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FridgeRenderer extends TileEntityRenderer<FridgeTileEntity> {

    public FridgeRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(FridgeTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
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
        float angle = state.get(BlockKitchen.FACING).getHorizontalAngle();
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = state.get(FridgeBlock.FLIPPED);
        boolean isLarge = fridgeModelType == FridgeBlock.FridgeModelType.LARGE;
        matrixStack.push();
        matrixStack.translate(0.5f, 0, 0.5f);
        matrixStack.rotate(new Quaternion(0f, angle, 0f, true));
        matrixStack.translate(-0.5f, 0f, -0.5f);

        float originX = 0.9375f - 0.5f / 16f;
        float originZ = 0.0625f + 0.5f / 16f;
        if (isFlipped) {
            originX = 1f - originX;
            doorAngle *= -1f;
        }

        matrixStack.translate(originX, 0f, originZ);
        matrixStack.rotate(new Quaternion(0f, -(float) Math.toDegrees(doorAngle), 0f, true));
        matrixStack.translate(-originX, 0f, -originZ);

        // TODO bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model;
        if (isLarge) {
            model = isFlipped ? ModModels.fridgeDoorLargeFlipped : ModModels.fridgeDoorLarge;
        } else {
            model = isFlipped ? ModModels.fridgeDoorFlipped : ModModels.fridgeDoor;
        }

        int color = tileEntity.getBlockState().get(BlockKitchen.COLOR).getColorValue();
        // TODO dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, (float) (color >> 16 & 255) / 255f, (float) (color >> 8 & 255) / 255f, (float) (color & 255) / 255f);
        matrixStack.pop();

        // Render the fridge content if the door is open
        if (doorAngle != 0f) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            matrixStack.push();
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.rotate(new Quaternion(0f, angle, 0f, true));
            matrixStack.scale(0.3f, 0.3f, 0.3f);
            float topY = fridgeModelType == FridgeBlock.FridgeModelType.LARGE ? 3.25f : 0.35f;
            IItemHandler itemHandler = tileEntity.getCombinedItemHandler();
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (fridgeModelType == FridgeBlock.FridgeModelType.LARGE) {
                        int rowIndex = i % 18;
                        offsetX = 0.7f - (rowIndex % 9) * 0.175f;
                        offsetY = topY - i / 18f * 1.25f;
                        offsetZ = 0.5f - (rowIndex / 9f) * 0.9f;
                    } else {
                        int rowIndex = i % 13;
                        offsetX = 0.7f;
                        float spacing = 0.175f;
                        if (rowIndex / 9 > 0) {
                            offsetX -= 0.2f;
                            spacing *= 2;
                        }
                        offsetX -= (rowIndex % 9) * spacing;
                        offsetY = topY - i / 13f * 1.25f;
                        offsetZ = 0.5f - (rowIndex / 9f) * 0.9f;
                    }
                    RenderUtils.renderItem(itemRenderer, itemStack, offsetX, offsetY, offsetZ, 45f, 0f, 1f, 0f);
                }
            }
            matrixStack.pop();
        }
    }

}
