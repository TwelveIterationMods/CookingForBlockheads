package net.blay09.mods.cookingbook.client.render;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.block.TileEntityFridge;
import net.blay09.mods.cookingbook.client.model.ModelFridge;
import net.blay09.mods.cookingbook.client.model.ModelSmallFridge;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityFridgeRenderer extends TileEntitySpecialRenderer {

    private static final ResourceLocation textureSmall = new ResourceLocation("cookingbook", "textures/entity/ModelSmallFridge.png");
    private static final ResourceLocation textureBig = new ResourceLocation("cookingbook", "textures/entity/ModelFridge.png");

    private ModelFridge modelBig = new ModelFridge();
    private ModelSmallFridge modelSmall = new ModelSmallFridge();
    public static final float[][] fridgeColorTable = new float[][] {{1.0F, 1.0F, 1.0F}, {0.85F, 0.5F, 0.2F}, {0.7F, 0.3F, 0.85F}, {0.4F, 0.6F, 0.85F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.5F, 0.65F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.5F, 0.6F}, {0.5F, 0.25F, 0.7F}, {0.2F, 0.3F, 0.7F}, {0.4F, 0.3F, 0.2F}, {0.4F, 0.5F, 0.2F}, {0.6F, 0.2F, 0.2F}, {0.1F, 0.1F, 0.1F}};

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float delta) {
        int metadata = 0;
        boolean isLargeFridge = false;
        TileEntityFridge tileEntityFridge = (TileEntityFridge) tileEntity;
        int dye = tileEntityFridge.getFridgeColor();
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
        boolean oldRescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        if(oldRescaleNormal) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glTranslatef((float) x, (float) y + 1f, (float) z);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
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
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glRotatef(angle, 0f, 1f, 0f);
        GL11.glRotatef(180f, 0f, 0f, 1f);
        float doorAngle = tileEntityFridge.getPrevDoorAngle() + (tileEntityFridge.getDoorAngle() - tileEntityFridge.getPrevDoorAngle()) * delta;
        doorAngle = 1.0f - doorAngle;
        doorAngle = 1.0f - doorAngle * doorAngle * doorAngle;
        if(isLargeFridge) {
            TileEntityFridge neighbourFridge = tileEntityFridge.getNeighbourFridge();
            if(neighbourFridge != null) {
                float neighbourDoorAngle = neighbourFridge.getPrevDoorAngle() + (neighbourFridge.getDoorAngle() - neighbourFridge.getPrevDoorAngle()) * delta;
                if(neighbourDoorAngle > doorAngle) {
                    doorAngle = neighbourDoorAngle;
                }
            }
            modelBig.Door.rotateAngleY = (float) ((Math.PI / 2f) * doorAngle);
            modelBig.DoorHandle.rotateAngleY = (float) ((Math.PI / 2f) * doorAngle);
            bindTexture(textureBig);
            modelBig.renderUncolored();
            GL11.glColor4f(fridgeColorTable[dye][0], fridgeColorTable[dye][1], fridgeColorTable[dye][2], 1f);
            modelBig.renderColored();
            if(doorAngle > 0f) {
                GL11.glRotatef(180f, 0f, 0f, -1f);
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                for (int i = 0; i < tileEntityFridge.getSizeInventory(); i++) {
                    ItemStack itemStack = tileEntityFridge.getStackInSlot(i);
                    if(itemStack != null) {
                        int shelfCapacity = tileEntityFridge.getSizeInventory() / 3;
                        int relSlot = i % shelfCapacity;
                        float itemX = Math.min(8f / 9f, (float) (relSlot % 9) / 9f);
                        float itemY;
                        if(i >= shelfCapacity * 2) {
                            itemY = -0.75f;
                        } else if(i >= shelfCapacity) {
                            itemY = 0.125f;
                        } else {
                            itemY = -1.625f;
                        }
                        float itemZ = (relSlot < 9) ? 0f : -0.5f;
                        if(relSlot % 2 == 0) {
                            itemZ -= 0.1f;
                        }
                        tileEntityFridge.getRenderItem().setEntityItemStack(itemStack);
                        RenderManager.instance.renderEntityWithPosYaw(tileEntityFridge.getRenderItem(), 0.45f - itemX, itemY, 0.5f + itemZ, 0f, 0f);
                    }
                }
            }
        } else {
            modelSmall.Door.rotateAngleY = (float) ((Math.PI / 2f) * doorAngle);
            modelSmall.DoorHandle.rotateAngleY = (float) ((Math.PI / 2f) * doorAngle);
            bindTexture(textureSmall);
            modelSmall.renderUncolored();
            GL11.glColor4f(fridgeColorTable[dye][0], fridgeColorTable[dye][1], fridgeColorTable[dye][2], 1f);
            modelSmall.renderColored();
            if(doorAngle > 0f) {
                GL11.glRotatef(180f, 0f, 0f, -1f);
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                for (int i = 0; i < tileEntityFridge.getSizeInventory(); i++) {
                    ItemStack itemStack = tileEntityFridge.getStackInSlot(i);
                    if(itemStack != null) {
                        int relSlot = i;
                        if(i > tileEntityFridge.getSizeInventory() / 2) {
                            relSlot -= tileEntityFridge.getSizeInventory() / 2;
                        }
                        float itemX = (relSlot > 8) ? Math.min(4f/5f, (relSlot-9) / 5f) : Math.min(8f/9f, (float) relSlot / 9f);
                        float itemY = (i > tileEntityFridge.getSizeInventory() / 2) ? -0.7f : 0.01f;
                        float itemZ = (relSlot > 8) ? -0.8f : -0.1f;
                        if(relSlot % 2 == 0) {
                            itemZ -= 0.1f;
                        }
                        tileEntityFridge.getRenderItem().setEntityItemStack(itemStack);
                        RenderManager.instance.renderEntityWithPosYaw(tileEntityFridge.getRenderItem(), 0.45f - itemX, -2f + itemY, 0.5f + itemZ, 0f, 0f);
                    }
                }
            }
        }
        if(!oldRescaleNormal) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

}
