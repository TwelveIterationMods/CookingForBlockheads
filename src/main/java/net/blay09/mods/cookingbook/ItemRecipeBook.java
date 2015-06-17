package net.blay09.mods.cookingbook;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemRecipeBook extends Item {

	private IIcon iconTier2;
	private IIcon iconTier3;

	public ItemRecipeBook() {
		setMaxStackSize(1);
		setUnlocalizedName("cookingbook:recipebook_tier1");
		setTextureName("cookingbook:recipebook_tier1");
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);

		iconTier2 = register.registerIcon("cookingbook:recipebook_tier2");
		iconTier3 = register.registerIcon("cookingbook:recipebook_tier3");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(stack.getMetadata() == 1) {
			return "item.cookingbook:recipebook_tier2";
		} else if(stack.getMetadata() == 2) {
			return "item.cookingbook:recipebook_tier1";
		}
		return super.getUnlocalizedName(stack);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		super.getSubItems(item, creativeTabs, list);
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		switch(damage) {
			case 1: return iconTier2;
			case 2: return iconTier3;
			default: return itemIcon;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.openGui(CookingBook.instance, itemStack.getMetadata() == 0 ? GuiHandler.GUI_ID_RECIPEBOOK : GuiHandler.GUI_ID_CRAFTBOOK, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isShiftDown) {
		super.addInformation(itemStack, player, list, isShiftDown);

		if(itemStack.getMetadata() == 1) {
			list.add("\u00a7e" + I18n.format("cookingbook:recipebook_tier2.tooltip"));
			for (String s : I18n.format("cookingbook:recipebook_tier2.tooltipDesc").split("\\\\n")) {
				list.add("\u00a77" + s);
			}
		} else if(itemStack.getMetadata() == 2) {
			list.add("\u00a7e" + I18n.format("cookingbook:recipebook_tier3.tooltip"));
			for (String s : I18n.format("cookingbook:recipebook_tier3.tooltipDesc").split("\\\\n")) {
				list.add("\u00a77" + s);
			}
		} else {
			list.add("\u00a7e" + I18n.format("cookingbook:recipebook_tier1.tooltip"));
			for (String s : I18n.format("cookingbook:recipebook_tier1.tooltipDesc").split("\\\\n")) {
				list.add("\u00a77" + s);
			}
		}
	}
}
