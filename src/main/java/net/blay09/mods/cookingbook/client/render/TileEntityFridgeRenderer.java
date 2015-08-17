package net.blay09.mods.cookingbook.client.render;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.block.TileEntityFridge;
import net.blay09.mods.cookingbook.client.model.ModelFridge;
import net.blay09.mods.cookingbook.client.model.ModelFridgeSmall;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityFridgeRenderer extends TileEntitySpecialRenderer {

    private static final ResourceLocation textureSmall = new ResourceLocation("cookingbook", "textures/entity/ModelFridgeSmall-texture.png");
    private static final ResourceLocation textureBig = new ResourceLocation("cookingbook", "textures/entity/ModelFridge-texture.png");

    private ModelFridge modelBig = new ModelFridge();
    private ModelFridgeSmall modelSmall = new ModelFridgeSmall();
    public static final float[][] fridgeColorTable = new float[][] {{1.0F, 1.0F, 1.0F}, {0.85F, 0.5F, 0.2F}, {0.7F, 0.3F, 0.85F}, {0.4F, 0.6F, 0.85F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.5F, 0.65F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.5F, 0.6F}, {0.5F, 0.25F, 0.7F}, {0.2F, 0.3F, 0.7F}, {0.4F, 0.3F, 0.2F}, {0.4F, 0.5F, 0.2F}, {0.6F, 0.2F, 0.2F}, {0.1F, 0.1F, 0.1F}};

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float delta) {
        int metadata = 0;
        boolean isLargeFridge = false;
        int dye = ((TileEntityFridge) tileEntity).getFridgeColor();
        if(tileEntity.hasWorldObj()) {
            metadata = tileEntity.getBlockMetadata();
            Block above = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord);
            if(above == CookingBook.blockFridge) {
                isLargeFridge = true;
            }
            Block below = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord);
            if(below == CookingBook.blockFridge) {
                return;
            }
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(fridgeColorTable[dye][0], fridgeColorTable[dye][1], fridgeColorTable[dye][2], 1f);
        GL11.glTranslatef((float) x, (float) y - 1f, (float) z);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        float angle = 0;
        switch(ForgeDirection.getOrientation(metadata)) {
            case NORTH:
                angle = -90;
                break;
            case EAST:
                angle = 180;
                break;
            case SOUTH:
                angle = 90;
                break;
            case WEST:
                angle = 0;
                break;
        }
        GL11.glRotatef(angle, 0f, 1f, 0f);
        if(isLargeFridge) {
            GL11.glTranslatef(0, 1f, 0);
            bindTexture(textureBig);
            modelBig.renderAll();
        } else {
            bindTexture(textureSmall);
            modelSmall.renderAll();
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

}
