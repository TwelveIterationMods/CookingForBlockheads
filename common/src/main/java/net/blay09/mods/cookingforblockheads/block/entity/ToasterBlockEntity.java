package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.DefaultContainer;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.component.ModDataComponents;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.sound.ModSounds;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.ToasterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class ToasterBlockEntity extends BlockEntity {

    private static final int UPDATE_INTERVAL = 20;
    private static final int TOAST_TICKS = 1200;

    private final DefaultContainer container = new DefaultContainer(2) {
        @Override
        public void setChanged() {
            ToasterBlockEntity.this.setChanged();
            BalmBlockEntityUtils.sync(ToasterBlockEntity.this);
        }
    };

    private boolean isDirty;
    private int ticksSinceUpdate;
    private boolean active;
    private int toastTicks;

    public ToasterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.toaster.value(), pos, state);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 0) {
            level.playSound(null, worldPosition, ModSounds.toasterStart.value(), SoundSource.BLOCKS, 1f, 1f);
            return true;
        } else if (id == 1) {
            level.playSound(null, worldPosition, ModSounds.toasterStop.value(), SoundSource.BLOCKS, 1f, 1f);
            return true;
        } else if (id == 2) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            return true;
        }
        return super.triggerEvent(id, type);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        input.child("ItemHandler").ifPresent(it -> ContainerHelper.loadAllItems(it, container.getItems()));
        active = input.getBooleanOr("Active", false);
        toastTicks = input.getIntOr("ToastTicks", 0);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
        output.putBoolean("Active", active);
        output.putInt("ToastTicks", toastTicks);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ToasterBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (active) {
            toastTicks--;
            if (toastTicks <= 0 && !level.isClientSide()) {
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack inputStack = container.getItem(i);
                    if (!inputStack.isEmpty()) {
                        final var outputStack = toastItem(inputStack);
                        final var itemEntity = new ItemEntity(level,
                                worldPosition.getX() + 0.5f,
                                worldPosition.getY() + 0.75f,
                                worldPosition.getZ() + 0.5f,
                                outputStack);
                        itemEntity.setDeltaMovement(0f, 0.1f, 0f);
                        level.addFreshEntity(itemEntity);
                        container.setItem(i, ItemStack.EMPTY);
                    }
                }
                setActive(false);
            }
            isDirty = true;
        }

        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            BalmBlockEntityUtils.sync(this);
            ticksSinceUpdate = 0;
            isDirty = false;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            toastTicks = TOAST_TICKS;
            level.blockEvent(worldPosition, ModBlocks.toaster.value(), 0, 0);
        } else {
            toastTicks = 0;
            level.blockEvent(worldPosition, ModBlocks.toaster.value(), 1, 0);
        }

        level.blockEvent(worldPosition, ModBlocks.toaster.value(), 2, 0);

        BlockState state = level.getBlockState(worldPosition);
        level.setBlockAndUpdate(worldPosition, state.setValue(ToasterBlock.ACTIVE, active));
        isDirty = true;
        setChanged();
    }

    public boolean isActive() {
        return active;
    }

    public float getToastProgress() {
        return 1f - toastTicks / (float) TOAST_TICKS;
    }

    public Container getContainer() {
        return container;
    }

    public boolean isBurningToast() {
        final var firstToasted = container.getItem(0).has(ModDataComponents.toasted.value());
        final var secondToasted = container.getItem(1).has(ModDataComponents.toasted.value());
        return firstToasted || secondToasted;
    }

    private ItemStack toastItem(ItemStack itemStack) {
        final var recipeInput = new SingleRecipeInput(itemStack);
        final var toastRecipe = ModRecipes.toasterRecipes.getRecipeFor(level, recipeInput);
        final var outputItem = toastRecipe.map(recipeHolder -> recipeHolder.value().assemble(recipeInput)).orElse(itemStack);
        if (outputItem.is(Items.BREAD)) {
            return toastBread(outputItem);
        } else {
            return outputItem;
        }
    }

    private ItemStack toastBread(ItemStack itemStack) {
        boolean alreadyToasted = itemStack.has(ModDataComponents.toasted.value());
        if (alreadyToasted) {
            if (CookingForBlockheadsConfig.getActive().allowVeryToastedBread) {
                ItemStack veryToasted = new ItemStack(Items.CHARCOAL);
                veryToasted.set(DataComponents.CUSTOM_NAME, Component.translatable("tooltip.cookingforblockheads.very_toasted"));
                return veryToasted;
            } else {
                return itemStack;
            }
        } else {
            ItemStack toasted = itemStack.copy();
            toasted.set(DataComponents.CUSTOM_NAME, Component.translatable("tooltip.cookingforblockheads.toasted", itemStack.getHoverName()));
            toasted.set(ModDataComponents.toasted.value(), Unit.INSTANCE);
            return toasted;
        }
    }

    public boolean canToast(ItemStack itemStack) {
        final var server = level.getServer();
        if (server == null) {
            return false;
        }

        return ModRecipes.toasterRecipes.getRecipeFor(level, new SingleRecipeInput(itemStack))
                .map(it -> true)
                .orElseGet(() -> itemStack.is(Items.BREAD));
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

}
