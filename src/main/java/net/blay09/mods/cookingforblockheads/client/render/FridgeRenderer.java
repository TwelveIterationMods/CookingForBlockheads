package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.client.model.FridgeDoorModel;
import net.blay09.mods.cookingforblockheads.client.model.FridgeLargeDoorModel;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class FridgeRenderer extends TileEntityRenderer<TileFridge> {

    private final FridgeDoorModel modelFridgeDoor = new FridgeDoorModel();
    private final FridgeLargeDoorModel modelFridgeLargeDoor = new FridgeLargeDoorModel();
    private final ResourceLocation textureFridgeDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/fridge_door.png");
    private final ResourceLocation textureFridgeLargeDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/fridge_large_door.png");

    @Override
    public void render(TileFridge tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        IBakedModel modelDoor = ModModels.milkJarLiquid; // TODO fixme
        IBakedModel modelDoorLarge = ModModels.milkJarLiquid; // TODO fixme
        IBakedModel modelDoorIceUnit = ModModels.milkJarLiquid; // TODO fixme
        IBakedModel modelDoorIceUnitLarge = ModModels.milkJarLiquid; // TODO fixme
        IBakedModel modelHandle = ModModels.milkJarLiquid; // TODO fixme
        IBakedModel modelHandleLarge = ModModels.milkJarLiquid; // TODO fixme

        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        FridgeBlock.FridgeType fridgeType = state.get(FridgeBlock.TYPE);
        if (fridgeType == FridgeBlock.FridgeType.INVISIBLE) {
            return;
        }

        // Render the oven door
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        float blockAngle = RenderUtils.getFacingAngle(state);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = state.get(FridgeBlock.FLIPPED);
        boolean isLarge = fridgeType == FridgeBlock.FridgeType.LARGE;
        boolean hasIceUnit = state.get(FridgeBlock.ICE_UNIT);
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

        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model;
        if (isLarge) {
            model = hasIceUnit ? modelDoorIceUnitLarge : modelDoorLarge;
        } else {
            model = hasIceUnit ? modelDoorIceUnit : modelDoor;
        }

        IBakedModel handleModel = isLarge ? modelHandleLarge : modelHandle;
        DyeColor fridgeColor = tileEntity.getFridgeColor();
        int color = fridgeColor.getFireworkColor();
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

        boolean isFlipped = state.getValue(FridgeBlock.FLIPPED);
        if (fridgeType == FridgeBlock.FridgeType.SMALL) {
            bindTexture(textureFridgeDoor);
            modelFridgeDoor.DoorMain.rotateAngleY = doorAngle;
            modelFridgeDoor.DoorHandle.rotateAngleY = doorAngle;
            modelFridgeDoor.DoorMainFlipped.rotateAngleY = -doorAngle;
            modelFridgeDoor.DoorHandleFlipped.rotateAngleY = -doorAngle;
            modelFridgeDoor.render(isFlipped);
            GlStateManager.color(1f, 1f, 1f, 1f);
            modelFridgeDoor.renderNoTint(isFlipped);
        } else if (fridgeType == FridgeBlock.FridgeType.LARGE) {
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
            GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
            GlStateManager.scalef(0.3f, 0.3f, 0.3f);
            float topY = fridgeType == FridgeBlock.FridgeType.LARGE ? 3.25f : 0.35f;
            IItemHandler itemHandler = tileEntity.getCombinedItemHandler();
            for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    if (fridgeType == FridgeBlock.FridgeType.LARGE) {
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
