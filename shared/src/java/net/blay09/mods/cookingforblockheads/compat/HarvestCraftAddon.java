//package net.blay09.mods.cookingforblockheads.compat; TODO
//
//import net.blay09.mods.balm.api.Balm;
//import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
//import net.blay09.mods.cookingforblockheads.block.ModBlocks;
//import net.blay09.mods.cookingforblockheads.util.TextUtils;
//import net.minecraft.ChatFormatting;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.network.chat.TranslatableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.item.context.UseOnContext;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraft.world.phys.shapes.CollisionContext;
//
//public class HarvestCraftAddon {
//
//    private boolean cuttingBoardFound;
//
//    public HarvestCraftAddon() {
//        Compat.cuttingBoardItem = Balm.getRegistries().getItem(new ResourceLocation(Compat.HARVESTCRAFT_FOOD_CORE, "cuttingboarditem"));
//        if (Compat.cuttingBoardItem != null && Compat.cuttingBoardItem != Items.AIR) {
//            CookingForBlockheads.extraItemGroupItems.add(new ItemStack(Compat.cuttingBoardItem));
//            cuttingBoardFound = true;
//        }
//    }
//
//    public void onItemTooltip(ItemTooltipEvent event) {
//        if (!cuttingBoardFound) {
//            return;
//        }
//
//        if (event.getItemStack().getItem() == Compat.cuttingBoardItem) {
//            event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:multiblock_kitchen", ChatFormatting.YELLOW));
//            event.getToolTip().add(new TranslatableComponent("tooltip.cookingforblockheads:can_be_placed_in_world"));
//        }
//    }
//
//    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
//        if (!cuttingBoardFound) {
//            return;
//        }
//
//        if (event.getItemStack().getItem() != Compat.cuttingBoardItem) {
//            return;
//        }
//
//        if (event.getFace() != Direction.UP) {
//            return;
//        }
//
//        Level level = event.getLevel();
//        Player player = event.getPlayer();
//        BlockState clickedBlock = level.getBlockState(event.getPos());
//        if (clickedBlock.getBlock() == Blocks.CHEST || clickedBlock.getBlock() == Blocks.CRAFTING_TABLE || clickedBlock.getBlock() == ModBlocks.cuttingBoard) {
//            return;
//        }
//
//        BlockPos pos = event.getPos().offset(event.getFace());
//        if (canPlace(player, ModBlocks.cuttingBoard.defaultBlockState(), level, pos)) {
//            BlockPlaceContext useContext = new BlockPlaceContext(new UseOnContext(player, event.getHand(), new BlockHitResult(Vec3.atLowerCornerOf(pos), event.getFace(), pos, true)));
//            BlockState placedState = ModBlocks.cuttingBoard.getStateForPlacement(useContext);
//            level.setBlockAndUpdate(pos, placedState);
//            if (!player.getAbilities().instabuild) {
//                event.getItemStack().shrink(1);
//            }
//
//            player.swing(event.getHand());
//            player.playSound(SoundEvents.WOOD_PLACE, 1f, 1f);
//            event.setCancellationResult(InteractionResult.SUCCESS);
//            event.setCanceled(true);
//        }
//    }
//
//    private boolean canPlace(Player player, BlockState state, Level level, BlockPos pos) {
//        CollisionContext context = CollisionContext.of(player);
//        return state.canSurvive(level, pos) && level.isUnobstructed(state, pos, context);
//    }
//
//}
