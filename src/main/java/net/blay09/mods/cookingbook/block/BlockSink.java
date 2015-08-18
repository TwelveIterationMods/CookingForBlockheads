package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.SinkRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockSink extends Block {

    public BlockSink() {
        super(Material.iron);

        setBlockName("cookingbook:sink");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeMetal);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (FluidContainerRegistry.isEmptyContainer(player.getHeldItem())) {
            ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(FluidRegistry.getFluidStack("water", 1000), player.getHeldItem());
            if(filledContainer != null) {
                if(player.getHeldItem().stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, filledContainer);
                } else {
                    if(player.inventory.addItemStackToInventory(filledContainer)) {
                        player.getHeldItem().stackSize--;
                    }
                }
            }
            return true;
        } else if(FluidContainerRegistry.isFilledContainer(player.getHeldItem())) {
            ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(player.getHeldItem());
            if(emptyContainer != null) {
                if(player.getHeldItem().stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, emptyContainer);
                } else {
                    if(player.inventory.addItemStackToInventory(emptyContainer)) {
                        player.getHeldItem().stackSize--;
                    }
                }
            }
            return true;
        } else {
            ItemStack resultStack = SinkRecipes.getSinkOutput(player.getHeldItem());
            if(resultStack != null) {
                if(player.getHeldItem().stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, resultStack);
                } else {
                    if(player.inventory.addItemStackToInventory(resultStack)) {
                        player.getHeldItem().stackSize--;
                    }
                }
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }
}
