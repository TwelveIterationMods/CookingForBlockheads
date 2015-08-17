package net.blay09.mods.cookingbook.client.render;

import net.blay09.mods.cookingbook.client.model.ModelToolRack;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityToolRackRenderer extends TileEntitySpecialRenderer {

    private static final ResourceLocation texture = new ResourceLocation("cookingbook", "textures/entity/ModelToolRack-texture.png");

    private ModelToolRack model = new ModelToolRack();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float delta) {
        int metadata = 0;
        if(tileEntity.hasWorldObj()) {
            metadata = tileEntity.getBlockMetadata();
        }

        GL11.glPushMatrix();
        boolean oldRescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        if(oldRescaleNormal) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslatef((float) x, (float) y - 0.4f, (float) z);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        float angle;
        switch(ForgeDirection.getOrientation(metadata)) {
            case NORTH:
                angle = -90;
                break;
            case EAST:
                angle = -90;
                break;
            case SOUTH:
                angle = 90;
                break;
            case WEST:
                angle = 0;
                break;
            default:
                angle = 180;
        }
        GL11.glRotatef(angle, 0f, 1f, 0f);
        bindTexture(texture);
        model.renderAll();
        if(!oldRescaleNormal) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

}
