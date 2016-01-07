package net.blay09.mods.cookingbook.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * toaster - ZeroTheShinigami
 * Created using Tabula 4.1.1
 */
public class ModelToaster extends ModelBase {
    public double[] modelScale = new double[] { 2D, 2D, 2D };
    public ModelRenderer Buttery;
    public ModelRenderer Why;
    public ModelRenderer Biscuit;
    public ModelRenderer Base;
    public ModelRenderer Blaywill;
    public ModelRenderer nevernotice;
    public ModelRenderer That;
    public ModelRenderer Naming;
    public ModelRenderer Nonsensible;
    public ModelRenderer Cause;
    public ModelRenderer Senpai;
    public ModelRenderer Never;
    public ModelRenderer ToasterButtonThingy;
    public ModelRenderer Im;
    public ModelRenderer These;
    public ModelRenderer Parts;
    public ModelRenderer With;
    public ModelRenderer Names;
    public ModelRenderer Notices;
    public ModelRenderer Me;
    public ModelRenderer BibleThump;
    public ModelRenderer is;
    public ModelRenderer it;
    public ModelRenderer BalyBot;
    public ModelRenderer Not;
    public ModelRenderer BlayBot;

    public ModelToaster() {
        this.textureWidth = 104;
        this.textureHeight = 34;
        this.Biscuit = new ModelRenderer(this, 0, 0);
        this.Biscuit.setRotationPoint(-0.5F, 0.5F, 1.5F);
        this.Biscuit.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.Never = new ModelRenderer(this, 48, 0);
        this.Never.mirror = true;
        this.Never.setRotationPoint(0.0F, -7.0F, 13.0F);
        this.Never.addBox(0.0F, 0.0F, 0.0F, 8, 7, 1, 0.0F);
        this.Im = new ModelRenderer(this, 36, 23);
        this.Im.setRotationPoint(-1.0F, -1.0F, 0.0F);
        this.Im.addBox(0.0F, 0.0F, 0.0F, 1, 1, 10, 0.0F);
        this.ToasterButtonThingy = new ModelRenderer(this, 8, 1);
        this.ToasterButtonThingy.setRotationPoint(3.0F, -4.0F, -0.5F);
        this.ToasterButtonThingy.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.Notices = new ModelRenderer(this, 0, 3);
        this.Notices.setRotationPoint(-1.0F, 0.0F, -1.0F);
        this.Notices.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.it = new ModelRenderer(this, 63, 12);
        this.it.setRotationPoint(1.0F, 0.0F, 8.0F);
        this.it.addBox(0.0F, 0.0F, 0.0F, 6, 8, 2, 0.0F);
        this.nevernotice = new ModelRenderer(this, 0, 0);
        this.nevernotice.setRotationPoint(6.5F, 0.5F, 10.5F);
        this.nevernotice.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.BalyBot = new ModelRenderer(this, 84, 9);
        this.BalyBot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BalyBot.addBox(3.0F, 0.0F, 2.0F, 2, 7, 6, 0.0F);
        this.Not = new ModelRenderer(this, 72, 12);
        this.Not.setRotationPoint(7.0F, 0.0F, 0.0F);
        this.Not.addBox(0.0F, 0.0F, 0.0F, 1, 8, 10, 0.0F);
        this.Cause = new ModelRenderer(this, 56, 1);
        this.Cause.setRotationPoint(-1.0F, 0.0F, 2.0F);
        this.Cause.addBox(0.0F, 0.0F, 0.0F, 1, 1, 10, 0.0F);
        this.Me = new ModelRenderer(this, 4, 3);
        this.Me.setRotationPoint(8.0F, 0.0F, -1.0F);
        this.Me.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.Parts = new ModelRenderer(this, 0, 3);
        this.Parts.setRotationPoint(-1.0F, 0.0F, 1.0F);
        this.Parts.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.Nonsensible = new ModelRenderer(this, 0, 15);
        this.Nonsensible.setRotationPoint(-2.0F, -7.0F, 2.0F);
        this.Nonsensible.addBox(0.0F, 0.0F, 0.0F, 1, 8, 10, 0.0F);
        this.Names = new ModelRenderer(this, 12, 23);
        this.Names.setRotationPoint(1.0F, -1.0F, 0.0F);
        this.Names.addBox(0.0F, 0.0F, 0.0F, 1, 1, 10, 0.0F);
        this.Buttery = new ModelRenderer(this, 0, 0);
        this.Buttery.setRotationPoint(-4.0F, 0.5F, -7.0F);
        this.Buttery.addBox(0.0F, 0.0F, 0.0F, 8, 1, 14, 0.0F);
        this.With = new ModelRenderer(this, 4, 3);
        this.With.setRotationPoint(8.0F, 0.0F, 1.0F);
        this.With.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1, 0.0F);
        this.is = new ModelRenderer(this, 44, 12);
        this.is.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.is.addBox(0.0F, 0.0F, 0.0F, 6, 8, 2, 0.0F);
        this.Naming = new ModelRenderer(this, 30, 0);
        this.Naming.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.Naming.addBox(0.0F, 0.0F, 0.0F, 8, 7, 1, 0.0F);
        this.These = new ModelRenderer(this, 30, 8);
        this.These.setRotationPoint(0.0F, -1.0F, 1.0F);
        this.These.addBox(0.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        this.Blaywill = new ModelRenderer(this, 0, 0);
        this.Blaywill.setRotationPoint(-0.5F, 0.5F, 10.5F);
        this.Blaywill.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.BibleThump = new ModelRenderer(this, 48, 8);
        this.BibleThump.setRotationPoint(0.0F, -1.0F, -1.0F);
        this.BibleThump.addBox(0.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        this.That = new ModelRenderer(this, 24, 15);
        this.That.setRotationPoint(9.0F, -7.0F, 2.0F);
        this.That.addBox(0.0F, 0.0F, 0.0F, 1, 8, 10, 0.0F);
        this.Why = new ModelRenderer(this, 50, 12);
        this.Why.setRotationPoint(-4.0F, -8.0F, -5.0F);
        this.Why.addBox(0.0F, 0.0F, 0.0F, 1, 8, 10, 0.0F);
        this.Senpai = new ModelRenderer(this, 68, 0);
        this.Senpai.setRotationPoint(8.0F, 0.0F, 2.0F);
        this.Senpai.addBox(0.0F, 0.0F, 0.0F, 1, 1, 10, 0.0F);
        this.BlayBot = new ModelRenderer(this, 80, 1);
        this.BlayBot.setRotationPoint(1.0F, 7.0F, 2.0F);
        this.BlayBot.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.Base = new ModelRenderer(this, 0, 0);
        this.Base.setRotationPoint(6.5F, 0.5F, 1.5F);
        this.Base.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.Buttery.addChild(this.Biscuit);
        this.Buttery.addChild(this.Never);
        this.That.addChild(this.Im);
        this.Buttery.addChild(this.ToasterButtonThingy);
        this.Never.addChild(this.Notices);
        this.Why.addChild(this.it);
        this.Buttery.addChild(this.nevernotice);
        this.Why.addChild(this.BalyBot);
        this.Why.addChild(this.Not);
        this.Buttery.addChild(this.Cause);
        this.Never.addChild(this.Me);
        this.Naming.addChild(this.Parts);
        this.Buttery.addChild(this.Nonsensible);
        this.Nonsensible.addChild(this.Names);
        this.Naming.addChild(this.With);
        this.Why.addChild(this.is);
        this.Buttery.addChild(this.Naming);
        this.Naming.addChild(this.These);
        this.Buttery.addChild(this.Blaywill);
        this.Never.addChild(this.BibleThump);
        this.Buttery.addChild(this.That);
        this.Buttery.addChild(this.Senpai);
        this.Why.addChild(this.BlayBot);
        this.Buttery.addChild(this.Base);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GL11.glPushMatrix();
        GL11.glScaled(1D / modelScale[0], 1D / modelScale[1], 1D / modelScale[2]);
        this.Buttery.render(f5);
        this.Why.render(f5);
        GL11.glPopMatrix();
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
        GL11.glPushMatrix();
        GL11.glScaled(1D / modelScale[0], 1D / modelScale[1], 1D / modelScale[2]);
        this.Buttery.render(f5);
        this.Why.render(f5);
        GL11.glPopMatrix();
    }

}
