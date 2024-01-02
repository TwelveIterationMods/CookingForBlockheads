package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HangingCornerBlock extends BaseKitchenBlock {

    public static final MapCodec<HangingCornerBlock> CODEC = simpleCodec(HangingCornerBlock::new);

    private static final VoxelShape[] BOUNDING_BOXES = new VoxelShape[]{
            Block.box(0, 0, 0, 15.5, 15, 15.5),
            Block.box(0.5, 0, 0.5, 16, 15, 16),
            Block.box(0, 0, 0.5, 15.5, 15, 16),
            Block.box(0.5, 0, 0, 16, 15, 15.5)
    };

    public HangingCornerBlock(Properties properties) {
        super(properties.sound(SoundType.STONE).strength(5f, 10f));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR, HAS_COLOR);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (tryRecolorBlock(state, heldItem, level, pos, player, rayTraceResult)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOXES[state.getValue(FACING).get3DDataValue() - 2];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }



    @Override
    protected void appendHoverDescriptionText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.hanging_corner.description").withStyle(ChatFormatting.GRAY));
    }
}
