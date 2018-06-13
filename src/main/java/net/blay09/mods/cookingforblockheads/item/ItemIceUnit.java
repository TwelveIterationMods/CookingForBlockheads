package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIceUnit extends Item implements IRegisterableItem {

    public ItemIceUnit() {
        setCreativeTab(CookingForBlockheads.creativeTab);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFridge && !((TileFridge) tileEntity).getBaseFridge().hasIceUpgrade) {
            if (!player.capabilities.isCreativeMode) {
                player.getHeldItem(hand).shrink(1);
            }

            ((TileFridge) tileEntity).getBaseFridge().setHasIceUpgrade(true);
            if (!world.isRemote) {
                NetworkHandler.instance.sendToAllAround(new MessageSyncedEffect(pos, MessageSyncedEffect.Type.FRIDGE_UPGRADE), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cookingforblockheads:fridge_upgrade"));

        addDefaultTooltipDescription(tooltip);
    }

    @Override
    public String getIdentifier() {
        return "ice_unit";
    }

}
