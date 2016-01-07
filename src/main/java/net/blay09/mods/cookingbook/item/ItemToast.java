package net.blay09.mods.cookingbook.item;

import net.blay09.mods.cookingbook.CookingForBlockheads;
import net.blay09.mods.cookingbook.GuiHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemToast extends ItemFood {

	public ItemToast() {
		super(7, 1.2f, false);
		setUnlocalizedName(CookingForBlockheads.MOD_ID + ":toast");
		setTextureName(CookingForBlockheads.MOD_ID + ":toast");
		setCreativeTab(CookingForBlockheads.creativeTab);
	}

}
