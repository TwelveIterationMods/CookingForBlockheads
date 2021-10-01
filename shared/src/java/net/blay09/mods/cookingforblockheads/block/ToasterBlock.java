package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.ToasterHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.ToasterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ToasterBlock extends BlockKitchen {

    public static final String name = "toaster";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape SHAPE = Block.box(4.4, 0, 4.4, 11.6, 6.4, 11.6);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public ToasterBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.5f), registryName);
        registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ToasterBlockEntity) {
            ToasterBlockEntity toaster = (ToasterBlockEntity) blockEntity;
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.isEmpty() || !toaster.getContainer().getItem(0).isEmpty() && !toaster.getContainer().getItem(1).isEmpty()) {
                if (!toaster.isActive() && (!toaster.getContainer().getItem(0).isEmpty() || !toaster.getContainer().getItem(1).isEmpty())) {
                    toaster.setActive(!toaster.isActive());
                }
            } else {
                ToasterHandler toastHandler = CookingRegistry.getToasterHandler(heldItem);
                if (toastHandler != null) {
                    ItemStack output = toastHandler.getToasterOutput(heldItem);
                    if (!output.isEmpty()) {
                        for (int i = 0; i < toaster.getContainer().getContainerSize(); i++) {
                            if (toaster.getContainer().getItem(i).isEmpty()) {
                                toaster.getContainer().setItem(i, heldItem.split(1));
                                return InteractionResult.SUCCESS;
                            }
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToasterBlockEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        ToasterBlockEntity tileEntity = (ToasterBlockEntity) world.getBlockEntity(pos);
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
