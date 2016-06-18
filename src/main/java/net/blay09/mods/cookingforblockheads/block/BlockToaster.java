package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.ToastErrorHandler;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileToaster;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockToaster extends BlockKitchen {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.275, 0, 0.275, 0.725, 0.4, 0.725);

    public BlockToaster() {
        super(Material.IRON);

        setRegistryName(CookingForBlockheads.MOD_ID, "toaster");
        setUnlocalizedName(getRegistryName().toString());
        setSoundType(SoundType.METAL);
        setHardness(2.5f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileToaster) {
            TileToaster tileToaster = (TileToaster) tileEntity;
            if (heldItem == null || tileToaster.getItemHandler().getStackInSlot(0) != null && tileToaster.getItemHandler().getStackInSlot(1) != null) {
                if (!tileToaster.isActive() && (tileToaster.getItemHandler().getStackInSlot(0) != null || tileToaster.getItemHandler().getStackInSlot(1) != null)) {
                    tileToaster.setActive(!tileToaster.isActive());
                }
            } else {
                ToastHandler toastHandler = CookingRegistry.getToastHandler(heldItem);
                if(toastHandler != null) {
                    ItemStack output = toastHandler instanceof ToastOutputHandler ? ((ToastOutputHandler) toastHandler).getToasterOutput(heldItem) : null;
                    if (output != null) {
                        for (int i = 0; i < tileToaster.getItemHandler().getSlots(); i++) {
                            if (tileToaster.getItemHandler().getStackInSlot(i) == null) {
                                tileToaster.getItemHandler().setStackInSlot(i, heldItem.splitStack(1));
                                return true;
                            }
                        }
                        return true;
                    } else if (!world.isRemote && toastHandler instanceof ToastErrorHandler) {
                        player.addChatComponentMessage(((ToastErrorHandler) toastHandler).getToasterHint(player, heldItem));
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
}
