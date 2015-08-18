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

    public ModelOven() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Oven = new ModelRenderer(this, 0, 0);
        this.Oven.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.Oven.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
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

    public void renderAll() {
        float f5 = 0.0625f;
        this.Oven.render(f5);
    }
}
