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

	public ItemRecipeBook() {
		setMaxStackSize(1);
		setUnlocalizedName("cookingbook:recipebook");
		setTextureName("cookingbook:recipebook");
		setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);

		iconTier2 = register.registerIcon("cookingbook:recipebook_tier2");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(stack.getMetadata() == 1) {
			return "item.cookingbook:recipebook_tier2";
		}
		return super.getUnlocalizedName(stack);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		super.getSubItems(item, creativeTabs, list);
		list.add(new ItemStack(item, 1, 1));
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		switch(damage) {
			case 1: return iconTier2;
			default: return itemIcon;
		}
	}

	//	@Override
//	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//		if(player.isSneaking()) {
//			TileEntity tileEntity = world.getTileEntity(x, y, z);
//			if(tileEntity instanceof IInventory) {
//				player.openGui(CookingBook.instance, GuiHandler.GUI_ID_RECIPEBOOK_WORLD, world, x, y, z);
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.openGui(CookingBook.instance, GuiHandler.GUI_ID_RECIPEBOOK, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isShiftDown) {
		super.addInformation(itemStack, player, list, isShiftDown);

		if(itemStack.getMetadata() == 0) {
			list.add("\u00a7e" + I18n.format("cookingbook:recipebook.tooltip"));
			for (String s : I18n.format("cookingbook:recipebook.tooltipDesc").split("\\\\n")) {
				list.add("\u00a77" + s);
			}
		} else {
			list.add("\u00a7e" + I18n.format("cookingbook:recipebook_tier2.tooltip"));
			for (String s : I18n.format("cookingbook:recipebook_tier2.tooltipDesc").split("\\\\n")) {
				list.add("\u00a77" + s);
			}
		}
	}
}
