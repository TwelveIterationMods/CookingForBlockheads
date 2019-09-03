package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.model.FridgeDoorModel;
import net.blay09.mods.cookingforblockheads.client.model.FridgeLargeDoorModel;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.BlockState;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class FridgeRenderer extends TileEntityRenderer<TileFridge> {

    public static IBakedModel modelDoor;
    public static IBakedModel modelDoorLarge;
    public static IBakedModel modelDoorIceUnit;
    public static IBakedModel modelDoorIceUnitLarge;
    public static IBakedModel modelHandle;
    public static IBakedModel modelHandleLarge;

    private final FridgeDoorModel modelFridgeDoor = new FridgeDoorModel();
    private final FridgeLargeDoorModel modelFridgeLargeDoor = new FridgeLargeDoorModel();
    private final ResourceLocation textureFridgeDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/fridge_door.png");
    private final ResourceLocation textureFridgeLargeDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/fridge_large_door.png");

    @Override
    public void render(TileFridge tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (state.getBlock() != ModBlocks.fridge) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        state = state.getBlock().getActualState(state, tileEntity.getWorld(), tileEntity.getPos());
        BlockFridge.FridgeType fridgeType = state.get(BlockFridge.TYPE);
        if (fridgeType == BlockFridge.FridgeType.INVISIBLE) {
            return;
        }

        // Render the oven door
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        float blockAngle = RenderUtils.getFacingAngle(state);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = state.get(BlockFridge.FLIPPED);
        boolean isLarge = fridgeType == BlockFridge.FridgeType.LARGE;
        boolean hasIceUnit = state.get(BlockFridge.ICE_UNIT);
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5f, y, z + 0.5f);
        GlStateManager.rotatef(blockAngle, 0f, 1f, 0f);
        GlStateManager.translatef(-0.5f, 0f, -0.5f);

        float originX = 0.9375f;
        float originZ = 0.0625f;
        if (isFlipped) {
            originX = 1f - originX;
            doorAngle *= -1f;
        }

        GlStateManager.translatef(originX, 0f, originZ);
        GlStateManager.rotatef(-(float) Math.toDegrees(doorAngle), 0f, 1f, 0f);
        GlStateManager.translatef(-originX, 0f, -originZ);

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model;
        if (isLarge) {
            model = hasIceUnit ? modelDoorIceUnitLarge : modelDoorLarge;
        } else {
            model = hasIceUnit ? modelDoorIceUnit : modelDoor;
        }

        IBakedModel handleModel = isLarge ? modelHandleLarge : modelHandle;
        DyeColor fridgeColor = tileEntity.getFridgeColor();
        int color = fridgeColor.getColorValue();
        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(model, 1f, (float) (color >> 16 & 255) / 255f, (float) (color >> 8 & 255) / 255f, (float) (color & 255) / 255f);
        if (isFlipped) {
            GlStateManager.translatef(isLarge ? 0.6875f : 0.625f, 0f, 0f);
        }
        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(handleModel, 1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();

        /*GlStateManager.pushMatrix();
        EnumDyeColor fridgeColor = tileEntity.getFridgeColor();
        int color = fridgeColor.getColorValue();
        GlStateManager.color((float) (color >> 16 & 255) / 255f, (float) (color >> 8 & 255) / 255f, (float) (color & 255) / 255f, 1f);
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);

        boolean isFlipped = state.getValue(BlockFridge.FLIPPED);
        if (fridgeType == BlockFridge.FridgeType.SMALL) {
            bindTexture(textureFridgeDoor);
            modelFridgeDoor.DoorMain.rotateAngleY = doorAngle;
            modelFridgeDoor.DoorHandle.rotateAngleY = doorAngle;
            modelFridgeDoor.DoorMainFlipped.rotateAngleY = -doorAngle;
            modelFridgeDoor.DoorHandleFlipped.rotateAngleY = -doorAngle;
            modelFridgeDoor.render(isFlipped);
            GlStateManager.color(1f, 1f, 1f, 1f);
            modelFridgeDoor.renderNoTint(isFlipped);
        } else if (fridgeType == BlockFridge.FridgeType.LARGE) {
            bindTexture(textureFridgeLargeDoor);
            modelFridgeLargeDoor.DoorMain.rotateAngleY = doorAngle;
            modelFridgeLargeDoor.DoorHandle.rotateAngleY = doorAngle;
            modelFridgeLargeDoor.DoorMainFlipped.rotateAngleY = -doorAngle;
            modelFridgeLargeDoor.DoorHandleFlipped.rotateAngleY = -doorAngle;
            modelFridgeLargeDoor.render(isFlipped);
            GlStateManager.color(1f, 1f, 1f, 1f);
            modelFridgeLargeDoor.renderNoTint(isFlipped);
        }
        GlStateManager.popMatrix();*/

        // Render the fridge content if the door is open
        if (doorAngle != 0f) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            GlStateManager.pushMatrix();
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            GlStateManager.translatef(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
            GlStateManager.scalef(0.3f, 0.3f, 0.3f);
            float topY = fridgeType == BlockFridge.FridgeType.LARGE ? 3.25f : 0.35f;
            IItemHandler itemHandler = tileEntity.getCombinedItemHandler();
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (fridgeType == BlockFridge.FridgeType.LARGE) {
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
