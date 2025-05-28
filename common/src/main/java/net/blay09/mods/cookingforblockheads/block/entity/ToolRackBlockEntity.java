package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ToolRackBlockEntity extends BalmBlockEntity implements BalmContainerProvider {

    private final DefaultContainer container = new DefaultContainer(2) {
        @Override
        public void setChanged() {
            ToolRackBlockEntity.this.setChanged();
            ToolRackBlockEntity.this.sync();
        }
    };

    public ToolRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.toolRack.get(), pos, state);
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
    public void writeUpdateTag(ValueOutput output) {
        saveAdditional(output);
    }

    @Override
    public Container getContainer() {
        return container;
    }

}
