package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHeatingUnit extends Item {

    public static final String name = "heating_unit";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public ItemHeatingUnit() {
        setUnlocalizedName(registryName.toString());
        setCreativeTab(CookingForBlockheads.creativeTab);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileOven && !((TileOven) tileEntity).hasPowerUpgrade()) {
            ((TileOven) tileEntity).setHasPowerUpgrade(true);
            if (!world.isRemote) {
                NetworkHandler.instance.sendToAllAround(new MessageSyncedEffect(pos, MessageSyncedEffect.Type.OVEN_UPGRADE), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cookingforblockheads:oven_upgrade"));
        for (String s : I18n.format("tooltip.cookingforblockheads:heating_unit.description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

}
