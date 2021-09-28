package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.MilkJarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class MilkJarRenderer extends TileEntityRenderer<MilkJarTileEntity> {

    private static final Random random = new Random();

    public MilkJarRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MilkJarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        float milkAmount = tileEntity.getMilkAmount();
        if (milkAmount > 0) {
            matrixStack.push();
            matrixStack.translate(0, (BlockKitchen.shouldBlockRenderLowered(world, tileEntity.getPos()) ? -0.05 : 0), 0);
            matrixStack.scale(1f, milkAmount / tileEntity.getMilkCapacity(), 1f);
            dispatcher.getBlockModelRenderer().renderModel(world, ModModels.milkJarLiquid, tileEntity.getBlockState(), tileEntity.getPos(), matrixStack, buffer.getBuffer(RenderType.getSolid()), false, random, 0, 0, EmptyModelData.INSTANCE);
            matrixStack.pop();
        }
    }

}
