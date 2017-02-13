package net.blay09.mods.cookingforblockheads.item;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	public static ItemRecipeBook recipeBook;

	public static void load() {
		recipeBook = new ItemRecipeBook();
		GameRegistry.register(recipeBook);
		recipeBook.registerModels();
	}

	@SideOnly(Side.CLIENT)
	@Deprecated
	public static void initModels() {
	}
}
