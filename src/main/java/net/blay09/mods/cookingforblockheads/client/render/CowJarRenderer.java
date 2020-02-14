package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.MilkJarBlock;
import net.blay09.mods.cookingforblockheads.tile.MilkJarTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;

public class CowJarRenderer extends MilkJarRenderer {

    private static final ResourceLocation COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");

    private static final CowModel<CowEntity> model = new CowModel<>();
    private static CowEntity entity;

    public CowJarRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MilkJarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);

        BlockState state = tileEntity.getBlockState();

        if (entity == null && tileEntity.hasWorld()) {
            entity = new CowEntity(EntityType.COW, tileEntity.getWorld());
        }

        // TODO bindTexture(COW_TEXTURES);
        if (entity != null) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.5 + (MilkJarBlock.shouldBlockRenderLowered(tileEntity.getWorld(), tileEntity.getPos()) ? -0.05 : 0), 0.5);
            matrixStack.rotate(new Quaternion(0f, RenderUtils.getFacingAngle(state.get(BlockKitchen.FACING)), 0f, true));
            matrixStack.rotate(new Quaternion(0f, 0f, 180f, true));
            matrixStack.scale(0.02f, 0.02f, 0.02f);
            // TODO model.render(entity, 0f, 0f, 0f, 0f, 0f, 1f);
            matrixStack.pop();
        }
    }

}
