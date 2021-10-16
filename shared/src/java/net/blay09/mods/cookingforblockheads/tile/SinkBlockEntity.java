package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.AbstractKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class SinkBlockEntity extends BalmBlockEntity implements BalmFluidTankProvider {

    private static final int SYNC_INTERVAL = 10;

    private class SinkItemProvider extends AbstractKitchenItemProvider {
        private final NonNullList<ItemStack> itemStacks = NonNullList.create();
        private final FluidTank fluidTank;
        private int waterUsed;

        public SinkItemProvider(FluidTank fluidTank) {
            this.fluidTank = fluidTank;
            itemStacks.addAll(CookingRegistry.getWaterItems());
        }

        @Override
        public void resetSimulation() {
            waterUsed = 0;
        }

        @Override
        public int getSimulatedUseCount(int slot) {
            return waterUsed / 1000;
        }

        @Override
        public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
            int availableAmount = fluidTank.getAmount();
            if(simulate) {
                availableAmount -= waterUsed;
            }
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater || availableAmount >= amount * 1000) {
                if (requireBucket && itemStacks.get(slot).getItem() == Items.MILK_BUCKET) {
                    if (!CookingRegistry.consumeBucket(inventories, simulate)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (simulate) {
                    waterUsed += amount * 1000;
                } else {
                    fluidTank.drain(Fluids.WATER, amount * 1000, false);
                    setChanged();
                }
                return ContainerUtils.copyStackWithSize(itemStacks.get(slot), amount);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
            for (ItemStack providedStack : itemStacks) {
                if (Balm.getHooks().canItemsStack(itemStack, providedStack)) {
                    fluidTank.fill(Fluids.WATER, 1000, false);
                    break;
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public int getSlots() {
            return itemStacks.size();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            if (CookingForBlockheadsConfig.getActive().sinkRequiresWater && fluidTank.getAmount() - waterUsed < 1000) {
                return ItemStack.EMPTY;
            }

            return itemStacks.get(slot);
        }
    }

    private final FluidTank sinkTank = new FluidTank(16000) {

        @Override
        public Fluid getFluid() {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return Fluids.WATER;
            }

            return super.getFluid();
        }

        @Override
        public int getAmount() {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return Integer.MAX_VALUE;
            }

            return super.getAmount();
        }

        @Override
        public int getCapacity() {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return Integer.MAX_VALUE;
            }

            return super.getCapacity();
        }

        @Override
        public int drain(Fluid fluid, int maxDrain, boolean simulate) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater && fluid == Fluids.WATER) {
                return maxDrain;
            }

            if (fluid.isSame(Fluids.EMPTY) || !fluid.isSame(fluid))
            {
                return 0;
            }

            SinkBlockEntity.this.setChanged();

            return super.drain(fluid, maxDrain, simulate);
        }

        @Override
        public int fill(Fluid fluid, int maxFill, boolean simulate) {
            if (!CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                return maxFill;
            }

            SinkBlockEntity.this.setChanged();

            return super.fill(fluid, maxFill, simulate);
        }
    };

    private final SinkItemProvider itemProvider = new SinkItemProvider(sinkTank);

    private int ticksSinceSync;
    private boolean isDirty;

    private DyeColor color = DyeColor.WHITE;

    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.sink.get(), pos, state);
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("FluidTank", sinkTank.serialize());
        tagCompound.putByte("Color", (byte) color.getId());
        return tagCompound;
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        sinkTank.deserialize(tagCompound.getCompound("FluidTank"));
        color = DyeColor.byId(tagCompound.getByte("Color"));
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, SinkBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        // Sync to clients
        ticksSinceSync++;
        if (ticksSinceSync >= SYNC_INTERVAL) {
            ticksSinceSync = 0;
            if (isDirty) {
                balmSync();
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
}
