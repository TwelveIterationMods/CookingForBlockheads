package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.FruitBasketMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FruitBasketBlockEntity extends BalmBlockEntity implements BalmMenuProvider, IMutableNameable, BalmContainerProvider {

    private final DefaultContainer container = new DefaultContainer(27) {
        @Override
        public void setChanged() {
            FruitBasketBlockEntity.this.setChanged();
        }
    };
    private final DefaultKitchenItemProvider itemProvider = new DefaultKitchenItemProvider(container);

    private Component customName;

    public FruitBasketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.fruitBasket.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        container.deserialize(tagCompound.getCompound("ItemHandler"));

        if (tagCompound.contains("CustomName", Tag.TAG_STRING)) {
            customName = Component.Serializer.fromJson(tagCompound.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", container.serialize());

        if (customName != null) {
            tagCompound.putString("CustomName", Component.Serializer.toJson(customName));
        }

        return tagCompound;
    }

    @Override
    public CompoundTag balmToClientTag(CompoundTag tag) {
        return save(tag);
    }

    @Override
    public void balmFromClientTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(IKitchenItemProvider.class, itemProvider));
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public void setCustomName(Component customName) {
        this.customName = customName;
        setChanged();
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new FruitBasketMenu(i, playerInventory, this);
    }

    @Override
    public Component getDefaultName() {
        return new TranslatableComponent("container.cookingforblockheads.fruit_basket");
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
