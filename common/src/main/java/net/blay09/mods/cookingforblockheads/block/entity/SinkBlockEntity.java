package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.platform.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.platform.fluid.DefaultFluidTank;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.CacheHint;
import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeProvider;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.capability.KitchenRecipeProviderHolder;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class SinkBlockEntity extends BlockEntity implements BalmFluidTankProvider, KitchenItemProviderHolder, KitchenRecipeProviderHolder {

    private static final int SYNC_INTERVAL = 10;
    private static final Identifier SALT_FILTER_SOURCE = id("salt_filter");
    private final SinkItemProvider itemProvider = new SinkItemProvider(this);
    private int ticksSinceSync;
    private boolean isDirty;
    private boolean hasSaltFilter;
    private final KitchenRecipeProvider recipeProvider = () -> hasSaltFilter ? Set.of(SALT_FILTER_SOURCE) : Set.of();
    private final DefaultFluidTank sinkTank = new DefaultFluidTank(16000) {

        @Override
        public Fluid getFluid(int slot) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return Fluids.WATER;
            }

            return super.getFluid(slot);
        }

        @Override
        public int getAmount(int slot) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return Integer.MAX_VALUE;
            }

            return super.getAmount(slot);
        }

        @Override
        public int getCapacity(int slot) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return Integer.MAX_VALUE;
            }

            return super.getCapacity(slot);
        }

        @Override
        public int drain(int slot, Fluid fluid, int maxDrain, boolean simulate) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater && fluid == Fluids.WATER) {
                return maxDrain;
            }

            if (fluid.isSame(Fluids.EMPTY) || !fluid.isSame(fluid)) {
                return 0;
            }

            SinkBlockEntity.this.setChanged();

            return super.drain(slot, fluid, maxDrain, simulate);
        }

        @Override
        public int fill(int slot, Fluid fluid, int maxFill, boolean simulate) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return maxFill;
            }

            SinkBlockEntity.this.setChanged();

            return super.fill(slot, fluid, maxFill, simulate);
        }
    };

    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.sink.value(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SinkBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public boolean hasSaltFilter() {
        return hasSaltFilter;
    }

    public void setHasSaltFilter(boolean hasSaltFilter) {
        this.hasSaltFilter = hasSaltFilter;
        setChanged();
        if (hasLevel() && !level.isClientSide()) {
            BalmBlockEntityUtils.sync(this);
            isDirty = false;
        }
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        sinkTank.serialize(output.child("FluidTank"));
        output.putBoolean("HasSaltFilter", hasSaltFilter);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        input.child("FluidTank").ifPresent(sinkTank::deserialize);
        hasSaltFilter = input.getBooleanOr("HasSaltFilter", false);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }

    @Override
    public KitchenRecipeProvider getKitchenRecipeProvider() {
        return recipeProvider;
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        // Sync to clients
        ticksSinceSync++;
        if (ticksSinceSync >= SYNC_INTERVAL) {
            ticksSinceSync = 0;
            if (isDirty) {
                BalmBlockEntityUtils.sync(this);
                isDirty = false;
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        isDirty = true;
    }

    @Override
    public FluidTank getFluidTank() {
        return sinkTank;
    }

    private record SinkIngredientToken(SinkBlockEntity milkJar, ItemStack itemStack, int count) implements IngredientToken {
        @Override
        public ItemStack peek() {
            final var drained = milkJar.getFluidTank().drain(0, Compat.getMilkFluid(), 1000, true);
            return drained >= 1000 ? itemStack : ItemStack.EMPTY;
        }

        @Override
        public ItemStack consume() {
            final var drained = milkJar.getFluidTank().drain(0, Compat.getMilkFluid(), 1000, false);
            return drained >= 1000 ? itemStack : ItemStack.EMPTY;
        }

        @Override
        public ItemStack restore(ItemStack itemStack) {
            milkJar.getFluidTank().fill(0, Compat.getMilkFluid(), 1000, false);
            return ItemStack.EMPTY;
        }

        @Override
        public int reservedCount() {
            return count;
        }
    }

    private record SinkItemProvider(SinkBlockEntity sink) implements KitchenItemProvider {
        @Override
        public @Nullable IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
            for (final var waterItem : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.WATER))
                if (ingredient.acceptsItem(waterItem)) {
                    final var waterUnitsUsed = ingredientTokens.size();
                    final var waterUnitsAvailable = sink.getFluidTank().getAmount(0) / 1000 - waterUnitsUsed;
                    if (waterUnitsAvailable >= 1) {
                        return new SinkIngredientToken(sink, new ItemStack(waterItem), greedy ? waterUnitsAvailable : 1);
                    } else {
                        return null;
                    }
                }

            return null;
        }

        @Override
        public @Nullable IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens, CacheHint cacheHint, boolean greedy) {
            if (!itemStack.is(ModItemTags.WATER)) {
                return null;
            }

            final var waterUnitsUsed = ingredientTokens.size();
            final var waterUnitsAvailable = sink.getFluidTank().getAmount(0) / 1000 - waterUnitsUsed;
            if (waterUnitsAvailable >= 1) {
                return new SinkIngredientToken(sink, itemStack, greedy ? waterUnitsAvailable : 1);
            } else {
                return null;
            }
        }

        @Override
        public CacheHint getCacheHint(IngredientToken ingredientToken) {
            return CacheHint.NONE;
        }
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }
}
