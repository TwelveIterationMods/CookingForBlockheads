package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelToolRack - BlayTheNinth
 * Created using Tabula 4.1.1
 */
public class ModelToolRack extends ModelBase {
    public ModelRenderer woodenBeam;
    public ModelRenderer hookLeft;
    public ModelRenderer hookRight;

    public ModelToolRack() {
        this.textureWidth = 64;
        this.textureHeight = 8;
        this.hookLeft = new ModelRenderer(this, 0, 4);
        this.hookLeft.setRotationPoint(-5.0F, 12.0F, 6.0F);
        this.hookLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.woodenBeam = new ModelRenderer(this, 0, 0);
        this.woodenBeam.setRotationPoint(-8.0F, 11.0F, 7.0F);
        this.woodenBeam.addBox(0.0F, 0.0F, 0.0F, 16, 3, 1, 0.0F);
        this.hookRight = new ModelRenderer(this, 0, 4);
        this.hookRight.setRotationPoint(4.0F, 12.0F, 6.0F);
        this.hookRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.hookLeft.render(f5);
        this.woodenBeam.render(f5);
        this.hookRight.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderAll() {
        this.hookLeft.render(0.0625f);
        this.woodenBeam.render(0.0625f);
        this.hookRight.render(0.0625f);
    }
}
