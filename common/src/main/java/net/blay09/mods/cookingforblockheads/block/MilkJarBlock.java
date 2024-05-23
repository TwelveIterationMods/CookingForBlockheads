package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.block.entity.MilkJarBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MilkJarBlock extends BaseKitchenBlock implements BucketPickup {

    public static final MapCodec<MilkJarBlock> CODEC = simpleCodec(MilkJarBlock::new);

    private static final VoxelShape SHAPE = Block.box(4.8, 0, 4.8, 11.2, 8.0, 11.2);

    public MilkJarBlock(Properties properties) {
        super(properties.sound(SoundType.GLASS).strength(0.6f));
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
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        
        final var blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof MilkJarBlockEntity milkJar)) {
            return ItemInteractionResult.FAIL;
        }

        FluidTank milkTank = milkJar.getFluidTank();
        if (itemStack.getItem() == Items.MILK_BUCKET) {
            if (milkTank.getAmount() <= milkTank.getCapacity() - 1000) {
                milkTank.fill(Compat.getMilkFluid(), 1000, false);
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
            }
            return ItemInteractionResult.SUCCESS;
        } else if (itemStack.getItem() == Items.BUCKET) {
            if (milkTank.getAmount() >= 1000) {
                if (itemStack.getCount() == 1) {
                    milkTank.drain(Compat.getMilkFluid(), 1000, false);
                    if (!player.getAbilities().instabuild) {
                        player.setItemInHand(hand, new ItemStack(Items.MILK_BUCKET));
                    }
                } else {
                    if (player.getInventory().add(new ItemStack(Items.MILK_BUCKET))) {
                        milkTank.drain(Compat.getMilkFluid(), 1000, false);
                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }
                    }
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, blockHitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MilkJarBlockEntity(pos, state);
    }

    @Override
    public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState blockState) {
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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }


    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.milk_jar.description").withStyle(ChatFormatting.GRAY));
    }
}
