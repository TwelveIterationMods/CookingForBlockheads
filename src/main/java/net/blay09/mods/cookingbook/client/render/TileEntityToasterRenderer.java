package net.blay09.mods.cookingbook.client.render;

import net.blay09.mods.cookingbook.CookingForBlockheads;
import net.blay09.mods.cookingbook.block.TileEntityToaster;
import net.blay09.mods.cookingbook.client.model.ModelToaster;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityToasterRenderer extends TileEntitySpecialRenderer {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/entity/ModelToaster.png");

    private ModelToaster model = new ModelToaster();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        int metadata = 0;
        if(tileEntity.hasWorldObj()) {
            metadata = tileEntity.getBlockMetadata();
        } else {
            GL11.glScalef(2f, 2f, 2f);
            GL11.glTranslatef(0, 0.25f, 0);
        }
        TileEntityToaster tileEntityToaster = (TileEntityToaster) tileEntity;
        boolean oldRescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        if(oldRescaleNormal) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glTranslatef(0.5f, 0.065f, 0.5f);
        float angle;
        switch(ForgeDirection.getOrientation(metadata)) {
            case NORTH:
                angle = 0;
                break;
            case EAST:
                angle = -90;
                break;
            case SOUTH:
                angle = 180;
                break;
            case WEST:
                angle = 90;
                break;
            default:
                angle = -90;
        }
        GL11.glRotatef(angle, 0f, 1f, 0f);
        GL11.glRotatef(180f, 0f, 0f, 1f);
        bindTexture(texture);
        model.renderAll();
        GL11.glRotatef(180f, 0f, 0f, 1f);

        if(tileEntityToaster.isActive()) {
            model.ToasterButtonThingy.offsetY = 0.17f;
        } else {
            model.ToasterButtonThingy.offsetY = 0;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0575f, 0.025f - (tileEntityToaster.isActive() ? 0.08f : 0f), -0.05f);
        GL11.glScalef(0.75f, 0.75f, 0.75f);
        GL11.glRotatef(90f, 0f, 1f, 0f);
        if(tileEntityToaster.getStackInSlot(0) != null) {
            RenderManager.instance.renderEntityWithPosYaw(tileEntityToaster.getRenderItem(0), 0d, 0d, 0d, 0f, 0f);
        }
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(-0.0625f, 0.025f - (tileEntityToaster.isActive() ? 0.08f : 0f), -0.05f);
        GL11.glScalef(0.75f, 0.75f, 0.75f);
        GL11.glRotatef(90f, 0f, 1f, 0f);
        if(tileEntityToaster.getStackInSlot(1) != null) {
            RenderManager.instance.renderEntityWithPosYaw(tileEntityToaster.getRenderItem(1), 0, 0, 0, 0f, 0f);
        }
        GL11.glPopMatrix();
        if(!oldRescaleNormal) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

}
