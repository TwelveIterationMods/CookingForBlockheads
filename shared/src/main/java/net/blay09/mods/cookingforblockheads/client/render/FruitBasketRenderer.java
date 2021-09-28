package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.tile.FruitBasketTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;

public class FruitBasketRenderer extends TileEntityRenderer<FruitBasketTileEntity> {

    public FruitBasketRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(FruitBasketTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        matrixStack.push();
        matrixStack.translate(0, 0.5, 0);
        RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState());
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
                if (BlockKitchen.shouldBlockRenderLowered(world, tileEntity.getPos())) {
                    curY -= 0.2f;
                }

                float curZ = -0.75f + colIndex * 0.35f + antiZFight;
                matrixStack.push();
                matrixStack.translate(curX, curY, curZ);
                matrixStack.rotate(new Quaternion(25f, 0f, 0f, true));
                RenderUtils.renderItem(itemStack, combinedLight, matrixStack, buffer);
                matrixStack.pop();
            }
        }

        matrixStack.pop();
    }

}
