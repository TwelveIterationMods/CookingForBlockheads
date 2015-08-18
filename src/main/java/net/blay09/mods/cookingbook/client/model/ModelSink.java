package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * sink - BlayTheNineth & Zero9o1o
 * Created using Tabula 5.1.0
 */
public class ModelSink extends ModelBase {
    public ModelRenderer BasinTopFront;
    public ModelRenderer BasinTopRight;
    public ModelRenderer BasinTopBack;
    public ModelRenderer BasinTopLeft;
    public ModelRenderer BasinFront;
    public ModelRenderer BasinBack;
    public ModelRenderer BasinRight;
    public ModelRenderer BasinLeft;
    public ModelRenderer BasinBottem;
    public ModelRenderer BasinMiddleWall;
    public ModelRenderer RightWall;
    public ModelRenderer LeftWall;
    public ModelRenderer BackWall;
    public ModelRenderer BottemWall;
    public ModelRenderer FrontRightWall;
    public ModelRenderer FrontLeftWall;
    public ModelRenderer FrontTopWall;
    public ModelRenderer FrontBottemWall;
    public ModelRenderer Door;
    public ModelRenderer DoorHandle;
    public ModelRenderer FaucetFront;
    public ModelRenderer FaucetBack;
    public ModelRenderer TopFrontPart;
    public ModelRenderer TopBackPart;
    public ModelRenderer TopRightPart;
    public ModelRenderer TopRightPart_1;

