package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.ToasterHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.ToasterTileEntity;
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
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockToaster extends BlockKitchen {

    public static final String name = "toaster";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape SHAPE = Block.makeCuboidShape(0.275, 0, 0.275, 0.725, 0.4, 0.725);
    private static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockToaster() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5f), registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ToasterTileEntity) {
            ToasterTileEntity tileToaster = (ToasterTileEntity) tileEntity;
            ItemStack heldItem = player.getHeldItem(hand);
            if (heldItem.isEmpty() || !tileToaster.getItemHandler().getStackInSlot(0).isEmpty() && !tileToaster.getItemHandler().getStackInSlot(1).isEmpty()) {
                if (!tileToaster.isActive() && (!tileToaster.getItemHandler().getStackInSlot(0).isEmpty() || !tileToaster.getItemHandler().getStackInSlot(1).isEmpty())) {
                    tileToaster.setActive(!tileToaster.isActive());
                }
            } else {
                ToasterHandler toastHandler = CookingRegistry.getToasterHandler(heldItem);
                if (toastHandler != null) {
                    ItemStack output = toastHandler.getToasterOutput(heldItem);
                    if (!output.isEmpty()) {
                        for (int i = 0; i < tileToaster.getItemHandler().getSlots(); i++) {
                            if (tileToaster.getItemHandler().getStackInSlot(i).isEmpty()) {
                                tileToaster.getItemHandler().setStackInSlot(i, heldItem.split(1));
                                return true;
                            }
                        }

                        return true;
                    }
                }
            }
        }

        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ToasterTileEntity();
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        ToasterTileEntity tileEntity = (ToasterTileEntity) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isActive()) {
            if (random.nextFloat() < tileEntity.getToastProgress()) {
                double x = (float) pos.getX() + 0.5f + (random.nextFloat() - 0.5f) * 0.25f;
                double y = (float) pos.getY() + 0.2f + random.nextFloat() * 6f / 16f;
                double z = (float) pos.getZ() + 0.5f + (random.nextFloat() - 0.5f) * 0.25f;
                world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
            }
        }
    }

}
