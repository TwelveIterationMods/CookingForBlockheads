package net.blay09.mods.cookingforblockheads.item;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	public static ItemRecipeBook recipeBook;
	public static ItemToast toast;

	public static void load() {
		recipeBook = new ItemRecipeBook();
		GameRegistry.registerItem(recipeBook);

		toast = new ItemToast();
		GameRegistry.registerItem(toast);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		recipeBook.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		toast.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
	}
}
