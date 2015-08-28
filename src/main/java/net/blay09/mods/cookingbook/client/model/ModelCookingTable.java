package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelCookingTable - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelCookingTable extends ModelBase {
    public ModelRenderer BottomWall;
    public ModelRenderer RightWall;
    public ModelRenderer LeftWall;
    public ModelRenderer TopFrontPart;
    public ModelRenderer BackWall;
    public ModelRenderer FrontWall;
    public ModelRenderer DoorTopHandle;
    public ModelRenderer DoorBottomHandle;
    public ModelRenderer DoorTop;
    public ModelRenderer DoorBottom;

    public ModelCookingTable() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.RightWall = new ModelRenderer(this, 0, 51);
        this.RightWall.setRotationPoint(-7.0F, 11.0F, -6.0F);
        this.RightWall.addBox(0.0F, 0.0F, 0.0F, 1, 13, 12, 0.0F);
        this.DoorTopHandle = new ModelRenderer(this, 0, 124);
        this.DoorTopHandle.setRotationPoint(-2.0F, 13.0F, -8.0F);
        this.DoorTopHandle.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
        this.DoorTop = new ModelRenderer(this, 26, 35);
        this.DoorTop.setRotationPoint(-5.0F, 12.0F, -7.0F);
        this.DoorTop.addBox(0.0F, 0.0F, 0.0F, 10, 4, 1, 0.0F);
        this.LeftWall = new ModelRenderer(this, 0, 26);
        this.LeftWall.setRotationPoint(6.0F, 11.0F, -6.0F);
        this.LeftWall.addBox(0.0F, 0.0F, 0.0F, 1, 13, 12, 0.0F);
        this.TopFrontPart = new ModelRenderer(this, 0, 0);
        this.TopFrontPart.setRotationPoint(-7.0F, 10.0F, -7.0F);
        this.TopFrontPart.addBox(0.0F, 0.0F, 0.0F, 14, 1, 14, 0.0F);
        this.DoorBottomHandle = new ModelRenderer(this, 0, 124);
        this.DoorBottomHandle.setRotationPoint(-2.0F, 19.0F, -8.0F);
        this.DoorBottomHandle.addBox(0.0F, 0.0F, 0.0F, 4, 1, 1, 0.0F);
        this.BottomWall = new ModelRenderer(this, 0, 15);
        this.BottomWall.setRotationPoint(-5.0F, 21.0F, -4.0F);
        this.BottomWall.addBox(0.0F, 0.0F, 0.0F, 10, 1, 9, 0.0F);
        this.DoorBottom = new ModelRenderer(this, 26, 35);
        this.DoorBottom.setRotationPoint(-5.0F, 18.0F, -7.0F);
        this.DoorBottom.addBox(0.0F, 0.0F, 0.0F, 10, 4, 1, 0.0F);
        this.FrontWall = new ModelRenderer(this, 0, 110);
        this.FrontWall.setRotationPoint(-6.0F, 11.0F, -6.0F);
        this.FrontWall.addBox(0.0F, 0.0F, 0.0F, 12, 13, 1, 0.0F);
        this.BackWall = new ModelRenderer(this, 0, 96);
        this.BackWall.setRotationPoint(-6.0F, 11.0F, 6.0F);
        this.BackWall.addBox(0.0F, 0.0F, 0.0F, 12, 13, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.DoorBottomHandle.render(f5);
        this.BottomWall.render(f5);
        this.TopFrontPart.render(f5);
        this.DoorBottom.render(f5);
        this.DoorTop.render(f5);
        this.RightWall.render(f5);
        this.LeftWall.render(f5);
        this.BackWall.render(f5);
        this.FrontWall.render(f5);
        this.DoorTopHandle.render(f5);
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
        this.DoorBottomHandle.render(f5);
        this.BottomWall.render(f5);
        this.TopFrontPart.render(f5);
        this.DoorBottom.render(f5);
        this.DoorTop.render(f5);
        this.RightWall.render(f5);
        this.LeftWall.render(f5);
        this.BackWall.render(f5);
        this.FrontWall.render(f5);
        this.DoorTopHandle.render(f5);
    }
}
