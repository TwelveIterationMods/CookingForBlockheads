package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.CombinedContainer;
import net.blay09.mods.cookingforblockheads.block.CabinetBlock;
import net.blay09.mods.cookingforblockheads.block.entity.util.DoorAnimator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class CabinetBlockEntity extends CounterBlockEntity {

    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cabinet.value(), pos, state);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.cabinet");
    }

    @Nullable
    public CabinetBlockEntity findNeighbourCabinet() {
        if (!hasLevel()) {
            return null;
        }
        final var state = getBlockState();
        final var stateAbove = level.getBlockState(worldPosition.above());
        final var stateBelow = level.getBlockState(worldPosition.below());
        if (stateAbove.getBlock() == state.getBlock()) {
            return (CabinetBlockEntity) level.getBlockEntity(worldPosition.above());
        } else if (stateBelow.getBlock() == state.getBlock()) {
            return (CabinetBlockEntity) level.getBlockEntity(worldPosition.below());
        }
        return null;
    }

    public CabinetBlockEntity getBaseCabinet() {
        if (!hasLevel()) {
            return this;
        }

        final var stateBelow = level.getBlockState(worldPosition.below());
        if (stateBelow.getBlock() == getBlockState().getBlock()) {
            final var baseCabinet = (CabinetBlockEntity) level.getBlockEntity(worldPosition.below());
            if (baseCabinet != null) {
                return baseCabinet;
            }
        }

        return this;
    }

    @Override
    public DoorAnimator getDoorAnimator() {
        final var baseCabinet = getBaseCabinet();
        return baseCabinet == this ? super.getDoorAnimator() : baseCabinet.getDoorAnimator();
    }

    public Container getCombinedContainer() {
        final var baseCabinet = getBaseCabinet();
        final CabinetBlockEntity neighbourCabinet;
        if (baseCabinet == this) {
            neighbourCabinet = findNeighbourCabinet();
        } else {
            neighbourCabinet = this;
        }

        if (neighbourCabinet != null) {
            return new CombinedContainer(baseCabinet.container, neighbourCabinet.container);
        }

        return container;
    }

    @Override
    public Container getContainer() {
        return getCombinedContainer();
    }

    @Override
    public boolean hasPreservationUpgrade() {
        return getBaseCabinet().hasPreservationUpgrade;
    }

    @Override
    public void setHasPreservationUpgrade(boolean hasPreservationUpgrade) {
        final var baseCabinet = getBaseCabinet();
        baseCabinet.hasPreservationUpgrade = hasPreservationUpgrade;
        baseCabinet.setChanged();
    }

    @Override
    public AABB getRenderBoundingBox() {
        if (getBlockState().getValue(CabinetBlock.MODEL_TYPE) == CabinetBlock.CabinetModelType.LARGE_UPPER) {
            return new AABB(worldPosition.below().offset(-1, 0, -1).getCenter(), worldPosition.offset(2, 1, 2).getCenter());
        }
        return new AABB(worldPosition.offset(-1, 0, -1).getCenter(), worldPosition.offset(2, 2, 2).getCenter());
    }
}
