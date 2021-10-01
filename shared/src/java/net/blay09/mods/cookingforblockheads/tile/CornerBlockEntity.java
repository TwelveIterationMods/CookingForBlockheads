package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CornerBlockEntity extends BalmBlockEntity {

    public CornerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.corner.get(), pos, state);
    }

    /*@Override TODO
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return CapabilityKitchenConnector.CAPABILITY.orEmpty(cap, kitchenConnectorCap);
    }*/

}
