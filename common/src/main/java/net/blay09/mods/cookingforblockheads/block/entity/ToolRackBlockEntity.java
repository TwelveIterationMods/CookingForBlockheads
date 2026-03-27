package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.DefaultContainer;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class ToolRackBlockEntity extends BlockEntity implements BalmContainerProvider {

    private final DefaultContainer container = new DefaultContainer(2) {
        @Override
        public void setChanged() {
            ToolRackBlockEntity.this.setChanged();
            BalmBlockEntityUtils.sync(ToolRackBlockEntity.this);
        }
    };

    public ToolRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.toolRack.value(), pos, state);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        input.child("ItemHandler").ifPresent(it -> ContainerHelper.loadAllItems(it, container.getItems()));
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        dropItems(level, pos);
    }

}
