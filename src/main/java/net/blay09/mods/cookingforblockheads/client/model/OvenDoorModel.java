package net.blay09.mods.cookingforblockheads.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class OvenDoorModel extends Model {

    public final RendererModel DoorMain;
    public final RendererModel DoorBopLeft;
    public final RendererModel DoorBopRight;
    public final RendererModel DoorHandle;

    public OvenDoorModel() {
        textureWidth = 64;
        textureHeight = 16;

        DoorMain = new RendererModel(this, 0, 0);
        DoorMain.addBox(0f, -10f, 0f, 12, 11, 1);
        DoorMain.setRotationPoint(-6f, 23f, -7f);
        DoorMain.setTextureSize(64, 16);

        DoorBopLeft = new RendererModel(this, 0, 0);
        DoorBopLeft.addBox(2f, -10f, -1f, 1, 1, 1);
        DoorBopLeft.setRotationPoint(-6f, 23f, -7f);
        DoorBopLeft.setTextureSize(64, 16);

        DoorBopRight = new RendererModel(this, 0, 0);
        DoorBopRight.addBox(9f, -10f, -1f, 1, 1, 1);
        DoorBopRight.setRotationPoint(-6f, 23f, -7f);
        DoorBopRight.setTextureSize(64, 16);

        DoorHandle = new RendererModel(this, 0, 0);
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
