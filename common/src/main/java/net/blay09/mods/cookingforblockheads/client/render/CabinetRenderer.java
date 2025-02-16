package net.blay09.mods.cookingforblockheads.client.render;

import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.CabinetBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

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
    protected BakedModel getDoorModel(@Nullable DyeColor blockColor, boolean isFlipped) {
        int colorIndex = blockColor != null ? blockColor.getId() + 1 : 0;
        return isFlipped ? ModModels.cabinetDoorsFlipped.get(colorIndex).get() : ModModels.cabinetDoors.get(colorIndex).get();
    }
}
