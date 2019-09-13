package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.cookingforblockheads.block.MilkJarBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;

public class CowJarRenderer extends MilkJarRenderer {

    private static final ResourceLocation COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");

    private static final CowModel<CowEntity> model = new CowModel<>();
    private static CowEntity entity;

    @Override
    public void render(TileMilkJar tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!tileEntity.hasWorld()) {
            return;
        }

        super.render(tileEntity, x, y, z, partialTicks, destroyStage);

        BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (state.getBlock() != ModBlocks.cowJar) { // I don't know. But it seems for some reason the renderer gets called for minecraft:air in certain cases.
            return;
        }

        if (entity == null && tileEntity.hasWorld()) {
            entity = new CowEntity(EntityType.COW, tileEntity.getWorld());
        }

        bindTexture(COW_TEXTURES);
        if (entity != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 0.5 + (MilkJarBlock.shouldBlockRenderLowered(tileEntity.getWorld(), tileEntity.getPos()) ? -0.05 : 0), z + 0.5);
            GlStateManager.rotatef(RenderUtils.getFacingAngle(state), 0f, 1f, 0f);
            GlStateManager.rotatef(180f, 0f, 0f, 1f);
            GlStateManager.scaled(0.02, 0.02, 0.02);
            model.render(entity, 0f, 0f, 0f, 0f, 0f, 1f);
            GlStateManager.popMatrix();
        }
    }

}
