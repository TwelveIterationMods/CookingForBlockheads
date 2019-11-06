package net.blay09.mods.cookingforblockheads.client.render;

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

    /*@Override TODO fixme
    protected IBakedModel getDoorModel(Direction facing, DyeColor blockColor, boolean isFlipped) {
        return isFlipped ? modelsFlipped[facing.getHorizontalIndex()][blockColor.getId()] : models[facing.getHorizontalIndex()][blockColor.getId()];
    }*/
}
