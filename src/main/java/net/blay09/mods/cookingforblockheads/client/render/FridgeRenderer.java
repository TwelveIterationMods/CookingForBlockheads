package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.model.ModelFridgeDoor;
import net.blay09.mods.cookingforblockheads.client.model.ModelFridgeLargeDoor;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class FridgeRenderer extends TileEntitySpecialRenderer<TileFridge> {

    private final ModelFridgeDoor modelFridgeDoor = new ModelFridgeDoor();
    private final ModelFridgeLargeDoor modelFridgeLargeDoor = new ModelFridgeLargeDoor();
    private final ResourceLocation textureFridgeDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/FridgeDoor.png");
    private final ResourceLocation textureFridgeLargeDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/FridgeLargeDoor.png");

    @Override
    public void renderTileEntityAt(TileFridge tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(state.getBlock() != ModBlocks.fridge) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }
        //noinspection deprecation
        state = state.getBlock().getActualState(state, tileEntity.getWorld(), tileEntity.getPos());
        BlockFridge.FridgeType fridgeType = state.getValue(BlockFridge.TYPE);
        if(fridgeType == BlockFridge.FridgeType.INVISIBLE) {
            return;
        }
        GlStateManager.pushMatrix();
        EnumDyeColor fridgeColor = tileEntity.getFridgeColor();
        int color = fridgeColor.getMapColor().colorValue;
        GlStateManager.color((float) (color >> 16 & 255) / 255f, (float) (color >> 8 & 255) / 255f, (float) (color & 255) / 255f, 1f);
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        boolean isFlipped = state.getValue(BlockFridge.FLIPPED);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        if(fridgeType == BlockFridge.FridgeType.SMALL) {
            bindTexture(textureFridgeDoor);
            modelFridgeDoor.DoorMain.rotateAngleY = doorAngle;
            modelFridgeDoor.DoorHandle.rotateAngleY = doorAngle;
            modelFridgeDoor.DoorMainFlipped.rotateAngleY = -doorAngle;
            modelFridgeDoor.DoorHandleFlipped.rotateAngleY = -doorAngle;
            modelFridgeDoor.render(isFlipped);
            GlStateManager.color(1f, 1f, 1f, 1f);
            modelFridgeDoor.renderNoTint(isFlipped);
        } else if(fridgeType == BlockFridge.FridgeType.LARGE) {
            bindTexture(textureFridgeLargeDoor);
            modelFridgeLargeDoor.DoorMain.rotateAngleY = doorAngle;
            modelFridgeLargeDoor.DoorHandle.rotateAngleY = doorAngle;
            modelFridgeLargeDoor.DoorMainFlipped.rotateAngleY = -doorAngle;
            modelFridgeLargeDoor.DoorHandleFlipped.rotateAngleY = -doorAngle;
            modelFridgeLargeDoor.render(isFlipped);
            GlStateManager.color(1f, 1f, 1f, 1f);
            modelFridgeLargeDoor.renderNoTint(isFlipped);
        }
        GlStateManager.popMatrix();

        if(doorAngle > 0f) {
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
            GlStateManager.scale(0.3f, 0.3f, 0.3f);
            float topY = fridgeType == BlockFridge.FridgeType.LARGE ? 3.25f : 0.35f;
            IItemHandler itemHandler = tileEntity.getCombinedItemHandler();
            for(int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if(!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if(fridgeType == BlockFridge.FridgeType.LARGE) {
                        int rowIndex = i % 18;
                        offsetX = 0.7f - (rowIndex % 9) * 0.175f;
                        offsetY = topY - i / 18 * 1.25f;
                        offsetZ = 0.5f - (rowIndex / 9) * 0.9f;
                    } else {
                        int rowIndex = i % 13;
                        offsetX = 0.7f;
                        float spacing = 0.175f;
                        if(rowIndex / 9 > 0) {
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
