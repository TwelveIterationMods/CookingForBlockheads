package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.tile.util.IDyeableKitchen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockKitchen extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LOWERED = BooleanProperty.create("lowered");
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private static final AxisAlignedBB BOUNDING_BOX_X = new AxisAlignedBB(0.03125, 0, 0, 0.96875, 0.9375, 1);
    private static final AxisAlignedBB BOUNDING_BOX_Z = new AxisAlignedBB(0, 0, 0.03125, 1, 0.9375, 0.96875);

    private final ResourceLocation registryName;

    protected BlockKitchen(Block.Properties properties, ResourceLocation registryName) {
        super(properties);
        this.registryName = registryName;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(FACING).getAxis() == EnumFacing.Axis.X) {
            return BOUNDING_BOX_X;
        } else {
            return BOUNDING_BOX_Z;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        TranslationTextComponent textComponent = new TranslationTextComponent("tooltip.cookingforblockheads:multiblock_kitchen");
        textComponent.getStyle().setColor(TextFormatting.YELLOW);
        tooltip.add(textComponent);

        if (hasTooltipDescription()) {
            for (String s : I18n.format("tooltip." + registryName + ".description").split("\\\\n")) {
                tooltip.add(TextFormatting.GRAY + s);
            }
        }

        if (isDyeable()) {
            tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.cookingforblockheads:dyeable"));
        }
    }

    protected boolean hasTooltipDescription() {
        return true;
    }

    protected boolean isDyeable() {
        return false;
    }

    public static boolean shouldBlockRenderLowered(IWorldReader world, BlockPos pos) {
        Block blockBelow = world.getBlockState(pos.down()).getBlock();
        return blockBelow == ModBlocks.corner || blockBelow == ModBlocks.counter;
    }

    public boolean shouldBePlacedFlipped(BlockItemUseContext context, Direction facing) {
        BlockPos pos = context.getPos();
        PlayerEntity placer = context.getPlayer();
        if (placer == null) {
            return Math.random() < 0.5;
        }

        boolean flipped;
        double dir = 0;
        if (facing.getAxis() == Direction.Axis.Z) {
            dir = pos.getX() + 0.5f - placer.posX;
            dir *= -1;
        } else if (facing.getAxis() == Direction.Axis.X) {
            dir = pos.getZ() + 0.5f - placer.posZ;
        }
        if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            flipped = dir < 0;
        } else {
            flipped = dir > 0;
        }
        return flipped;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .ifPresent(itemHandler -> ItemUtils.dropItemHandlerItems(world, pos, itemHandler));
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean recolorBlock(BlockState state, IWorld world, BlockPos pos, Direction facing, DyeColor color) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IDyeableKitchen) {
            IDyeableKitchen dyeable = (IDyeableKitchen) tileEntity;
            if (dyeable.getDyedColor() == color) {
                return false;
            }

            dyeable.setDyedColor(color);
        }

        return true;
    }

}
