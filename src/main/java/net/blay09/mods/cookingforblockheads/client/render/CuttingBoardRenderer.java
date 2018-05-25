package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileCuttingBoard;
import net.blay09.mods.cookingforblockheads.tile.TileFruitBasket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class CuttingBoardRenderer extends TileEntitySpecialRenderer<TileCuttingBoard> {

    @Override
    public void render(TileCuttingBoard tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (state.getBlock() != ModBlocks.fruitBasket) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
        GlStateManager.scale(0.25f, 0.25f, 0.25f);


        GlStateManager.popMatrix();
    }

}
