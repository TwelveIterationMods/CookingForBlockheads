package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockOven extends BlockKitchen {

    public static BooleanProperty POWERED = BooleanProperty.create("powered");

    public static final String name = "oven";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    private static final Random random = new Random();

    public BlockOven() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5f, 10f), registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        /*if (!heldItem.isEmpty() && heldItem.getItem() == Items.DYE) {
            if (recolorBlock(world, pos, facing, EnumDyeColor.byDyeDamage(heldItem.getItemDamage()))) {
                heldItem.shrink(1);
            }
            return true;
        }*/

        if (facing == Direction.UP) {
            if (CookingRegistry.isToolItem(heldItem)) {
                Direction stateFacing = state.get(FACING);
                float hx = hitX;
                float hz = hitZ;
                switch (stateFacing) {
                    case NORTH:
                        hx = 1f - hitX;
                        hz = 1f - hitZ;
                        break;
//                    case SOUTH: hx = hitX; hz = hitZ; break;
                    case WEST:
                        hz = 1f - hitX;
                        hx = hitZ;
                        break;
                    case EAST:
                        hz = hitX;
                        hx = 1f - hitZ;
                        break;
                }
                int index = -1;
                if (hx < 0.5f && hz < 0.5f) {
                    index = 1;
                } else if (hx >= 0.5f && hz < 0.5f) {
                    index = 0;
                } else if (hx < 0.5f && hz >= 0.5f) {
                    index = 3;
                } else if (hx >= 0.5f && hz >= 0.5f) {
                    index = 2;
                }
                if (index != -1) {
                    TileOven tileOven = (TileOven) world.getTileEntity(pos);
                    if (tileOven != null && tileOven.getToolItem(index).isEmpty()) {
                        ItemStack toolItem = heldItem.split(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return true;
            }
        }

        if (facing == state.get(FACING)) {
            TileOven tileOven = (TileOven) world.getTileEntity(pos);
            if (tileOven != null) {
                if (player.isSneaking()) {
                    tileOven.getDoorAnimator().toggleForcedOpen();
                    return true;
                } else if (!heldItem.isEmpty() && tileOven.getDoorAnimator().isForcedOpen()) {
                    heldItem = ItemHandlerHelper.insertItemStacked(tileOven.getInputHandler(), heldItem, false);
                    if (!heldItem.isEmpty()) {
                        heldItem = ItemHandlerHelper.insertItemStacked(tileOven.getItemHandlerFuel(), heldItem, false);
                    }
                    player.setHeldItem(hand, heldItem);
                    return true;
                }
            }
        }

        if (!world.isRemote) {
            player.openGui(CookingForBlockheads.instance, GuiHandler.COOKING_OVEN, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        boolean hasPowerUpgrade = tileEntity instanceof TileOven && ((TileOven) tileEntity).hasPowerUpgrade();
        return state.withProperty(POWERED, hasPowerUpgrade);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileOven();
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        TileOven tileEntity = (TileOven) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isBurning()) {
            Direction facing = state.get(FACING);
            float x = (float) pos.getX() + 0.5f;
            float y = (float) pos.getY() + 0f + BlockOven.random.nextFloat() * 6f / 16f;
            float z = (float) pos.getZ() + 0.5f;
            float f3 = 0.52f;
            float f4 = BlockOven.random.nextFloat() * 0.6f - 0.3f;

            if (facing == Direction.WEST) {
                world.addParticle(ParticleTypes.SMOKE, (double) (x - f3), (double) y, (double) (z + f4), 0, 0, 0);
            } else if (facing == Direction.EAST) {
                world.addParticle(ParticleTypes.SMOKE, (double) (x + f3), (double) y, (double) (z + f4), 0, 0, 0);
            } else if (facing == Direction.NORTH) {
                world.addParticle(ParticleTypes.SMOKE, (double) (x + f4), (double) y, (double) (z - f3), 0, 0, 0);
            } else if (facing == Direction.SOUTH) {
                world.addParticle(ParticleTypes.SMOKE, (double) (x + f4), (double) y, (double) (z + f3), 0, 0, 0);
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileOven) {
            if (((TileOven) tileEntity).hasPowerUpgrade()) {
                ItemUtils.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.heatingUnit));
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

}
