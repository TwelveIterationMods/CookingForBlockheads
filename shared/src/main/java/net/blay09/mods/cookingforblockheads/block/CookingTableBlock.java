package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.block.entity.CookingTableBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.cooking_table.description").withStyle(ChatFormatting.GRAY));
    }
}
