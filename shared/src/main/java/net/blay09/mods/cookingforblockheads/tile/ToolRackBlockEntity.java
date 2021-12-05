package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

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
    public void load(CompoundTag tag) {
        super.load(tag);
        container.deserialize(tag.getCompound("ItemHandler"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("ItemHandler", container.serialize());
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(IKitchenItemProvider.class, itemProvider));
    }

}
