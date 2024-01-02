package net.blay09.mods.cookingforblockheads.block.entity;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.FruitBasketMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
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
    private boolean isDirty;

    public FruitBasketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.fruitBasket.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        container.deserialize(tag.getCompound("ItemHandler"));

        if (tag.contains("CustomName", Tag.TAG_STRING)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("ItemHandler", container.serialize());

        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName));
        }
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
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
        return Component.translatable("container.cookingforblockheads.fruit_basket");
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FruitBasketBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (isDirty) {
            sync();
            isDirty = false;
        }
    }

    @Override
    public void setChanged() {
        isDirty = true;
        super.setChanged();
    }
}
