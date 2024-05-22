package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.block.entity.CookingTableBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CookingTableBlock extends BaseKitchenBlock {

    public static final MapCodec<CookingTableBlock> CODEC = simpleCodec(CookingTableBlock::new);

    public CookingTableBlock(Properties properties) {
        super(properties.sound(SoundType.STONE).strength(2.5f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR, HAS_COLOR);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        final var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingTableBlockEntity cookingTable) {
            if (player.isShiftKeyDown()) {
                ItemStack noFilterBook = cookingTable.getNoFilterBook();
                if (!noFilterBook.isEmpty()) {
                    if (!player.getInventory().add(noFilterBook)) {
                        player.drop(noFilterBook, false);
                    }
                    cookingTable.setNoFilterBook(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }

            if (!level.isClientSide) {
                Balm.getNetworking().openGui(player, cookingTable);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        final var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingTableBlockEntity cookingTable) {
            if (tryRecolorBlock(state, itemStack, level, pos, player, blockHitResult)) {
                return ItemInteractionResult.SUCCESS;
            }

            if (!cookingTable.hasNoFilterBook() && itemStack.getItem() == ModItems.noFilterBook) {
                cookingTable.setNoFilterBook(itemStack.split(1));
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.cooking_table.description").withStyle(ChatFormatting.GRAY));
    }
}
