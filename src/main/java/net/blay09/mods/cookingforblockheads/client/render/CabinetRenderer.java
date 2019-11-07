package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.DyeColor;

import javax.annotation.Nullable;

public class CabinetRenderer extends CounterRenderer {

    private static final float doorOriginX = 0.84375f;

    private static final float doorOriginZ = 0.1875f;

    @Override
    protected float getDoorOriginX() {
        return doorOriginX;
    }

    @Override
    protected float getDoorOriginZ() {
        return doorOriginZ;
    }

    @Override
    protected float getBottomShelfOffsetY() {
        return -0.45f;
    }

    @Override
    protected float getTopShelfOffsetY() {
        return 0.9f;
    }

    @Override
    protected IBakedModel getDoorModel(@Nullable DyeColor blockColor, boolean isFlipped) {
        int colorIndex = blockColor != null ? blockColor.getId() + 1 : 0;
        return isFlipped ? ModModels.cabinetDoorsFlipped[colorIndex] : ModModels.cabinetDoors[colorIndex];
    }
}
