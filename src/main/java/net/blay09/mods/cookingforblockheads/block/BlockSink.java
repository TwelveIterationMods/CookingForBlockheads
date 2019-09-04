package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSink extends BlockDyeableKitchen {

    public static final String name = "sink";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public BlockSink(DyeColor dyeColor, ResourceLocation registryName) {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(5f, 10f), dyeColor, registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR, FLIPPED);
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FLIPPED, shouldBePlacedFlipped(context, state.get(FACING)));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (tryRecolorBlock(heldItem, world, pos, player, rayTraceResult)) {
            return true;
        }

        ItemStack resultStack = CookingRegistry.getSinkOutput(heldItem);
        if (!resultStack.isEmpty()) {
            CompoundNBT tagCompound = heldItem.getTag();
            ItemStack newItem = resultStack.copy();
            if (tagCompound != null) {
                newItem.setTag(tagCompound);
            }
            if (heldItem.getCount() <= 1) {
                player.setHeldItem(hand, newItem);
            } else {
                if (player.inventory.addItemStackToInventory(newItem)) {
                    heldItem.shrink(1);
                }
            }
            spawnParticles(world, pos, state);
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1f, world.rand.nextFloat() + 0.5f);
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                LazyOptional<IFluidHandler> fluidHandlerCap = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                if (!fluidHandlerCap.isPresent()) {
                    spawnParticles(world, pos, state);
                } else {
                    IFluidHandler fluidHandler = fluidHandlerCap.orElseThrow(IllegalStateException::new);
                    if (!FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) {
                        // Special case for bottles, they can hold 1/3 of a bucket
                        if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                            FluidStack simulated = fluidHandler.drain(333, IFluidHandler.FluidAction.SIMULATE);
                            if (simulated.getAmount() == 333) {
                                fluidHandler.drain(333, IFluidHandler.FluidAction.EXECUTE);
                                if (player.addItemStackToInventory(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER))) {
                                    heldItem.shrink(1);
                                }
                            } else {
                                spawnParticles(world, pos, state);
                            }
                        } else {
                            spawnParticles(world, pos, state);
                        }
                    }
                }
                return !heldItem.isEmpty() && !(heldItem.getItem() instanceof BlockItem);
            }
        }

        return true;
    }

    private void spawnParticles(World world, BlockPos pos, BlockState state) {
        float dripWaterX = 0f;
        float dripWaterZ = 0f;
        switch (state.get(FACING)) {
            case NORTH:
                dripWaterZ = 0.25f;
                dripWaterX = -0.05f;
                break;
            case SOUTH:
                dripWaterX = 0.25f;
                break;
            case WEST:
                dripWaterX = 0.25f;
                dripWaterZ = 0.25f;
                break;
            case EAST:
                dripWaterZ = -0.05f;
                break;
        }

        float particleX = (float) pos.getX() + 0.5f;
        float particleY = (float) pos.getY() + 1.25f;
        float particleZ = (float) pos.getZ() + 0.5f;
        world.addParticle(ParticleTypes.SPLASH, (double) particleX + dripWaterX, (double) particleY - 0.45f, (double) particleZ + dripWaterZ, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            world.addParticle(ParticleTypes.SPLASH, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 0, 0);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 0.1f, world.rand.nextFloat() + 0.5f);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSink();
    }

    @Override
    public boolean recolorBlock(BlockState state, IWorld world, BlockPos pos, Direction facing, DyeColor color) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileSink) {
            TileSink tileSink = (TileSink) tileEntity;
            if (tileSink.getDyedColor() == color) {
                return false;
            }

            tileSink.setDyedColor(color);
        }
        return true;
    }

}
