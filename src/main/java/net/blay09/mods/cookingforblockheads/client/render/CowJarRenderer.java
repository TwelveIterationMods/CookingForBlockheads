package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.MilkJarBlock;
import net.blay09.mods.cookingforblockheads.tile.MilkJarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CowJarRenderer extends MilkJarRenderer {

    private static final RenderMaterial MATERIAL = new RenderMaterial(Atlases.SIGN_ATLAS, new ResourceLocation(CookingForBlockheads.MOD_ID, "entity/cow/cow"));

    private static final CowModel<CowEntity> model = new CowModel<>();
    private static CowEntity entity;

    public CowJarRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MilkJarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world == null) {
            return;
        }

        super.render(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);

        if (entity == null) {
            entity = new CowEntity(EntityType.COW, world);
        }

        float shrinkage = 0.2f;
        matrixStack.push();
        RenderUtils.applyBlockAngle(matrixStack, tileEntity.getBlockState(), 0f);
        matrixStack.translate(0, 0 + (MilkJarBlock.shouldBlockRenderLowered(world, tileEntity.getPos()) ? -0.05 : 0), 0);
        matrixStack.scale(shrinkage, shrinkage, shrinkage);

        Minecraft.getInstance().getRenderManager().renderEntityStatic(entity, 0, 0, 0, 0f, 0f, matrixStack, buffer, combinedLight);
        matrixStack.pop();
    }

}
