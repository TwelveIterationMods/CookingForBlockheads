package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfigData;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CowJarBlockEntity extends MilkJarBlockEntity implements IMutableNameable {

    private static final int UPDATE_INTERVAL = 20;

    private boolean isDirty;
    private int ticksSinceUpdate;

    private Component customName;
    private boolean compressedCow;

    public CowJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cowJar.get(), pos, state);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
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
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (tag.contains("CustomName", Tag.TAG_STRING)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"), provider);
        }

        compressedCow = tag.getBoolean("CompressedCow");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName, provider));
        }

        tag.putBoolean("CompressedCow", compressedCow);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CowJarBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (milkTank.getAmount() < MILK_CAPACITY) {
            CookingForBlockheadsConfigData config = CookingForBlockheadsConfig.getActive();

            int milkToAdd = config.cowJarMilkPerTick;
            if (compressedCow) {
                milkToAdd *= config.compressedCowJarMilkMultiplier;
            }

            milkTank.fill(Compat.getMilkFluid(), milkToAdd, false);
            isDirty = true;
        }

        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            sync();
            ticksSinceUpdate = 0;
            isDirty = false;
        }
    }

    public boolean isCompressedCow() {
        return compressedCow;
    }

    public void setCompressedCow(boolean compressedCow) {
        this.compressedCow = compressedCow;
    }

    @Override
    public void setCustomName(Component customName) {
        this.customName = customName;
        setChanged();
    }

    @Override
    public Component getCustomName() {
        return customName;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public Component getDefaultName() {
        if (compressedCow) {
            return Component.translatable("container.cookingforblockheads.cow_jar_compressed");
        }

        return Component.translatable("container.cookingforblockheads.cow_jar");
    }
}
