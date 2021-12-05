package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.cookingforblockheads.ModSounds;
import net.blay09.mods.cookingforblockheads.api.ToasterHandler;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.ToasterBlock;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ToasterBlockEntity extends BalmBlockEntity {

    private static final int TOAST_TICKS = 1200;

    private final DefaultContainer container = new DefaultContainer(2) {
        @Override
        public void setChanged() {
            ToasterBlockEntity.this.setChanged();
            balmSync();
        }
    };

    private boolean active;
    private int toastTicks;

    public ToasterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.toaster.get(), pos, state);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 0) {
            level.playSound(null, worldPosition, ModSounds.toasterStart.get(), SoundSource.BLOCKS, 1f, 1f);
            return true;
        } else if (id == 1) {
            level.playSound(null, worldPosition, ModSounds.toasterStop.get(), SoundSource.BLOCKS, 1f, 1f);
            return true;
        } else if (id == 2) {
            BlockState state = level.getBlockState(worldPosition);
            level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), state, state, 3, 512);
            return true;
        }
        return super.triggerEvent(id, type);
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        container.deserialize(tagCompound.getCompound("ItemHandler"));
        active = tagCompound.getBoolean("Active");
        toastTicks = tagCompound.getInt("ToastTicks");
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", container.serialize());
        tagCompound.putBoolean("Active", active);
        tagCompound.putInt("ToastTicks", toastTicks);
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, ToasterBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (active) {
            toastTicks--;
            if (toastTicks <= 0 && !level.isClientSide) {
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack inputStack = container.getItem(i);
                    if (!inputStack.isEmpty()) {
                        ToasterHandler toastHandler = CookingRegistry.getToasterHandler(inputStack);
                        ItemStack outputStack = toastHandler.getToasterOutput(inputStack);
                        if (outputStack.isEmpty()) {
                            outputStack = inputStack;
                        } else {
                            outputStack = outputStack.copy();
                        }
                        ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5f, worldPosition.getY() + 0.75f, worldPosition.getZ() + 0.5f, outputStack);
                        itemEntity.setDeltaMovement(0f, 0.1f, 0f);
                        level.addFreshEntity(itemEntity);
                        container.setItem(i, ItemStack.EMPTY);
                    }
                }
                setActive(false);
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            toastTicks = TOAST_TICKS;
            level.blockEvent(worldPosition, ModBlocks.toaster, 0, 0);
        } else {
            toastTicks = 0;
            level.blockEvent(worldPosition, ModBlocks.toaster, 1, 0);
        }

        level.blockEvent(worldPosition, ModBlocks.toaster, 2, 0);

        BlockState state = level.getBlockState(worldPosition);
        level.setBlockAndUpdate(worldPosition, state.setValue(ToasterBlock.ACTIVE, active));
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
}
