package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.addon.VanillaAddon;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = CookingForBlockheads.MOD_ID)
public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";

	public static CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public Item getTabIconItem() {
			return ModItems.recipeBook;
		}

		@Override
		public int getIconItemDamage() {
			return 1;
		}
	};

	@Mod.Instance
    public static CookingForBlockheads instance;

	@SidedProxy(clientSide = "net.blay09.mods.cookingforblockheads.client.ClientProxy", serverSide = "net.blay09.mods.cookingforblockheads.CommonProxy")
    public static CommonProxy proxy;

	private Configuration config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		CookingConfig.load(config);
		CookingForBlockheadsAPI.setupAPI(new InternalMethods());

		ModBlocks.load();
		ModItems.load();
		ModRecipes.load(config);

		proxy.preInit(event);

		if(config.hasChanged()) {
			config.save();
		}
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingForBlockheads.instance, new GuiHandler());

		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(config.getBoolean("Vanilla Minecraft", "modules", true, "Sink Support, Ingredient Recipes")) {
			new VanillaAddon();
		}

		if(config.getBoolean("Pam's HarvestCraft", "modules", true, "Multiblock Kitchen Support, Tool Support, Oven Recipes, Oven Fuel, Ingredient Recipes")) {
			event.buildSoftDependProxy("harvestcraft", "net.blay09.mods.cookingforblockheads.addon.HarvestCraftAddon");
		}

		if(config.getBoolean("EnviroMine", "modules", true, "Multiblock Kitchen Support (Freezer)")) {
//			event.buildSoftDependProxy("enviromine", "net.blay09.mods.cookingforblockheads.addon.EnviroMineAddon");
		}

		if(config.getBoolean("AppleCore", "modules", true, "Dynamic Food Values")) {
//			event.buildSoftDependProxy("AppleCore", "net.blay09.mods.cookingforblockheads.addon.AppleCoreAddon");
		}

//		event.buildSoftDependProxy("MineTweaker3", "net.blay09.mods.cookingforblockheads.addon.MineTweakerAddon");

		CookingRegistry.initFoodRegistry();
	}

}
