package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.model.ModelCounterDoor;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class CounterRenderer extends TileEntitySpecialRenderer<TileCounter> {

    private final ModelCounterDoor modelCounterDoor = new ModelCounterDoor();
    private final ResourceLocation textureCounterDoor = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/counter_door.png");

    @Override
    public void renderTileEntityAt(TileCounter tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(true || state.getBlock() != ModBlocks.counter) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        boolean isFlipped = state.getValue(BlockFridge.FLIPPED);
        float doorAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        bindTexture(textureCounterDoor);
        modelCounterDoor.DoorMain.rotateAngleY = doorAngle;
        modelCounterDoor.DoorHandle.rotateAngleY = doorAngle;
        modelCounterDoor.DoorMainFlipped.rotateAngleY = -doorAngle;
        modelCounterDoor.DoorHandleFlipped.rotateAngleY = -doorAngle;
        modelCounterDoor.render(isFlipped);
        GlStateManager.color(1f, 1f, 1f, 1f);
        modelCounterDoor.renderNoTint(isFlipped);
        GlStateManager.popMatrix();

        if(doorAngle > 0f) {
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.rotate(RenderUtils.getFacingAngle(tileEntity), 0f, 1f, 0f);
            GlStateManager.scale(0.3f, 0.3f, 0.3f);
            float topY = 0.25f;
            IItemHandler itemHandler = tileEntity.getItemHandler();
            for(int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if(!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
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
                    RenderUtils.renderItem(itemRenderer, itemStack, offsetX, offsetY, offsetZ, 45f, 0f, 1f, 0f);
                }
            }
            GlStateManager.popMatrix();
        }
    }

}
