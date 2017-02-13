package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.model.ModelOvenDoor;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class OvenRenderer extends TileEntitySpecialRenderer<TileOven> {

    private final ModelOvenDoor modelOvenDoor = new ModelOvenDoor();
    private final ResourceLocation textureOvenDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/oven_door.png");
    private final ResourceLocation textureOvenDoorActive = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/oven_door_active.png");

    @Override
    public void renderTileEntityAt(TileOven tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(state.getBlock() != ModBlocks.oven) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + 0.5, y + 1.05, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
        ItemStack itemStack = tileEntity.getToolItem(0);
        if(!itemStack.isEmpty()) {
            RenderUtils.renderItem(itemRenderer, itemStack, -0.55f, 0f, 0.5f, 45f, 1f, 0f, 0f);
        }
        itemStack = tileEntity.getToolItem(1);
        if(!itemStack.isEmpty()) {
            RenderUtils.renderItem(itemRenderer, itemStack, 0.55f, 0f, 0.5f, 45f, 1f, 0f, 0f);
        }
        itemStack = tileEntity.getToolItem(2);
        if(!itemStack.isEmpty()) {
            RenderUtils.renderItem(itemRenderer, itemStack, -0.55f, 0f, -0.5f, 45f, 1f, 0f, 0f);
        }
        itemStack = tileEntity.getToolItem(3);
        if(!itemStack.isEmpty()) {
            RenderUtils.renderItem(itemRenderer, itemStack, 0.55f, 0f, -0.5f, 45f, 1f, 0f, 0f);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
//        EnumDyeColor fridgeColor = tileEntity.getOvenColor();
//        int color = fridgeColor.getMapColor().colorValue;
//        GlStateManager.color((float) (color >> 16 & 255) / 255f, (float) (color >> 8 & 255) / 255f, (float) (color & 255) / 255f, 1f);
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        bindTexture(doorAngle < 0.3f && tileEntity.isBurning() ? textureOvenDoorActive : textureOvenDoor);
        modelOvenDoor.DoorMain.rotateAngleX = doorAngle;
        modelOvenDoor.DoorHandle.rotateAngleX = doorAngle;
        modelOvenDoor.DoorBopLeft.rotateAngleX = doorAngle;
        modelOvenDoor.DoorBopRight.rotateAngleX = doorAngle;
        modelOvenDoor.render();
//        GlStateManager.color(1f, 1f, 1f, 1f);
        modelOvenDoor.renderNoTint();
        GlStateManager.popMatrix();

        if(doorAngle > 0f) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.35, z + 0.5);
            GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
            GlStateManager.scale(0.3f, 0.3f, 0.3f);
            float offsetX = 0.825f;
            float offsetZ = 0.8f;
            for(int i = 0; i < 9; i++) {
                itemStack = tileEntity.getItemHandler().getStackInSlot(7 + i);
                if(!itemStack.isEmpty()) {
                    RenderUtils.renderItem(itemRenderer, itemStack, offsetX, 0f, offsetZ, 90f, 1f, 0f, 0f);
                }
                offsetX -= 0.8f;
                if(offsetX < -0.8f) {
                    offsetX = 0.825f;
                    offsetZ -= 0.8f;
                }
            }
            GlStateManager.popMatrix();
        }
    }

}
