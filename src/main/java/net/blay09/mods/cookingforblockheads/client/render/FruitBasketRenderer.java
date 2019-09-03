package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.FruitBasketTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class FruitBasketRenderer extends TileEntitySpecialRenderer<FruitBasketTileEntity> {

    @Override
    public void render(FruitBasketTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (state.getBlock() != ModBlocks.fruitBasket) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        GlStateManager.rotate(RenderUtils.getFacingAngle(state) + 180f, 0f, 1f, 0f);
        GlStateManager.scale(0.25f, 0.25f, 0.25f);
        int itemsPerRow = 7;
        for (int i = 0; i < tileEntity.getItemHandler().getSlots(); i++) {
            ItemStack itemStack = tileEntity.getItemHandler().getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                int rowIndex = i % itemsPerRow;
                int colIndex = i / itemsPerRow;
                float antiZFight = ((rowIndex % 2 != 0) ? 0.1f : 0f) + i * 0.01f;
                float curX = -0.75f + rowIndex * 0.25f + ((colIndex == 3) ? 0.15f : 0f);
                float curY = -1.35f;
                if (BlockKitchen.shouldBlockRenderLowered(tileEntity.getWorld(), tileEntity.getPos())) {
                    curY -= 0.2f;
                }

                float curZ = -0.85f + colIndex * 0.35f + antiZFight;
                RenderUtils.renderItem(itemRenderer, itemStack, curX, curY, curZ, -25f, 1f, 0f, 0f);
            }
        }

        GlStateManager.popMatrix();
    }

}
