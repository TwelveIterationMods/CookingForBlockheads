package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.SpiceRackTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class SpiceRackRenderer extends TileEntityRenderer<SpiceRackTileEntity> {

    @Override
    public void render(SpiceRackTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        if (state.getBlock() != ModBlocks.spiceRack) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        GlStateManager.translated(x + 0.5, y + 0.6, z + 0.5);
        GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
        GlStateManager.translated(0, 0, 0.4);
        GlStateManager.rotatef(90f, 0f, 1f, 0f);
        GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < tileEntity.getItemHandler().getSlots(); i++) {
            ItemStack itemStack = tileEntity.getItemHandler().getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                RenderUtils.renderItem(itemRenderer, itemStack, 0.15f, 0.35f, 0.8f - i * 0.2f, -30f, 0f, 1f, 0f);
            }
        }

        GlStateManager.popMatrix();
    }

}
