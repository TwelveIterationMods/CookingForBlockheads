package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.CombinedContainer;
import net.blay09.mods.balm.world.DefaultContainer;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.blay09.mods.cookingforblockheads.block.entity.util.DoorAnimator;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableContainer;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.kitchen.CombinedKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ConditionalKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ConversingKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.FridgeMenu;
import net.blay09.mods.cookingforblockheads.sound.ModSounds;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FridgeBlockEntity extends BlockEntity implements BalmMenuProvider<BlockPos>, IMutableNameable, BalmContainerProvider, TransferableBlockEntity<TransferableContainer>, UpgradeablePreservation, KitchenItemProviderHolder {

    private final KitchenItemProvider iceUnitItemProvider = new KitchenItemProvider() {
        private final Set<ItemStack> providedItems = Set.of(new ItemStack(Items.SNOWBALL), new ItemStack(Items.SNOW_BLOCK), new ItemStack(Items.ICE));

        @Override
        public IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
            for (final var providedItem : providedItems) {
                if (ingredient.test(providedItem)) {
                    return new IceUnitIngredientToken(providedItem);
                }
            }
            return null;
        }

        @Override
        public IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint) {
            for (final var providedItem : providedItems) {
                if (ItemStack.isSameItem(providedItem, itemStack)) {
                    return new IceUnitIngredientToken(providedItem);
                }
            }
            return null;
        }

        @Override
        public CacheHint getCacheHint(IngredientToken ingredientToken) {
            return CacheHint.NONE;
        }
    };
    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);
    public boolean hasIceUpgrade;
    public boolean hasPreservationUpgrade;
    private Component customName;
    private boolean isDirty;
    private final DefaultContainer container = new DefaultContainer(27) {
        @Override
        public void setChanged() {
            isDirty = true;
            FridgeBlockEntity.this.setChanged();
        }
    };
    private final ContainerKitchenItemProvider conservingItemProvider = new ConversingKitchenItemProvider(container);
    private final ContainerKitchenItemProvider containerItemProvider = new ContainerKitchenItemProvider(container);
    private final KitchenItemProvider itemProvider = new CombinedKitchenItemProvider(List.of(
            new ConditionalKitchenItemProvider<>(this::hasIceUpgrade, iceUnitItemProvider),
            new ConditionalKitchenItemProvider<>(this::hasPreservationUpgrade, conservingItemProvider, containerItemProvider)));

    public FridgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.fridge.value(), pos, state);
        doorAnimator.setOpenRadius(2);
        doorAnimator.setSoundEventOpen(ModSounds.fridgeOpen.value());
        doorAnimator.setSoundEventClose(ModSounds.fridgeClose.value());
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, FridgeBlockEntity blockEntity) {
        blockEntity.clientTick(level, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FridgeBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public boolean hasIceUpgrade() {
        return hasIceUpgrade;
    }

    public void setHasIceUpgrade(boolean hasIceUpgrade) {
        this.hasIceUpgrade = hasIceUpgrade;
        markDirtyAndUpdate();
    }

    @Override
    public boolean hasPreservationUpgrade() {
        return getBaseFridge().hasPreservationUpgrade;
    }

    @Override
    public void setHasPreservationUpgrade(boolean hasPreservationUpgrade) {
        final var baseFridge = getBaseFridge();
        baseFridge.hasPreservationUpgrade = hasPreservationUpgrade;
        baseFridge.markDirtyAndUpdate();
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        doorAnimator.update();
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (isDirty) {
            sync();
            isDirty = false;
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.triggerEvent(id, type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter input) {
        final var customNameComponent = input.get(DataComponents.CUSTOM_NAME);
        if (customNameComponent != null) {
            customName = customNameComponent;
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.CUSTOM_NAME, customName);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        input.child("ItemHandler").ifPresent(it -> ContainerHelper.loadAllItems(it, container.getItems()));
        hasIceUpgrade = input.getBooleanOr("HasIceUpgrade", false);
        hasPreservationUpgrade = input.getBooleanOr("HasPreservationUpgrade", false);
        customName = input.read("CustomNameV2", ComponentSerialization.CODEC).orElse(null);
        doorAnimator.setForcedOpen(input.getBooleanOr("IsForcedOpen", false));
        doorAnimator.setNumPlayersUsing(input.getByteOr("NumPlayersUsing", (byte) 0));
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
        output.putBoolean("HasIceUpgrade", hasIceUpgrade);
        output.putBoolean("HasPreservationUpgrade", hasPreservationUpgrade);
        output.storeNullable("CustomNameV2", ComponentSerialization.CODEC, customName);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, output -> {
            saveAdditional(output);
            output.putBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
            output.putByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        });
    }

    @Nullable
    public FridgeBlockEntity findNeighbourFridge() {
        final var state = getBlockState();
        final var posBelow = worldPosition.below();
        final var stateBelow = level.getBlockState(posBelow);
        final var posAbove = worldPosition.above();
        final var stateAbove = level.getBlockState(posAbove);
        if (stateAbove.getBlock() == state.getBlock()) {
            return (FridgeBlockEntity) level.getBlockEntity(posAbove);
        } else if (stateBelow.getBlock() == state.getBlock()) {
            return (FridgeBlockEntity) level.getBlockEntity(posBelow);
        }

        return null;
    }

    public FridgeBlockEntity getBaseFridge() {
        if (!hasLevel()) {
            return this;
        }

        final var state = getBlockState();
        final var stateBelow = level.getBlockState(worldPosition.below());
        if (stateBelow.getBlock() == state.getBlock()) {
            FridgeBlockEntity baseFridge = (FridgeBlockEntity) level.getBlockEntity(worldPosition.below());
            if (baseFridge != null) {
                return baseFridge;
            }
        }

        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    public Container getCombinedContainer() {
        FridgeBlockEntity baseFridge = getBaseFridge();
        FridgeBlockEntity neighbourFridge;
        if (baseFridge == this) {
            neighbourFridge = findNeighbourFridge();
        } else {
            neighbourFridge = this;
        }

        if (neighbourFridge != null) {
            return new CombinedContainer(neighbourFridge.container, baseFridge.container);
        }

        return container;
    }

    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-1, 0, -1).getCenter(), worldPosition.offset(2, 2, 2).getCenter());
    }

    public void markDirtyAndUpdate() {
        BlockState state = level.getBlockState(worldPosition);
        level.sendBlockUpdated(worldPosition, state, state, 3);
        setChanged();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new FridgeMenu(i, playerInventory, this);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
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
    public void setCustomName(Component customName) {
        this.customName = customName;
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.fridge");
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
        return worldPosition;
    }

    @Override
    public TransferableContainer snapshotDataForTransfer() {
        return TransferableContainer.copyAndClear(container);
    }

    @Override
    public void restoreFromTransferSnapshot(TransferableContainer data) {
        data.applyTo(container);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        dropItems(level, pos);
        if (hasIceUpgrade()) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, ModItems.iceUnit.createStack());
        }
        if (hasPreservationUpgrade()) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, ModItems.preservationChamber.createStack());
        }
    }

    public record IceUnitIngredientToken(ItemStack itemStack) implements IngredientToken {
        @Override
        public ItemStack peek() {
            return itemStack;
        }

        @Override
        public ItemStack consume() {
            return itemStack;
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

    public void sync() {
        BalmBlockEntityUtils.sync(this);
    }

}
