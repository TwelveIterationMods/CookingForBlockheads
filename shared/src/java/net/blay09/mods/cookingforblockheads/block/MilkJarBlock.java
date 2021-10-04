package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.tile.MilkJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MilkJarBlock extends BlockKitchen implements BucketPickup {

    public static final String name = "milk_jar";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    private static final VoxelShape SHAPE = Block.box(4.8, 0, 4.8, 11.2, 8.0, 11.2);

    public MilkJarBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.6f), registryName);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (shouldBlockRenderLowered(world, pos)) {
            return SHAPE.move(0, -0.05, 0);
        }

        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        MilkJarBlockEntity milkJar = (MilkJarBlockEntity) level.getBlockEntity(pos);
        if (!heldItem.isEmpty() && milkJar != null) {
            FluidTank milkTank = milkJar.getFluidTank();
            if (heldItem.getItem() == Items.MILK_BUCKET) {
                if (milkTank.getAmount() <= milkTank.getCapacity() - 1000) {
                    milkTank.fill(Compat.getMilkFluid(), 1000, false);
                    if (!player.getAbilities().instabuild) {
                        player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                    }
                }
                return InteractionResult.SUCCESS;
            } else if (heldItem.getItem() == Items.BUCKET) {
                if (milkTank.getAmount() >= 1000) {
                    if (heldItem.getCount() == 1) {
                        milkTank.drain(Compat.getMilkFluid(), 1000, false);
                        if (!player.getAbilities().instabuild) {
                            player.setItemInHand(hand, new ItemStack(Items.MILK_BUCKET));
                        }
                    } else {
                        if (player.getInventory().add(new ItemStack(Items.MILK_BUCKET))) {
                            milkTank.drain(Compat.getMilkFluid(), 1000, false);
                            if (!player.getAbilities().instabuild) {
                                heldItem.shrink(1);
                            }
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MilkJarBlockEntity(pos, state);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState blockState) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MilkJarBlockEntity milkJar && ((MilkJarBlockEntity) blockEntity).getFluidTank().getAmount() >= 1000) {
            int drained = milkJar.getFluidTank().drain(Compat.getMilkFluid(), 1000, false);
            return drained >= 1000 ? new ItemStack(Items.MILK_BUCKET) : ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}
