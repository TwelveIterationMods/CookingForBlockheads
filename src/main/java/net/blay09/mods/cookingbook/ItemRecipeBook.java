package net.blay09.mods.cookingbook;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemRecipeBook extends Item {

	public ItemRecipeBook() {
		setMaxStackSize(1);
		setUnlocalizedName("cookingbook:recipebook");
		setTextureName("cookingbook:recipebook");
		setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.openGui(CookingBook.instance, GuiHandler.GUI_ID_RECIPEBOOK, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isShiftDown) {
		super.addInformation(itemStack, player, list, isShiftDown);

		list.add("\u00a7e" + I18n.format("cookingbook:recipebook.tooltip"));
		for(String s : I18n.format("cookingbook:recipebook.tooltipDesc").split("\\\\n")) {
			list.add("\u00a77" + s);
		}
	}
}
