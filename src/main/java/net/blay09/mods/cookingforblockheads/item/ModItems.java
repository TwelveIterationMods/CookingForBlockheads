package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(CookingForBlockheads.MOD_ID)
public class ModItems {

	@GameRegistry.ObjectHolder(ItemRecipeBook.name)
	public static Item recipeBook = Items.AIR;

	@GameRegistry.ObjectHolder(ItemHeatingUnit.name)
	public static Item heatingUnit = Items.AIR;

	@GameRegistry.ObjectHolder(ItemIceUnit.name)
	public static Item iceUnit = Items.AIR;

	@GameRegistry.ObjectHolder(ItemPreservationChamber.name)
	public static Item preservationChamber = Items.AIR;

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemRecipeBook().setRegistryName(ItemRecipeBook.name),
				new ItemHeatingUnit().setRegistryName(ItemHeatingUnit.name),
				new ItemIceUnit().setRegistryName(ItemIceUnit.name),
				new ItemPreservationChamber().setRegistryName(ItemPreservationChamber.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(recipeBook, 0, new ModelResourceLocation("cookingforblockheads:recipe_book", "inventory"));
		ModelLoader.setCustomModelResourceLocation(recipeBook, 1, new ModelResourceLocation("cookingforblockheads:recipe_book_tier1", "inventory"));
		ModelLoader.setCustomModelResourceLocation(recipeBook, 2, new ModelResourceLocation("cookingforblockheads:recipe_book_tier2", "inventory"));
		ModelLoader.setCustomModelResourceLocation(heatingUnit, 0, new ModelResourceLocation("cookingforblockheads:heating_unit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(iceUnit, 0, new ModelResourceLocation("cookingforblockheads:ice_unit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(preservationChamber, 0, new ModelResourceLocation("cookingforblockheads:preservation_chamber", "inventory"));
	}
}
