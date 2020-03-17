package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.FridgeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.IItemHandler;

public class FridgeRenderer extends TileEntityRenderer<FridgeTileEntity> {

    public FridgeRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(FridgeTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        FridgeBlock.FridgeModelType fridgeModelType = state.get(FridgeBlock.MODEL_TYPE);
        if (fridgeModelType == FridgeBlock.FridgeModelType.LARGE_UPPER) {
            return;
        }

        // Render the oven door
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        renderDoor(world, tileEntity, matrixStack, buffer, state, fridgeModelType, doorAngle);

        // Render the fridge content if the door is open
        if (doorAngle > 0f) {
            matrixStack.push();
            matrixStack.translate(0, 0.5, 0);
            RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
            matrixStack.scale(0.3f, 0.3f, 0.3f);
            float topY = fridgeModelType == FridgeBlock.FridgeModelType.LARGE_LOWER ? 3.25f : 0.45f;
            IItemHandler itemHandler = tileEntity.getCombinedItemHandler();
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (fridgeModelType == FridgeBlock.FridgeModelType.LARGE_LOWER) {
                        int rowIndex = i % 18;
                        offsetX = 0.7f - (rowIndex % 9) * 0.175f;
                        offsetY = topY - (int) (i / 18f) * 1.25f;
                        offsetZ = 0.5f - (int) (rowIndex / 9f) * 0.9f;
                    } else {
                        int rowIndex = i % 13;
                        offsetX = 0.7f;
                        float spacing = 0.175f;
                        if (rowIndex / 9 > 0) {
                            offsetX -= 0.2f;
                            spacing *= 2;
                        }
                        offsetX -= (rowIndex % 9) * spacing;
                        offsetY = topY - (int) (i / 14f) * 1.25f;
                        offsetZ = 0.5f - (int) (rowIndex / 9f) * 0.9f;
                    }
                    matrixStack.push();
                    matrixStack.translate(offsetX, offsetY, offsetZ);
                    matrixStack.rotate(new Quaternion(0f, 45f, 0f, true));
                    RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
                    matrixStack.pop();
                }
            }
            matrixStack.pop();
        }
    }

    private void renderDoor(World world, FridgeTileEntity tileEntity, MatrixStack matrixStack, IRenderTypeBuffer buffer, BlockState state, FridgeBlock.FridgeModelType fridgeModelType, float doorAngle) {
        boolean isFlipped = state.get(FridgeBlock.FLIPPED);
        boolean isLarge = fridgeModelType == FridgeBlock.FridgeModelType.LARGE_LOWER;
        matrixStack.push();
        RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
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

        IBakedModel lowerModel;
        IBakedModel upperModel = null;
        if (isLarge) {
            lowerModel = isFlipped ? ModModels.fridgeDoorLargeLowerFlipped : ModModels.fridgeDoorLargeLower;
            upperModel = isFlipped ? ModModels.fridgeDoorLargeUpperFlipped : ModModels.fridgeDoorLargeUpper;
        } else {
            lowerModel = isFlipped ? ModModels.fridgeDoorFlipped : ModModels.fridgeDoor;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        dispatcher.getBlockModelRenderer().renderModel(world, lowerModel, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.solid()), false, world.rand, 0, 0, EmptyModelData.INSTANCE);
        if (upperModel != null) {
            matrixStack.translate(0, 1, 0);
            dispatcher.getBlockModelRenderer().renderModel(world, upperModel, tileEntity.getBlockState(), tileEntity.getPos().up(), matrixStack, buffer.getBuffer(RenderType.solid()), false, world.rand, 0, 0, EmptyModelData.INSTANCE);
        }

        matrixStack.pop();
    }

}
