package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.FruitBasketTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class FruitBasketRenderer extends TileEntityRenderer<FruitBasketTileEntity> {

    public FruitBasketRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(FruitBasketTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        BlockState state = tileEntity.getBlockState();
        if (state.getBlock() != ModBlocks.fruitBasket) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.rotate(new Quaternion(0f, RenderUtils.getFacingAngle(state) + 180f, 0f, true));
        matrixStack.scale(0.25f, 0.25f, 0.25f);
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

        matrixStack.pop();
    }

}
