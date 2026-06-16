package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CowJarBlockEntity extends MilkJarBlockEntity implements IMutableNameable {

    private static final int UPDATE_INTERVAL = 20;

    private boolean isDirty;
    private int ticksSinceUpdate;

    private @Nullable Component customName;
    private @Nullable Holder<CowVariant> variant;
    private boolean compressedCow;

    private @Nullable BlockPos jukebox;
    private int partyBpm;

    public CowJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cowJar.value(), pos, state);
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
        super.loadAdditional(input);
        customName = input.read("CustomNameV2", ComponentSerialization.CODEC).orElse(null);
        variant = input.read("Variant", CowVariant.CODEC).orElse(null);
        compressedCow = input.getBooleanOr("CompressedCow", false);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("CustomNameV2", ComponentSerialization.CODEC, customName);
        output.storeNullable("Variant", CowVariant.CODEC, variant);
        output.putBoolean("CompressedCow", compressedCow);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CowJarBlockEntity blockEntity) {
        final var jukebox = blockEntity.jukebox;
        if (jukebox == null || !jukebox.closerToCenterThan(Vec3.atCenterOf(pos), 3.46f) || !level.getBlockState(jukebox).is(Blocks.JUKEBOX)) {
            blockEntity.partyBpm = 0;
            blockEntity.jukebox = null;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CowJarBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (milkTank.getAmount(0) < MILK_CAPACITY) {
            CookingForBlockheadsConfig config = CookingForBlockheadsConfig.getActive();

            double milkToAdd = config.cowJarMilkPerTick;
            if (compressedCow) {
                milkToAdd *= config.compressedCowJarMilkMultiplier;
            }

            milkTank.fill(0, Compat.getMilkFluid(), (int) milkToAdd, false);
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
    public @Nullable Component getCustomName() {
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

    public @Nullable Holder<CowVariant> getVariant() {
        return variant;
    }

    public void setVariant(@Nullable Holder<CowVariant> variant) {
        this.variant = variant;
    }

    public boolean isPartying() {
        return partyBpm > 0;
    }

    public void setRecordPlayingNearby(BlockPos pos, boolean playing) {
        this.jukebox = pos;
        this.partyBpm = playing ? 85 : 0;
    }

}
