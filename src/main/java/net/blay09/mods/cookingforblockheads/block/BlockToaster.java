package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.TileToaster;
import net.minecraft.block.BlockContainer;
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

import java.util.List;

public class BlockToaster extends BlockContainer {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.275, 0, 0.275, 0.725, 0.4, 0.725);

    public BlockToaster() {
        super(Material.iron);

        setRegistryName(CookingForBlockheads.MOD_ID, "toaster");
        setUnlocalizedName(getRegistryName().toString());
        setStepSound(SoundType.WOOD);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
//        TileToaster tileEntity = (TileToaster) world.getTileEntity(pos);
//        if(heldItem == null) {
//            if(!tileEntity.isActive()) {
//                tileEntity.setActive(!tileEntity.isActive());
//            }
//        } else {
//            ItemStack output = CookingRegistry.getToastOutput(heldItem);
//            if(output != null) {
//                for(int i = 0; i < tileEntity.getSizeInventory(); i++) {
//                    if(tileEntity.getStackInSlot(i) == null) {
//                        tileEntity.setInventorySlotContents(i, heldItem.splitStack(1));
//                        return false;
//                    }
//                }
//                return false;
//            }
//        }
        return false;
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
