package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.BlockMilkJar;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.minecraft.block.BlockState;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

public class CowJarRenderer extends MilkJarRenderer {

    private static final ResourceLocation COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");

    private static final CowModel<CowEntity> model = new CowModel<>();
    private static CowEntity entity;

    @Override
    public void render(TileMilkJar tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        super.render(tileEntity, x, y, z, partialTicks, destroyStage, alpha);

        BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (state.getBlock() != ModBlocks.cowJar) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        if (entity == null && tileEntity.hasWorld()) {
            entity = new CowEntity(tileEntity.getWorld());
            entity.setScaleForAge(false);
        }

        bindTexture(COW_TEXTURES);
        if (entity != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5 + (BlockMilkJar.shouldBlockRenderLowered(tileEntity.getWorld(), tileEntity.getPos()) ? -0.05 : 0), z + 0.5);
            GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
            GlStateManager.rotatef(180f, 0f, 0f, 1f);
            GlStateManager.scalef(0.02, 0.02, 0.02);
            model.render(entity, 0f, 0f, 0f, 0f, 0f, 1f);
            GlStateManager.popMatrix();
        }
    }

}
