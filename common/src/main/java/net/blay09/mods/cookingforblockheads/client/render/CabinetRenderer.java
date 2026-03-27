package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.block.entity.CabinetBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.DyeColor;
import org.jspecify.annotations.Nullable;

public class CabinetRenderer extends CounterRenderer<CabinetBlockEntity> {

    private static final float doorOriginX = 0.84375f;

    private static final float doorOriginZ = 0.1875f;

    public CabinetRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

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
    protected DeferredBlockStateModel getDoorModel(@Nullable DyeColor blockColor, boolean isFlipped) {
        return isFlipped ? ModModels.cabinetDoorsFlipped.get(blockColor) : ModModels.cabinetDoors.get(blockColor);
    }
}
