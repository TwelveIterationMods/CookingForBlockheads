package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelOven - BlayTheNinth
 * Created using Tabula 4.1.1
 */
public class ModelOven extends ModelBase {
    public ModelRenderer Oven;
    public ModelRenderer OvenBurning;
    public ModelRenderer OvenDoorHandle;
    public ModelRenderer OvenDoorHandleKnobLeft;
    public ModelRenderer OvenDoorHandleKnobRight;

    public ModelOven() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.OvenDoorHandleKnobLeft = new ModelRenderer(this, 0, 0);
        this.OvenDoorHandleKnobLeft.setRotationPoint(-5.0F, 12.0F, -8.0F);
        this.OvenDoorHandleKnobLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.OvenDoorHandleKnobRight = new ModelRenderer(this, 0, 0);
        this.OvenDoorHandleKnobRight.setRotationPoint(4.0F, 12.0F, -7.9F);
        this.OvenDoorHandleKnobRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.OvenDoorHandle = new ModelRenderer(this, 0, 32);
        this.OvenDoorHandle.setRotationPoint(-6.0F, 12.0F, -9.0F);
        this.OvenDoorHandle.addBox(0.0F, 0.0F, 0.0F, 12, 1, 1, 0.0F);
        this.Oven = new ModelRenderer(this, 0, 0);
        this.Oven.setRotationPoint(-7.0F, 9.0F, -7.0F);
        this.Oven.addBox(0.0F, 0.0F, 0.0F, 14, 15, 14, 0.0F);
        this.OvenBurning = new ModelRenderer(this, 0, 34);
        this.OvenBurning.setRotationPoint(-7.0F, 9.0F, -7.0F);
        this.OvenBurning.addBox(0.0F, 0.0F, 0.0F, 14, 15, 14, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.OvenDoorHandleKnobLeft.render(f5);
        this.OvenDoorHandleKnobRight.render(f5);
        this.OvenDoorHandle.render(f5);
        this.Oven.render(f5);
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
        if(isBurning) {
            this.OvenBurning.render(f5);
        } else {
            this.Oven.render(f5);
        }
        this.OvenDoorHandleKnobLeft.render(f5);
        this.OvenDoorHandleKnobRight.render(f5);
        this.OvenDoorHandle.render(f5);
    }
}
