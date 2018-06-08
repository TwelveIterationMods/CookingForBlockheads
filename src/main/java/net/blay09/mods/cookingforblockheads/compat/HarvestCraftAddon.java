package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HarvestCraftAddon {

    public HarvestCraftAddon() {
        MinecraftForge.EVENT_BUS.register(this);
        Compat.cuttingBoardItem = Item.REGISTRY.getObject(new ResourceLocation(Compat.PAMS_HARVESTCRAFT, "cuttingboarditem"));
        if (Compat.cuttingBoardItem != null) {
            CookingForBlockheads.extraCreativeTabItems.add(new ItemStack(Compat.cuttingBoardItem));
        }
    }

    public static boolean isWeirdConversionRecipe(IRecipe recipe) {
        if (recipe.getIngredients().size() == 2 && recipe.getRecipeOutput().getCount() == 2) {
            Ingredient first = recipe.getIngredients().get(0);
            Ingredient second = recipe.getIngredients().get(1);
            if (first.apply(recipe.getRecipeOutput()) && second.apply(recipe.getRecipeOutput())) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == Compat.cuttingBoardItem) {
            event.getToolTip().add(TextFormatting.YELLOW + I18n.format("tooltip.cookingforblockheads:multiblock_kitchen"));
            event.getToolTip().add(I18n.format("tooltip.cookingforblockheads:can_be_placed_in_world"));
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() != Compat.cuttingBoardItem) {
            return;
        }

        if (event.getFace() != EnumFacing.UP) {
            return;
        }

        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        IBlockState clickedBlock = world.getBlockState(event.getPos());
        if (clickedBlock.getBlock() == Blocks.CHEST || clickedBlock.getBlock() == Blocks.CRAFTING_TABLE || clickedBlock.getBlock() == ModBlocks.cuttingBoard) {
            return;
        }

        BlockPos pos = event.getPos().offset(event.getFace());
        if (world.mayPlace(ModBlocks.cuttingBoard, pos, false, event.getFace(), player)) {
            IBlockState placedState = ModBlocks.cuttingBoard.getStateForPlacement(world, pos, event.getFace(), 0.5f, 1f, 0.5f, 0, player, event.getHand());
            BlockEvent.PlaceEvent placeEvent = ForgeEventFactory.onPlayerBlockPlace(player, new BlockSnapshot(world, pos, placedState), event.getFace(), event.getHand());
            if (placeEvent.isCanceled()) {
                return;
            }

            world.setBlockState(pos, placedState);
            if (!player.capabilities.isCreativeMode) {
                event.getItemStack().shrink(1);
            }

            player.swingArm(event.getHand());
            player.playSound(SoundEvents.BLOCK_WOOD_PLACE, 1f, 1f);
            event.setCancellationResult(EnumActionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

}
