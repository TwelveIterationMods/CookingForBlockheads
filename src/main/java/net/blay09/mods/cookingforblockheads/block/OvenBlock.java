package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.OvenTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class OvenBlock extends BlockKitchen {

    public static BooleanProperty POWERED = BooleanProperty.create("powered");
    public static BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final String name = "oven";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    private static final Random random = new Random();

    public OvenBlock() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5f, 10f), registryName);
        setDefaultState(getStateContainer().getBaseState().with(POWERED, false).with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, ACTIVE);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (rayTraceResult.getFace() == Direction.UP) {
            if (CookingRegistry.isToolItem(heldItem)) {
                Direction stateFacing = state.get(FACING);
                double hx = rayTraceResult.getHitVec().x;
                double hz = rayTraceResult.getHitVec().z;
                switch (stateFacing) {
                    case NORTH:
                        hx = 1f - rayTraceResult.getHitVec().x;
                        hz = 1f - rayTraceResult.getHitVec().z;
                        break;
//                    case SOUTH: hx = hitX; hz = hitZ; break;
                    case WEST:
                        hz = 1f - rayTraceResult.getHitVec().x;
                        hx = rayTraceResult.getHitVec().z;
                        break;
                    case EAST:
                        hz = rayTraceResult.getHitVec().x;
                        hx = 1f - rayTraceResult.getHitVec().z;
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
                    OvenTileEntity tileOven = (OvenTileEntity) world.getTileEntity(pos);
                    if (tileOven != null && tileOven.getToolItem(index).isEmpty()) {
                        ItemStack toolItem = heldItem.split(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return true;
            }
        }

        OvenTileEntity tileEntity = (OvenTileEntity) world.getTileEntity(pos);
        if (rayTraceResult.getFace() == state.get(FACING)) {
            if (tileEntity != null) {
                if (player.isSneaking()) {
                    tileEntity.getDoorAnimator().toggleForcedOpen();
                    return true;
                } else if (!heldItem.isEmpty() && tileEntity.getDoorAnimator().isForcedOpen()) {
                    heldItem = ItemHandlerHelper.insertItemStacked(tileEntity.getInputHandler(), heldItem, false);
                    if (!heldItem.isEmpty()) {
                        heldItem = ItemHandlerHelper.insertItemStacked(tileEntity.getItemHandlerFuel(), heldItem, false);
                    }
                    player.setHeldItem(hand, heldItem);
                    return true;
                }
            }
        }

        if (!world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, tileEntity, pos);
        }

        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OvenTileEntity();
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        OvenTileEntity tileEntity = (OvenTileEntity) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isBurning()) {
            Direction facing = state.get(FACING);
            float x = (float) pos.getX() + 0.5f;
            float y = (float) pos.getY() + 0f + OvenBlock.random.nextFloat() * 6f / 16f;
            float z = (float) pos.getZ() + 0.5f;
            float f3 = 0.52f;
            float f4 = OvenBlock.random.nextFloat() * 0.6f - 0.3f;

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
        if (tileEntity instanceof OvenTileEntity) {
            if (((OvenTileEntity) tileEntity).hasPowerUpgrade()) {
                ItemUtils.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.heatingUnit));
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

}
