package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * ModelOven - BlayTheNinth
 * Created using Tabula 4.1.1
 */
public class ModelOven extends ModelBase {
    public ModelRenderer OvenLeft;
    public ModelRenderer OvenDoor;
    public ModelRenderer OvenDoorBurning;
    public ModelRenderer OvenRight;
    public ModelRenderer OvenBack;
    public ModelRenderer OvenBottom;
    public ModelRenderer OvenFront;
    public ModelRenderer OvenTop;
    public ModelRenderer OvenGrid;
    public ModelRenderer OvenDoorHandleKnobRight;
    public ModelRenderer OvenDoorHandleKnobLeft;
    public ModelRenderer OvenDoorHandle;

    public ModelOven() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.OvenDoorBurning = new ModelRenderer(this, 26, 99);
        this.OvenDoorBurning.setRotationPoint(-6.0F, 24.0F, -6.0F);
        this.OvenDoorBurning.addBox(0.0F, -11.0F, -1.0F, 12, 11, 1, 0.0F);
        this.OvenLeft = new ModelRenderer(this, 0, 15);
        this.OvenLeft.setRotationPoint(-7.0F, 10.0F, -7.0F);
        this.OvenLeft.addBox(0.0F, 0.0F, 0.0F, 1, 14, 14, 0.0F);
        this.OvenDoorHandle = new ModelRenderer(this, 26, 86);
        this.OvenDoorHandle.setRotationPoint(0.0F, -11.0F, -3.0F);
        this.OvenDoorHandle.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1, 0.0F);
        this.OvenRight = new ModelRenderer(this, 0, 43);
        this.OvenRight.setRotationPoint(6.0F, 10.0F, -7.0F);
        this.OvenRight.addBox(0.0F, 0.0F, 0.0F, 1, 14, 14, 0.0F);
        this.OvenGrid = new ModelRenderer(this, 0, 114);
        this.OvenGrid.setRotationPoint(-6.5F, 19.0F, -6.0F);
        this.OvenGrid.addBox(0.0F, 0.0F, 0.0F, 13, 1, 12, 0.0F);
        this.OvenDoorHandleKnobLeft = new ModelRenderer(this, 26, 84);
        this.OvenDoorHandleKnobLeft.setRotationPoint(2.0F, -11.0F, -2.0F);
        this.OvenDoorHandleKnobLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.OvenBack = new ModelRenderer(this, 0, 84);
        this.OvenBack.setRotationPoint(-6.0F, 10.0F, 6.0F);
        this.OvenBack.addBox(0.0F, 0.0F, 0.0F, 12, 14, 1, 0.0F);
        this.OvenDoor = new ModelRenderer(this, 0, 99);
        this.OvenDoor.setRotationPoint(-6.0F, 24.0F, -6.0F);
        this.OvenDoor.addBox(0.0F, -11.0F, -1.0F, 12, 11, 1, 0.0F);
        this.OvenBottom = new ModelRenderer(this, 0, 71);
        this.OvenBottom.setRotationPoint(-6.0F, 23.0F, -6.0F);
        this.OvenBottom.addBox(0.0F, 0.0F, 0.0F, 12, 1, 12, 0.0F);
        this.OvenTop = new ModelRenderer(this, 0, 0);
        this.OvenTop.setRotationPoint(-7.0F, 9.0F, -7.0F);
        this.OvenTop.addBox(0.0F, 0.0F, 0.0F, 14, 1, 14, 0.0F);
        this.OvenFront = new ModelRenderer(this, 0, 110);
        this.OvenFront.setRotationPoint(-6.0F, 10.0F, -7.0F);
        this.OvenFront.addBox(0.0F, 0.0F, 0.0F, 12, 3, 1, 0.0F);
        this.OvenDoorHandleKnobRight = new ModelRenderer(this, 26, 84);
        this.OvenDoorHandleKnobRight.setRotationPoint(9.0F, -11.0F, -2.0F);
        this.OvenDoorHandleKnobRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.OvenDoor.addChild(this.OvenDoorHandle);
        this.OvenDoor.addChild(this.OvenDoorHandleKnobLeft);
        this.OvenDoor.addChild(this.OvenDoorHandleKnobRight);
        this.OvenDoorBurning.addChild(this.OvenDoorHandle);
        this.OvenDoorBurning.addChild(this.OvenDoorHandleKnobLeft);
        this.OvenDoorBurning.addChild(this.OvenDoorHandleKnobRight);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.OvenDoor.render(f5);
        this.OvenRight.render(f5);
        this.OvenBack.render(f5);
        this.OvenTop.render(f5);
        this.OvenBottom.render(f5);
        this.OvenFront.render(f5);
        this.OvenLeft.render(f5);
        this.OvenGrid.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderAll(boolean isBurning) {
        float f5 = 0.0625f;
        this.OvenRight.render(f5);
        this.OvenBack.render(f5);
        this.OvenTop.render(f5);
        this.OvenBottom.render(f5);
        this.OvenFront.render(f5);
        this.OvenLeft.render(f5);
        this.OvenGrid.render(f5);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if(isBurning) {
            this.OvenDoorBurning.render(f5);
        } else {
            this.OvenDoor.render(f5);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}
