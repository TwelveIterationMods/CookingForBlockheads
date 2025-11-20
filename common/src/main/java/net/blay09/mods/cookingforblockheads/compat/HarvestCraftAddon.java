package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.platform.event.callback.BlockCallback;
import net.blay09.mods.balm.platform.event.callback.ItemCallback;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class HarvestCraftAddon {

    private boolean cuttingBoardFound;

    public HarvestCraftAddon() {
        final var cuttingBoardItem = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(Compat.HARVESTCRAFT_FOOD_CORE, "cuttingboarditem"));
        if (cuttingBoardItem != null && cuttingBoardItem != Items.AIR) {
            cuttingBoardFound = true;
        }

        ItemCallback.Tooltip.EVENT.register((itemStack, tooltip, flags) -> {
            if (!cuttingBoardFound) {
                return;
            }

            if (itemStack.getItem() == cuttingBoardItem) {
                tooltip.add(Component.translatable("tooltip.cookingforblockheads.multiblock_kitchen").withStyle(ChatFormatting.YELLOW));
                tooltip.add(Component.translatable("tooltip.cookingforblockheads.can_be_placed_in_world"));
            }
        });

        BlockCallback.Use.EVENT.register((player, level, hand, hitResult) -> {
            if (!cuttingBoardFound) {
                return InteractionResult.PASS;
            }

            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.getItem() != cuttingBoardItem) {
                return InteractionResult.PASS;
            }

            Direction face = hitResult.getDirection();
            if (face != Direction.UP) {
                return InteractionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            BlockState clickedBlock = level.getBlockState(pos);
            if (clickedBlock.getBlock() == Blocks.CHEST || clickedBlock.getBlock() == Blocks.CRAFTING_TABLE || clickedBlock.getBlock() == ModBlocks.cuttingBoard) {
                return InteractionResult.PASS;
            }

            BlockPos relativePos = pos.relative(face);
            if (canPlace(player, ModBlocks.cuttingBoard.defaultBlockState(), level, relativePos)) {
                BlockPlaceContext useContext = new BlockPlaceContext(new UseOnContext(player, hand, new BlockHitResult(Vec3.atLowerCornerOf(relativePos), face, relativePos, true)));
                BlockState placedState = ModBlocks.cuttingBoard.value().getStateForPlacement(useContext);
                level.setBlockAndUpdate(relativePos, placedState);
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }

                player.swing(hand);
                player.playSound(SoundEvents.WOOD_PLACE, 1f, 1f);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        });
    }

    private boolean canPlace(Player player, BlockState state, Level level, BlockPos pos) {
        CollisionContext context = CollisionContext.of(player);
        return state.canSurvive(level, pos) && level.isUnobstructed(state, pos, context);
    }

}
