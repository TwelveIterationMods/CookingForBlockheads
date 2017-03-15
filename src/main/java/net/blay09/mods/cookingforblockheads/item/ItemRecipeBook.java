package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemRecipeBook extends Item {

	public ItemRecipeBook() {
		setMaxStackSize(1);
		setRegistryName(CookingForBlockheads.MOD_ID, "recipe_book");
		setCreativeTab(CookingForBlockheads.creativeTab);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "item." + CookingForBlockheads.MOD_ID + ":recipe_book_tier" + itemstack.getItemDamage();
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		subItems.add(new ItemStack(item, 1, 0));
		subItems.add(new ItemStack(item, 1, 1));
		subItems.add(new ItemStack(item, 1, 2));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.openGui(CookingForBlockheads.instance, GuiHandler.ITEM_RECIPE_BOOK, world, hand.ordinal(), 0, 0);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean isShiftDown) {
		super.addInformation(itemStack, player, list, isShiftDown);

		list.add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":recipe_book_tier" + itemStack.getItemDamage()));
		for (String s : I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":recipe_book_tier" + itemStack.getItemDamage() + ".description").split("\\\\n")) {
			list.add(TextFormatting.GRAY + s);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelBakery.registerItemVariants(this, new ResourceLocation(CookingForBlockheads.MOD_ID, "recipe_book"), new ResourceLocation(CookingForBlockheads.MOD_ID, "recipe_book_tier1"), new ResourceLocation(CookingForBlockheads.MOD_ID, "recipe_book_tier2"));

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":recipe_book", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":recipe_book_tier1", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":recipe_book_tier2", "inventory"));
	}
}
