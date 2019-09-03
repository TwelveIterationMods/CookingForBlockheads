package net.blay09.mods.cookingforblockheads.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class FridgeLargeDoorModel extends Model {

    public final RendererModel DoorMain;
    public final RendererModel DoorMainFlipped;
    public final RendererModel DoorHandle;
    public final RendererModel DoorHandleFlipped;

    public FridgeLargeDoorModel() {
        textureWidth = 64;
        textureHeight = 32;

        DoorMain = new RendererModel(this, 4, 0);
        DoorMain.addBox(0f, 0f, 0f, 14, 29, 1);
        DoorMain.setRotationPoint(-7f, -5f, -7f);
        DoorMain.setTextureSize(64, 32);

        DoorMainFlipped = new RendererModel(this, 4, 0);
        DoorMainFlipped.addBox(-14f, 0f, 0f, 14, 29, 1);
        DoorMainFlipped.setRotationPoint(7f, -5f, -7f);
        DoorMainFlipped.setTextureSize(64, 32);

        DoorHandle = new RendererModel(this, 0, 0);
        DoorHandle.addBox(12f, 3f, -1f, 1, 23, 1);
        DoorHandle.setRotationPoint(-7f, -5f, -7f);
        DoorHandle.setTextureSize(64, 32);

        DoorHandleFlipped = new RendererModel(this, 0, 0);
        DoorHandleFlipped.addBox(-13f, 3f, -1f, 1, 23, 1);
        DoorHandleFlipped.setRotationPoint(7f, -5f, -7f);
        DoorHandleFlipped.setTextureSize(64, 32);
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
