package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class OvenRenderer extends TileEntityRenderer<OvenTileEntity> {

    private static final Random random = new Random();

    public OvenRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(OvenTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        Direction facing = tileEntity.getFacing();
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);

        // Render the oven door
        matrixStack.push();
        RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
        matrixStack.translate(-0.5f, 0f, -0.5f);
        matrixStack.rotate(new Quaternion(-(float) Math.toDegrees(doorAngle), 0f, 0f, true));
        IBakedModel model = doorAngle < 0.3f && tileEntity.isBurning() ? ModModels.ovenDoorActive : ModModels.ovenDoor;
        dispatcher.getBlockModelRenderer().renderModel(world, model, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.getSolid()), false, random, 0, 0, EmptyModelData.INSTANCE);
        matrixStack.translate(0f, 0f, -1f);
        dispatcher.getBlockModelRenderer().renderModel(world, ModModels.ovenDoorHandle, tileEntity.getBlockState(), tileEntity.getPos().offset(facing), matrixStack, buffer.getBuffer(RenderType.getSolid()), false, random, 0, 0, EmptyModelData.INSTANCE);
        matrixStack.pop();

        // Render the oven tools
        matrixStack.push();
        matrixStack.translate(0f, 1.05, 0f);
        RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
        matrixStack.scale(0.4f, 0.4f, 0.4f);
        ItemStack itemStack = tileEntity.getToolItem(0);
        if (!itemStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(-0.55f, 0f, 0.5f);
            matrixStack.rotate(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
            matrixStack.pop();
        }

        itemStack = tileEntity.getToolItem(1);
        if (!itemStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.55f, 0f, 0.5f);
            matrixStack.rotate(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
            matrixStack.pop();
        }

        itemStack = tileEntity.getToolItem(2);
        if (!itemStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(-0.55f, 0f, -0.5f);
            matrixStack.rotate(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
            matrixStack.pop();
        }

        itemStack = tileEntity.getToolItem(3);
        if (!itemStack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.55f, 0f, -0.5f);
            matrixStack.rotate(new Quaternion(45f, 0f, 0f, true));
            RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
            matrixStack.pop();
        }
        matrixStack.pop();

        // Render the oven content when the door is open
        if (doorAngle > 0f) {
            matrixStack.push();
            matrixStack.translate(0, 0.4, 0);
            RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
            matrixStack.scale(0.3f, 0.3f, 0.3f);
            float offsetX = 0.825f;
            float offsetZ = 0.8f;
            for (int i = 0; i < 9; i++) {
                itemStack = tileEntity.getItemHandler().getStackInSlot(7 + i);
                if (!itemStack.isEmpty()) {
                    matrixStack.push();
                    matrixStack.translate(offsetX, 0f, offsetZ);
                    matrixStack.rotate(new Quaternion(90f, 0f, 0f, true));
                    RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
                    matrixStack.pop();
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
