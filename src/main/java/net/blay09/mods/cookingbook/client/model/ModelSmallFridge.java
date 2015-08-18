package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Small Fridge - Blay & Zero
 * Created using Tabula 5.1.0
 */
public class ModelSmallFridge extends ModelBase {
    public ModelRenderer Shelf;
    public ModelRenderer Door;
    public ModelRenderer DoorHandle;
    public ModelRenderer TopHinge;
    public ModelRenderer BottemHinge;
    public ModelRenderer RightSeal;
    public ModelRenderer LeftSeal;
    public ModelRenderer TopSeal;
    public ModelRenderer BottemSeal;
    public ModelRenderer RightWall;
    public ModelRenderer LeftWall;
    public ModelRenderer Backwall;
    public ModelRenderer Bottom;
    public ModelRenderer Top;
    public ModelRenderer PlugThingy;
    public ModelRenderer BackLeftFoot;
    public ModelRenderer BackRightFoot;
    public ModelRenderer FrontLeftFoot;
    public ModelRenderer FrontRightFoot;

    public ModelSmallFridge() {
        this.textureWidth = 126;
        this.textureHeight = 43;
        this.TopSeal = new ModelRenderer(this, 80, 0);
        this.TopSeal.setRotationPoint(-5.5F, 9.200000000000001F, -5.8F);
        this.TopSeal.addBox(0.0F, 0.0F, 0.0F, 11, 1, 1, 0.0F);
        this.Backwall = new ModelRenderer(this, 16, 12);
        this.Backwall.setRotationPoint(-6.0F, 9.700000000000001F, 5.7F);
        this.Backwall.addBox(1.0F, 1.0F, 0.0F, 10, 11, 1, 0.0F);
        this.Bottom = new ModelRenderer(this, 28, 16);
        this.Bottom.setRotationPoint(-6.0F, 22.699999999999996F, -7.0F);
        this.Bottom.addBox(1.0F, -1.0F, 2.0F, 10, 2, 12, 0.0F);
        this.Top = new ModelRenderer(this, 60, 29);
        this.Top.setRotationPoint(-6.0F, 8.700000000000001F, -7.0F);
        this.Top.addBox(1.0F, 0.0F, 2.0F, 10, 2, 12, 0.0F);
        this.DoorHandle = new ModelRenderer(this, 0, 0);
        this.DoorHandle.setRotationPoint(-6.3F, 9.5F, -5.3F);
        this.DoorHandle.addBox(11.1F, 5.8F, -2.0F, 1, 2, 1, 0.0F);
        this.LeftWall = new ModelRenderer(this, 0, 12);
        this.LeftWall.setRotationPoint(6.0F, 8.700000000000001F, -7.0F);
        this.LeftWall.addBox(-1.0F, 0.0F, 2.0F, 2, 15, 12, 0.0F);
        this.BottemSeal = new ModelRenderer(this, 103, 1);
        this.BottemSeal.setRotationPoint(-5.5F, 22.199999999999996F, -5.8F);
        this.BottemSeal.addBox(0.0F, 0.0F, 0.0F, 11, 1, 1, 0.0F);
        this.PlugThingy = new ModelRenderer(this, 35, 0);
        this.PlugThingy.setRotationPoint(-5.0F, 20.7F, 5.9F);
        this.PlugThingy.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.RightSeal = new ModelRenderer(this, 72, 0);
        this.RightSeal.setRotationPoint(-6.5F, 9.200000000000001F, -5.8F);
        this.RightSeal.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
        this.TopHinge = new ModelRenderer(this, 4, 0);
        this.TopHinge.setRotationPoint(-6.8F, 8.9F, -6.0F);
        this.TopHinge.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.Door = new ModelRenderer(this, 42, 0);
        this.Door.setRotationPoint(-6.3F, 9.5F, -5.3F);
        this.Door.addBox(-0.7F, -0.8F, -1.5F, 14, 15, 1, 0.0F);
        this.Shelf = new ModelRenderer(this, 0, 0);
        this.Shelf.setRotationPoint(-6.0F, 15.8F, -4.0F);
        this.Shelf.addBox(1.0F, 0.0F, -1.0F, 10, 1, 11, 0.0F);
        this.FrontLeftFoot = new ModelRenderer(this, 4, 3);
        this.FrontLeftFoot.setRotationPoint(-6.0F, 23.0F, 5.0F);
        this.FrontLeftFoot.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.BackRightFoot = new ModelRenderer(this, 0, 5);
        this.BackRightFoot.setRotationPoint(5.0F, 23.0F, -4.0F);
        this.BackRightFoot.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.BottemHinge = new ModelRenderer(this, 31, 0);
        this.BottemHinge.setRotationPoint(-6.8F, 22.499999999999996F, -6.0F);
        this.BottemHinge.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.BackLeftFoot = new ModelRenderer(this, 4, 5);
        this.BackLeftFoot.setRotationPoint(-6.0F, 23.0F, -4.0F);
        this.BackLeftFoot.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.RightWall = new ModelRenderer(this, 80, 2);
        this.RightWall.setRotationPoint(-7.0F, 8.700000000000001F, -7.0F);
        this.RightWall.addBox(0.0F, 0.0F, 2.0F, 2, 15, 12, 0.0F);
        this.LeftSeal = new ModelRenderer(this, 76, 0);
        this.LeftSeal.setRotationPoint(5.5F, 9.200000000000001F, -5.8F);
        this.LeftSeal.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1, 0.0F);
        this.FrontRightFoot = new ModelRenderer(this, 0, 3);
        this.FrontRightFoot.setRotationPoint(5.0F, 23.0F, 5.0F);
        this.FrontRightFoot.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.RightWall.render(f5);
        this.Shelf.render(f5);
        this.LeftSeal.render(f5);
        this.Bottom.render(f5);
        this.PlugThingy.render(f5);
        this.Top.render(f5);
        this.LeftWall.render(f5);
        this.TopHinge.render(f5);
        this.BottemHinge.render(f5);
        this.RightSeal.render(f5);
        this.DoorHandle.render(f5);
        this.BackRightFoot.render(f5);
        this.Door.render(f5);
        this.FrontRightFoot.render(f5);
        this.BottemSeal.render(f5);
        this.BackLeftFoot.render(f5);
        this.Backwall.render(f5);
        this.FrontLeftFoot.render(f5);
        this.TopSeal.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderInterior() {
        float f5 = 0.0625f;
        this.Shelf.render(f5);
    }

    public void renderUncolored() {
        float f5 = 0.0625f;
        this.PlugThingy.render(f5);
        this.LeftSeal.render(f5);
        this.RightSeal.render(f5);
        this.BottemSeal.render(f5);
        this.TopSeal.render(f5);
        this.TopHinge.render(f5);
        this.BottemHinge.render(f5);
        this.DoorHandle.render(f5);
        this.BackRightFoot.render(f5);
        this.FrontRightFoot.render(f5);
        this.BackLeftFoot.render(f5);
        this.FrontLeftFoot.render(f5);
    }

    public void renderColored() {
        float f5 = 0.0625f;
        this.RightWall.render(f5);
        this.Bottom.render(f5);
        this.Top.render(f5);
        this.LeftWall.render(f5);
        this.Door.render(f5);
        this.Backwall.render(f5);
    }
}
