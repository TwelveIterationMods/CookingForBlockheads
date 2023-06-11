package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.CookingTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CookingTableBlock extends BlockDyeableKitchen {

    public static final String name = "cooking_table";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public CookingTableBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2.5f), registryName);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR, HAS_COLOR);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        CookingTableBlockEntity blockEntity = (CookingTableBlockEntity) level.getBlockEntity(pos);
        if (!heldItem.isEmpty()) {
            if (blockEntity != null) {
                if (tryRecolorBlock(state, heldItem, level, pos, player, rayTraceResult)) {
                    return InteractionResult.SUCCESS;
                }

                if (!blockEntity.hasNoFilterBook() && heldItem.getItem() == ModItems.noFilterBook) {
                    blockEntity.setNoFilterBook(heldItem.split(1));
                    return InteractionResult.SUCCESS;
                }
            }
        } else if (player.isShiftKeyDown()) {
            if (blockEntity != null) {
                ItemStack noFilterBook = blockEntity.getNoFilterBook();
                if (!noFilterBook.isEmpty()) {
                    if (!player.getInventory().add(noFilterBook)) {
                        player.drop(noFilterBook, false);
                    }
                    blockEntity.setNoFilterBook(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (!level.isClientSide) {
            Balm.getNetworking().openGui(player, blockEntity);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        CookingTableBlockEntity tileEntity = (CookingTableBlockEntity) level.getBlockEntity(pos);
        if (tileEntity != null && !state.is(newState.getBlock())) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tileEntity.getNoFilterBook());
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CookingTableBlockEntity(pos, state);
    }

}
