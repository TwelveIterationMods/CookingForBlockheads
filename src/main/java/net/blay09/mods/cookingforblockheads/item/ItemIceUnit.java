package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIceUnit extends Item {

	public static final String name = "ice_unit";
	public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

	public ItemIceUnit() {
		setUnlocalizedName(registryName.toString());
		setCreativeTab(CookingForBlockheads.creativeTab);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);

		tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cookingforblockheads:fridge_upgrade"));
		for (String s : I18n.format("tooltip.cookingforblockheads:ice_unit.description").split("\\\\n")) {
			tooltip.add(TextFormatting.GRAY + s);
		}
	}

}
