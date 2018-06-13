package net.blay09.mods.cookingforblockheads.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelOvenDoor extends ModelBase {

    public final ModelRenderer DoorMain;
    public final ModelRenderer DoorBopLeft;
    public final ModelRenderer DoorBopRight;
    public final ModelRenderer DoorHandle;

    public ModelOvenDoor() {
        textureWidth = 64;
        textureHeight = 16;

        DoorMain = new ModelRenderer(this, 0, 0);
        DoorMain.addBox(0f, -10f, 0f, 12, 11, 1);
        DoorMain.setRotationPoint(-6f, 23f, -7f);
        DoorMain.setTextureSize(64, 16);

        DoorBopLeft = new ModelRenderer(this, 0, 0);
        DoorBopLeft.addBox(2f, -10f, -1f, 1, 1, 1);
        DoorBopLeft.setRotationPoint(-6f, 23f, -7f);
        DoorBopLeft.setTextureSize(64, 16);

        DoorBopRight = new ModelRenderer(this, 0, 0);
        DoorBopRight.addBox(9f, -10f, -1f, 1, 1, 1);
        DoorBopRight.setRotationPoint(-6f, 23f, -7f);
        DoorBopRight.setTextureSize(64, 16);

        DoorHandle = new ModelRenderer(this, 0, 0);
        DoorHandle.addBox(0f, -10f, -2f, 12, 1, 1);
        DoorHandle.setRotationPoint(-6f, 23f, -7f);
        DoorHandle.setTextureSize(64, 16);
    }

    public void render() {
        float scale = 0.0625f;
        DoorMain.render(scale);
    }

    public void renderNoTint() {
        float scale = 0.0625f;
        DoorBopLeft.render(scale);
        DoorBopRight.render(scale);
        DoorHandle.render(scale);
    }

}