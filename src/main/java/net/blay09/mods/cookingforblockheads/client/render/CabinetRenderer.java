package net.blay09.mods.cookingforblockheads.client.render;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;

public class CabinetRenderer extends CounterRenderer {

    private static final float[] doorOriginsX = new float[]{
            (1 - 0.84375f) - 0 / 16f, // South
            0.09375f + 1 / 16f + 1 / 32f, // West
            0.84375f + 0 / 16f, // North
            1 - (0.09375f + 1 / 16f + 1 / 32f) // East
    };

    private static final float[] doorOriginsZ = new float[]{
            (1 - 0.09375f) - 1 / 16f - 1 / 32f, // South
            (1 - 0.84375f) + 0 / 16f, // West
            0.09375f + 1 / 16f + 1 / 32f, // North
            0.84375f + 0 / 16f // East
    };

    @Override
    protected float getDoorOriginX(Direction facing) {
        return doorOriginsX[facing.getHorizontalIndex()];
    }

    @Override
    protected float getDoorOriginZ(Direction facing) {
        return doorOriginsZ[facing.getHorizontalIndex()];
    }

    @Override
    protected float getBottomShelfOffsetY() {
        return -0.45f;
    }

    @Override
    protected float getTopShelfOffsetY() {
        return 0.9f;
    }

    /*@Override TODO fixme
    protected IBakedModel getDoorModel(Direction facing, DyeColor blockColor, boolean isFlipped) {
        return isFlipped ? modelsFlipped[facing.getHorizontalIndex()][blockColor.getId()] : models[facing.getHorizontalIndex()][blockColor.getId()];
    }*/
}
