package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.UseBlockEvent;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
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
        Compat.cuttingBoardItem = Balm.getRegistries().getItem(new ResourceLocation(Compat.HARVESTCRAFT_FOOD_CORE, "cuttingboarditem"));
        if (Compat.cuttingBoardItem != null && Compat.cuttingBoardItem != Items.AIR) {
            cuttingBoardFound = true;
        }

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            if (!cuttingBoardFound) {
                return;
            }

            if (event.getItemStack().getItem() == Compat.cuttingBoardItem) {
                event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:multiblock_kitchen", ChatFormatting.YELLOW));
                event.getToolTip().add(new TranslatableComponent("tooltip.cookingforblockheads:can_be_placed_in_world"));
            }
        });

        Balm.getEvents().onEvent(UseBlockEvent.class, event -> {
            if (!cuttingBoardFound) {
                return;
            }

            ItemStack heldItem = event.getPlayer().getItemInHand(event.getHand());
            if (heldItem.getItem() != Compat.cuttingBoardItem) {
                return;
            }

            Direction face = event.getHitResult().getDirection();
            if (face != Direction.UP) {
                return;
            }

            Level level = event.getLevel();
            Player player = event.getPlayer();
            BlockPos pos = event.getHitResult().getBlockPos();
            BlockState clickedBlock = level.getBlockState(pos);
            if (clickedBlock.getBlock() == Blocks.CHEST || clickedBlock.getBlock() == Blocks.CRAFTING_TABLE || clickedBlock.getBlock() == ModBlocks.cuttingBoard) {
                return;
            }

            BlockPos relativePos = pos.relative(face);
            if (canPlace(player, ModBlocks.cuttingBoard.defaultBlockState(), level, relativePos)) {
                BlockPlaceContext useContext = new BlockPlaceContext(new UseOnContext(player, event.getHand(), new BlockHitResult(Vec3.atLowerCornerOf(relativePos), face, relativePos, true)));
                BlockState placedState = ModBlocks.cuttingBoard.getStateForPlacement(useContext);
                level.setBlockAndUpdate(relativePos, placedState);
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }

                player.swing(event.getHand());
                player.playSound(SoundEvents.WOOD_PLACE, 1f, 1f);
                event.setResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        });
    }

    private boolean canPlace(Player player, BlockState state, Level level, BlockPos pos) {
        CollisionContext context = CollisionContext.of(player);
        return state.canSurvive(level, pos) && level.isUnobstructed(state, pos, context);
    }

}
