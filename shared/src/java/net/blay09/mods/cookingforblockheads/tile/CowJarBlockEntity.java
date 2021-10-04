package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);

        if (tagCompound.contains("CustomName", Tag.TAG_STRING)) {
            customName = Component.Serializer.fromJson(tagCompound.getString("CustomName"));
        }

        compressedCow = tagCompound.getBoolean("CompressedCow");
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        if (customName != null) {
            tagCompound.putString("CustomName", Component.Serializer.toJson(customName));
        }

        tagCompound.putBoolean("CompressedCow", compressedCow);

        return super.save(tagCompound);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CowJarBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (milkAmount < MILK_CAPACITY) {
            double milkToAdd = CookingForBlockheadsConfig.getActive().cowJarMilkPerTick;
            if (compressedCow) {
                milkToAdd *= CookingForBlockheadsConfig.getActive().compressedCowJarMilkMultiplier;
            }

            milkAmount += milkToAdd;
            isDirty = true;
        }

        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            balmSync();
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
            return new TranslatableComponent("container.cookingforblockheads.cow_jar_compressed");
        }

        return new TranslatableComponent("container.cookingforblockheads.cow_jar");
    }
}
