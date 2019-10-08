package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.CookingTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CookingTableBlock extends BlockDyeableKitchen {

    public static final String name = "cooking_table";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public CookingTableBlock() {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2.5f), registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        CookingTableTileEntity tileEntity = (CookingTableTileEntity) world.getTileEntity(pos);
        if (!heldItem.isEmpty()) {
            if (tileEntity != null) {
                if (tryRecolorBlock(heldItem, world, pos, player, rayTraceResult)) {
                    return true;
                }

                if (!tileEntity.hasNoFilterBook() && heldItem.getItem() == ModItems.noFilterBook) {
                    tileEntity.setNoFilterBook(heldItem.split(1));
                    return true;
                }
            }
        } else if (player.isSneaking()) {
            if (tileEntity != null) {
                ItemStack noFilterBook = tileEntity.getNoFilterBook();
                if (!noFilterBook.isEmpty()) {
                    if (!player.inventory.addItemStackToInventory(noFilterBook)) {
                        player.dropItem(noFilterBook, false);
                    }
                    tileEntity.setNoFilterBook(ItemStack.EMPTY);
                    return true;
                }
            }
        }

        if (!world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, tileEntity, pos);
        }

        return true;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        CookingTableTileEntity tileEntity = (CookingTableTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            ItemUtils.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tileEntity.getNoFilterBook());
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CookingTableTileEntity();
    }

}
