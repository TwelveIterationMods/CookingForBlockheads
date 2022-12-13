package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.AbstractKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class MilkJarBlockEntity extends BalmBlockEntity implements BalmFluidTankProvider {

    protected static final int MILK_CAPACITY = 32000;

    private static class MilkJarItemProvider extends AbstractKitchenItemProvider {
        private final NonNullList<ItemStack> itemStacks = NonNullList.create();
        private final MilkJarBlockEntity milkJar;
        private int milkUsed;

        public MilkJarItemProvider(MilkJarBlockEntity milkJar) {
            this.milkJar = milkJar;
            itemStacks.addAll(CookingRegistry.getMilkItems());
        }

        @Override
        public void resetSimulation() {
            milkUsed = 0;
        }

        @Override
        public int getSimulatedUseCount(int slot) {
            return milkUsed / 1000;
        }

        @Override
        public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
            if (milkJar.getFluidTank().getAmount() - milkUsed >= amount * 1000) {
                if (requireBucket && itemStacks.get(slot).getItem() == Items.MILK_BUCKET) {
                    if (!CookingRegistry.consumeBucket(inventories, simulate)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (simulate) {
                    milkUsed += amount * 1000;
                } else {
                    milkJar.getFluidTank().drain(Compat.getMilkFluid(), amount * 1000, false);
                }
                return ContainerUtils.copyStackWithSize(itemStacks.get(slot), amount);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
            for (ItemStack providedStack : itemStacks) {
                if (Balm.getHooks().canItemsStack(itemStack, providedStack)) {
                    milkJar.getFluidTank().fill(Compat.getMilkFluid(), 1000, false);
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
            if (milkJar.getFluidTank().getAmount() - milkUsed < 1000) {
                return ItemStack.EMPTY;
            }

            return itemStacks.get(slot);
        }

        @Override
        public int getCountInSlot(int slot) {
            return milkJar.getFluidTank().getAmount() / 1000;
        }
    }

    private final MilkJarItemProvider itemProvider = new MilkJarItemProvider(this);
    protected final FluidTank milkTank = new FluidTank(MILK_CAPACITY) {
        @Override
        public boolean canFill(Fluid fluid) {
            return fluid.isSame(Compat.getMilkFluid()) && super.canFill(fluid);
        }

        @Override
        public void setChanged() {
            MilkJarBlockEntity.this.setChanged();
        }
    };

    public MilkJarBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.milkJar.get(), pos, state);
    }

    protected MilkJarBlockEntity(BlockEntityType<? extends MilkJarBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("FluidTank", milkTank.serialize());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        milkTank.deserialize(tag.getCompound("FluidTank"));
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    @Override
    public FluidTank getFluidTank() {
        return milkTank;
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(IKitchenItemProvider.class, itemProvider));
    }

}
