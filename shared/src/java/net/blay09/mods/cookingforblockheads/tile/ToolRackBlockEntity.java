package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;

public class ToolRackBlockEntity extends BalmBlockEntity {

    private final DefaultContainer container = new DefaultContainer(2) {
        @Override
        public void setChanged() {
            ToolRackBlockEntity.this.setChanged();
        }
    };

    private final DefaultKitchenItemProvider itemProvider = new DefaultKitchenItemProvider(container);

    public ToolRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.toolRack.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        container.deserialize(tagCompound.getCompound("ItemHandler"));
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", container.serialize());
        return tagCompound;
    }

    public Container getContainer() {
        return container;
    }

    /*@Override TODO
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, itemHandlerCap);
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        }

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }*/

}
