package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.ToolRackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ToolRackBlock extends BlockKitchen {

    public static final String name = "tool_rack";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(0, 4, 14, 16, 16, 16),
            Block.box(0, 4, 0, 16, 16, 2),
            Block.box(14, 4, 0, 16, 16, 16),
            Block.box(0, 4, 0, 2, 16, 16),
    };

    private static final VoxelShape[] RENDER_SHAPES = new VoxelShape[]{
            Block.box(0, 11, 14, 16, 14, 16),
            Block.box(0, 11, 0, 16, 14, 2),
            Block.box(14, 11, 0, 16, 14, 16),
            Block.box(0, 11, 0, 2, 14, 16),
    };

    public ToolRackBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2.5f), registryName);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToolRackBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return SHAPES[facing.get3DDataValue() - 2];
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return RENDER_SHAPES[facing.get3DDataValue() - 2];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        if (facing == Direction.UP || facing == Direction.DOWN) {
            facing = Direction.NORTH;
        }

        return defaultBlockState().setValue(FACING, facing);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.SUCCESS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
            return InteractionResult.SUCCESS;
        }

        if (rayTraceResult.getLocation().y > 0.25f) {
            Direction stateFacing = state.getValue(FACING);

            double hitX = rayTraceResult.getLocation().x - pos.getX();
            double hitZ = rayTraceResult.getLocation().z - pos.getZ();
            double hit = switch (stateFacing) {
                case NORTH -> hitX;
                case SOUTH -> 1f - hitX;
                case WEST -> 1f - hitZ;
                case EAST -> hitZ;
                default -> hitX;
            };

            int hitSlot = hit > 0.5f ? 0 : 1;
            ToolRackBlockEntity toolRack = (ToolRackBlockEntity) level.getBlockEntity(pos);
            if (toolRack != null) {
                if (!heldItem.isEmpty()) {
                    ItemStack oldToolItem = toolRack.getContainer().getItem(hitSlot);
                    ItemStack toolItem = heldItem.split(1);
                    if (!oldToolItem.isEmpty()) {
                        if (!player.getInventory().add(oldToolItem)) {
                            player.drop(oldToolItem, false);
                        }
                        toolRack.getContainer().setItem(hitSlot, toolItem);
                    } else {
                        toolRack.getContainer().setItem(hitSlot, toolItem);
                    }
                } else {
                    ItemStack itemStack = toolRack.getContainer().getItem(hitSlot);
                    if (!itemStack.isEmpty()) {
                        toolRack.getContainer().setItem(hitSlot, ItemStack.EMPTY);
                        player.setItemInHand(hand, itemStack);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
    }

}
