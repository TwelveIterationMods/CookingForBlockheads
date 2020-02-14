package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.tile.CounterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KitchenCounterBlock extends BlockDyeableKitchen {

    public static final String name = "counter";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public KitchenCounterBlock() {
        this(registryName);
    }

    public KitchenCounterBlock(ResourceLocation registryName) {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(5f, 10f), registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, FLIPPED, COLOR, HAS_COLOR);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CounterTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (tryRecolorBlock(state, heldItem, world, pos, player, rayTraceResult)) {
            return ActionResultType.SUCCESS;
        }

        CounterTileEntity tileCounter = (CounterTileEntity) world.getTileEntity(pos);
        if (rayTraceResult.getFace() == state.get(FACING)) {
            if (tileCounter != null) {
                if (player.isShiftKeyDown()) {
                    tileCounter.getDoorAnimator().toggleForcedOpen();
                    return ActionResultType.SUCCESS;
                } else if (!heldItem.isEmpty() && tileCounter.getDoorAnimator().isForcedOpen()) {
                    heldItem = ItemHandlerHelper.insertItemStacked(tileCounter.getItemHandler(), heldItem, false);
                    player.setHeldItem(hand, heldItem);
                    return ActionResultType.SUCCESS;
                }
            }
        }

        if (!world.isRemote) {
            if (rayTraceResult.getFace() == Direction.UP && !heldItem.isEmpty() && (heldItem.getItem() instanceof BlockItem || heldItem.getItem() == Compat.cuttingBoardItem)) {
                return ActionResultType.FAIL;
            }

            NetworkHooks.openGui((ServerPlayerEntity) player, tileCounter, pos);
        }

        return ActionResultType.SUCCESS;
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FLIPPED, shouldBePlacedFlipped(context, state.get(FACING)));
    }

}
