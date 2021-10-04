package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmContainerBlock;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.tile.CounterBlockEntity;
import net.blay09.mods.cookingforblockheads.tile.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.tile.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class KitchenCounterBlock extends BlockDyeableKitchen implements BalmContainerBlock {

    public static final String name = "counter";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public KitchenCounterBlock() {
        this(registryName);
    }

    public KitchenCounterBlock(ResourceLocation registryName) {
        super(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(5f, 10f), registryName);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED, COLOR, HAS_COLOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CounterBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (tryRecolorBlock(state, heldItem, level, pos, player, rayTraceResult)) {
            return InteractionResult.SUCCESS;
        }

        CounterBlockEntity counter = (CounterBlockEntity) level.getBlockEntity(pos);
        if (rayTraceResult.getDirection() == state.getValue(FACING)) {
            if (counter != null) {
                if (player.isShiftKeyDown()) {
                    counter.getDoorAnimator().toggleForcedOpen();
                    return InteractionResult.SUCCESS;
                } else if (!heldItem.isEmpty() && counter.getDoorAnimator().isForcedOpen()) {
                    heldItem = counter.insertItemStacked(heldItem, false);
                    player.setItemInHand(hand, heldItem);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (!level.isClientSide) {
            if (rayTraceResult.getDirection() == Direction.UP && !heldItem.isEmpty() && (heldItem.getItem() instanceof BlockItem || heldItem.getItem() == Compat.cuttingBoardItem)) {
                return InteractionResult.FAIL;
            }

            Balm.getNetworking().openGui(player, counter);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(FLIPPED, shouldBePlacedFlipped(context, state.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.counter.get(), CounterBlockEntity::serverTick);
    }
}