    public ModelSink() {
        this.textureWidth = 122;
        this.textureHeight = 37;
        this.BasinTopLeft = new ModelRenderer(this, 48, 14);
        this.BasinTopLeft.setRotationPoint(4.5F, 9.8F, -3.0F);
        this.BasinTopLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 7, 0.0F);
        this.LeftWall = new ModelRenderer(this, 24, 0);
        this.LeftWall.setRotationPoint(5.0F, 11.0F, -5.0F);
        this.LeftWall.addBox(0.0F, 0.0F, 0.0F, 1, 13, 11, 0.0F);
        this.BasinMiddleWall = new ModelRenderer(this, 108, 21);
        this.BasinMiddleWall.setRotationPoint(-0.7F, 9.7F, -2.5F);
        this.BasinMiddleWall.addBox(0.0F, 0.0F, 0.0F, 1, 3, 6, 0.0F);
        this.DoorHandle = new ModelRenderer(this, 27, 0);
        this.DoorHandle.setRotationPoint(-4.0F, 12.0F, -5.0F);
        this.DoorHandle.addBox(6.0F, 1.0F, -1.6F, 1, 2, 1, 0.0F);
        this.BasinBack = new ModelRenderer(this, 64, 20);
        this.BasinBack.setRotationPoint(-5.0F, 9.7F, 3.5F);
        this.BasinBack.addBox(0.0F, 0.0F, 0.0F, 10, 3, 1, 0.0F);
        this.TopRightPart_1 = new ModelRenderer(this, 25, 29);
        this.TopRightPart_1.setRotationPoint(5.0F, 10.0F, -3.0F);
        this.TopRightPart_1.addBox(0.0F, 0.0F, 0.0F, 2, 1, 7, 0.0F);
        this.FaucetFront = new ModelRenderer(this, 0, 0);
        this.FaucetFront.setRotationPoint(2.0F, 9.1F, 2.0F);
        this.FaucetFront.addBox(0.0F, -1.1F, 0.0F, 1, 1, 1, 0.0F);
        this.FrontTopWall = new ModelRenderer(this, 13, 0);
        this.FrontTopWall.setRotationPoint(-3.0F, 11.0F, -5.0F);
        this.FrontTopWall.addBox(0.0F, 0.0F, 0.0F, 6, 2, 1, 0.0F);
        this.TopRightPart = new ModelRenderer(this, 83, 26);
        this.TopRightPart.setRotationPoint(-7.0F, 10.0F, -3.0F);
        this.TopRightPart.addBox(0.0F, 0.0F, 0.0F, 2, 1, 7, 0.0F);
        this.FrontRightWall = new ModelRenderer(this, 108, 0);
        this.FrontRightWall.setRotationPoint(-5.0F, 11.0F, -5.0F);
        this.FrontRightWall.addBox(0.0F, 0.0F, 0.0F, 2, 12, 1, 0.0F);
        this.TopBackPart = new ModelRenderer(this, 56, 29);
        this.TopBackPart.setRotationPoint(-7.0F, 10.0F, 4.0F);
        this.TopBackPart.addBox(0.0F, 0.0F, 0.0F, 14, 1, 3, 0.0F);
        this.BasinBottem = new ModelRenderer(this, 0, 24);
        this.BasinBottem.setRotationPoint(-4.5F, 12.0F, -3.0F);
        this.BasinBottem.addBox(0.0F, 0.0F, 0.0F, 9, 1, 7, 0.0F);
        this.BackWall = new ModelRenderer(this, 48, 0);
        this.BackWall.setRotationPoint(-5.0F, 11.0F, 5.0F);
        this.BackWall.addBox(0.0F, 0.0F, 0.0F, 10, 13, 1, 0.0F);
        this.BasinLeft = new ModelRenderer(this, 94, 20);
        this.BasinLeft.setRotationPoint(4.0F, 9.7F, -2.5F);
        this.BasinLeft.addBox(0.0F, 0.0F, 0.0F, 1, 3, 6, 0.0F);
        this.BasinRight = new ModelRenderer(this, 86, 17);
        this.BasinRight.setRotationPoint(-5.0F, 9.7F, -2.5F);
        this.BasinRight.addBox(0.0F, 0.0F, 0.0F, 1, 3, 6, 0.0F);
        this.TopFrontPart = new ModelRenderer(this, 25, 24);
        this.TopFrontPart.setRotationPoint(-7.0F, 10.0F, -7.0F);
        this.TopFrontPart.addBox(0.0F, 0.0F, 0.0F, 14, 1, 4, 0.0F);
        this.FrontLeftWall = new ModelRenderer(this, 114, 0);
        this.FrontLeftWall.setRotationPoint(3.0F, 11.0F, -5.0F);
        this.FrontLeftWall.addBox(0.0F, 0.0F, 0.0F, 2, 12, 1, 0.0F);
        this.FrontBottemWall = new ModelRenderer(this, 13, 3);
        this.FrontBottemWall.setRotationPoint(-3.0F, 20.0F, -5.0F);
        this.FrontBottemWall.addBox(0.0F, 0.0F, 0.0F, 6, 3, 1, 0.0F);
        this.RightWall = new ModelRenderer(this, 0, 0);
        this.RightWall.setRotationPoint(-6.0F, 11.0F, -5.0F);
        this.RightWall.addBox(0.0F, 0.0F, 0.0F, 1, 13, 11, 0.0F);
        this.FaucetBack = new ModelRenderer(this, 4, 0);
        this.FaucetBack.setRotationPoint(2.0F, 8.0F, 3.0F);
        this.FaucetBack.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
        this.BasinTopBack = new ModelRenderer(this, 88, 15);
        this.BasinTopBack.setRotationPoint(-5.5F, 9.8F, 4.0F);
        this.BasinTopBack.addBox(0.0F, 0.0F, 0.0F, 11, 1, 1, 0.0F);
        this.Door = new ModelRenderer(this, 70, 10);
        this.Door.setRotationPoint(-4.0F, 12.0F, -5.0F);
        this.Door.addBox(0.0F, 0.0F, -1.0F, 8, 9, 1, 0.0F);
        this.BottemWall = new ModelRenderer(this, 70, 0);
        this.BottemWall.setRotationPoint(-5.0F, 21.0F, -4.0F);
        this.BottemWall.addBox(0.0F, 0.0F, 0.0F, 10, 1, 9, 0.0F);
        this.BasinTopFront = new ModelRenderer(this, 88, 13);
        this.BasinTopFront.setRotationPoint(-5.5F, 9.8F, -4.0F);
        this.BasinTopFront.addBox(0.0F, 0.0F, 0.0F, 11, 1, 1, 0.0F);
        this.BasinFront = new ModelRenderer(this, 13, 7);
        this.BasinFront.setRotationPoint(-5.0F, 9.7F, -3.5F);
        this.BasinFront.addBox(0.0F, 0.0F, 0.0F, 10, 3, 1, 0.0F);
        this.BasinTopRight = new ModelRenderer(this, 105, 13);
        this.BasinTopRight.setRotationPoint(-5.5F, 9.8F, -3.0F);
        this.BasinTopRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 7, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.BasinTopLeft.render(f5);
        this.LeftWall.render(f5);
        this.BasinMiddleWall.render(f5);
        this.DoorHandle.render(f5);
        this.BasinBack.render(f5);
        this.TopRightPart_1.render(f5);
        this.FaucetFront.render(f5);
        this.FrontTopWall.render(f5);
        this.TopRightPart.render(f5);
        this.FrontRightWall.render(f5);
        this.TopBackPart.render(f5);
        this.BasinBottem.render(f5);
        this.BackWall.render(f5);
        this.BasinLeft.render(f5);
        this.BasinRight.render(f5);
        this.TopFrontPart.render(f5);
        this.FrontLeftWall.render(f5);
        this.FrontBottemWall.render(f5);
        this.RightWall.render(f5);
        this.FaucetBack.render(f5);
        this.BasinTopBack.render(f5);
        this.Door.render(f5);
        this.BottemWall.render(f5);
        this.BasinTopFront.render(f5);
        this.BasinFront.render(f5);
        this.BasinTopRight.render(f5);
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
        this.BasinTopLeft.render(f5);
        this.LeftWall.render(f5);
        this.BasinMiddleWall.render(f5);
        this.DoorHandle.render(f5);
        this.BasinBack.render(f5);
        this.TopRightPart_1.render(f5);
        this.FaucetFront.render(f5);
        this.FrontTopWall.render(f5);
        this.TopRightPart.render(f5);
        this.FrontRightWall.render(f5);
        this.TopBackPart.render(f5);
        this.BasinBottem.render(f5);
        this.BackWall.render(f5);
        this.BasinLeft.render(f5);
        this.BasinRight.render(f5);
        this.TopFrontPart.render(f5);
        this.FrontLeftWall.render(f5);
        this.FrontBottemWall.render(f5);
        this.RightWall.render(f5);
        this.FaucetBack.render(f5);
        this.BasinTopBack.render(f5);
        this.Door.render(f5);
        this.BottemWall.render(f5);
        this.BasinTopFront.render(f5);
        this.BasinFront.render(f5);
        this.BasinTopRight.render(f5);
    }
}
