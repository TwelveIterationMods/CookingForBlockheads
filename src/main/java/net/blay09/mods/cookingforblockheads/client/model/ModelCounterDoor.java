package net.blay09.mods.cookingforblockheads.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCounterDoor extends ModelBase {

    public final ModelRenderer DoorMain;
    public final ModelRenderer DoorHandle;
    public final ModelRenderer DoorMainFlipped;
    public final ModelRenderer DoorHandleFlipped;

    public ModelCounterDoor() {
        textureWidth = 64;
        textureHeight = 16;

        DoorMain = new ModelRenderer(this, 4, 0);
        DoorMain.addBox(0f, 0f, 0f, 12, 14, 1);
        DoorMain.setRotationPoint(-6f, 10f, -6f);
        DoorMain.setTextureSize(64, 16);

        DoorMainFlipped = new ModelRenderer(this, 4, 0);
        DoorMainFlipped.addBox(-12f, 0f, 0f, 12, 14, 1);
        DoorMainFlipped.setRotationPoint(6f, 10f, -6f);
        DoorMainFlipped.setTextureSize(64, 16);

        DoorHandle = new ModelRenderer(this, 0, 0);
        DoorHandle.addBox(9f, 7f, -1f, 1, 2, 1);
        DoorHandle.setRotationPoint(-6f, 8f, -6f);
        DoorHandle.setTextureSize(64, 16);

        DoorHandleFlipped = new ModelRenderer(this, 0, 0);
        DoorHandleFlipped.addBox(-10f, 7f, -1f, 1, 2, 1);
        DoorHandleFlipped.setRotationPoint(6f, 8f, -6f);
        DoorHandleFlipped.setTextureSize(64, 16);
    }

    public void render(boolean flipped) {
        float scale = 0.0625f;
        if (!flipped) {
            DoorMain.render(scale);
        } else {
            DoorMainFlipped.render(scale);
        }
    }

    public void renderNoTint(boolean flipped) {
        float scale = 0.0625f;
        if (!flipped) {
            DoorHandle.render(scale);
        } else {
            DoorHandleFlipped.render(scale);
        }
    }

}