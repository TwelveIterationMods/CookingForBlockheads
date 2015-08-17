package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelFridgeSmall - BlayTheNinth
 * Created using Tabula 4.1.1
 */
public class ModelFridgeSmall extends ModelBase {
    public ModelRenderer chestBelow;
    public ModelRenderer chestLid;

    public ModelFridgeSmall() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.chestLid = new ModelRenderer(this, 0, 0);
        this.chestLid.setRotationPoint(-7.5F, 24.0F, -7.0F);
        this.chestLid.addBox(0.0F, 0.0F, 0.0F, 15, 5, 14, 0.0F);
        this.setRotateAngle(chestLid, 0.0F, 0.0F, -1.5707963267948966F);
        this.chestBelow = new ModelRenderer(this, 0, 19);
        this.chestBelow.setRotationPoint(-2.5F, 24.0F, -7.0F);
        this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 15, 10, 14, 0.0F);
        this.setRotateAngle(chestBelow, 0.0F, 0.0F, -1.5707963267948966F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.chestBelow.render(f5);
        this.chestLid.render(f5);
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
        this.chestLid.render(0.0625F);
        this.chestBelow.render(0.0625F);
    }
}
