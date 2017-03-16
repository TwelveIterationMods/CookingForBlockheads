package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.ToastErrorHandler;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.blay09.mods.cookingforblockheads.tile.TileToaster;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockToaster extends BlockKitchen {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.275, 0, 0.275, 0.725, 0.4, 0.725);

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockToaster() {
        super(Material.IRON);

        setRegistryName(CookingForBlockheads.MOD_ID, "toaster");
        setUnlocalizedName(getRegistryNameString());
        setSoundType(SoundType.METAL);
        setHardness(2.5f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileToaster) {
            return state.withProperty(ACTIVE, ((TileToaster) tileEntity).isActive());
        }
        return state;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileToaster) {
            TileToaster tileToaster = (TileToaster) tileEntity;
            ItemStack heldItem = player.getHeldItem(hand);
            if (heldItem.isEmpty() || !tileToaster.getItemHandler().getStackInSlot(0).isEmpty() && !tileToaster.getItemHandler().getStackInSlot(1).isEmpty()) {
                if (!tileToaster.isActive() && (!tileToaster.getItemHandler().getStackInSlot(0).isEmpty() || !tileToaster.getItemHandler().getStackInSlot(1).isEmpty())) {
                    tileToaster.setActive(!tileToaster.isActive());
                }
            } else {
                ToastHandler toastHandler = CookingRegistry.getToastHandler(heldItem);
                if(toastHandler != null) {
                    ItemStack output = toastHandler instanceof ToastOutputHandler ? ((ToastOutputHandler) toastHandler).getToasterOutput(heldItem) : ItemStack.EMPTY;
                    if (!output.isEmpty()) {
                        for (int i = 0; i < tileToaster.getItemHandler().getSlots(); i++) {
                            if (tileToaster.getItemHandler().getStackInSlot(i).isEmpty()) {
                                tileToaster.getItemHandler().setStackInSlot(i, heldItem.splitStack(1));
                                return true;
                            }
                        }
                        return true;
                    } else if (!world.isRemote && toastHandler instanceof ToastErrorHandler) {
                        player.sendStatusMessage(((ToastErrorHandler) toastHandler).getToasterHint(player, heldItem), true);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileToaster();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileToaster tileEntity = (TileToaster) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isActive()) {
            if(rand.nextFloat() < tileEntity.getToastProgress()) {
                double x = (float) pos.getX() + 0.5f + (rand.nextFloat() - 0.5f) * 0.25f;
                double y = (float) pos.getY() + 0.2f + rand.nextFloat() * 6f / 16f;
                double z = (float) pos.getZ() + 0.5f + (rand.nextFloat() - 0.5f) * 0.25f;
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
            }
        }
    }
}
