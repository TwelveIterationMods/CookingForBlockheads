package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.VanillaAddon;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID)
@Mod(modid = CookingForBlockheads.MOD_ID, acceptedMinecraftVersions = "[1.12]", dependencies = "after:mousetweaks[2.8,)")
public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
	public static final Logger logger = LogManager.getLogger(MOD_ID);

	public static final CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.recipeBook, 1, 1);
		}
	};

	@Mod.Instance(MOD_ID)
    public static CookingForBlockheads instance;

	@SidedProxy(clientSide = "net.blay09.mods.cookingforblockheads.client.ClientProxy", serverSide = "net.blay09.mods.cookingforblockheads.CommonProxy")
    public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CookingForBlockheadsAPI.setupAPI(new InternalMethods());

		MinecraftForge.EVENT_BUS.register(new IMCHandler());
		MinecraftForge.EVENT_BUS.register(new CowJarHandler());

		ModBlocks.registerTileEntities();

		proxy.preInit(event);
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingForBlockheads.instance, new GuiHandler());

		ModRecipes.load();

		FMLInterModComms.sendFunctionMessage(Compat.THEONEPROBE, "getTheOneProbe", "net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon");

		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(ModConfig.modules.vanilla) {
			new VanillaAddon();
		}

		if(ModConfig.modules.pamsHarvestcraft) {
			event.buildSoftDependProxy(Compat.PAMS_HARVESTCRAFT, "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");
		}

		if(ModConfig.modules.moreFoods) {
			event.buildSoftDependProxy(Compat.MORE_FOOD, "net.blay09.mods.cookingforblockheads.compat.MoreFoodsAddon");
		}

		if(ModConfig.modules.extraFood) {
			event.buildSoftDependProxy(Compat.EXTRA_FOOD, "net.blay09.mods.cookingforblockheads.compat.ExtraFoodAddon");
		}

		if(ModConfig.modules.foodExpansion) {
			event.buildSoftDependProxy(Compat.FOOD_EXPANSION, "net.blay09.mods.cookingforblockheads.compat.FoodExpansionAddon");
		}

		if(ModConfig.modules.vanillaFoodPantry) {
			event.buildSoftDependProxy(Compat.VANILLA_FOOD_PANTRY, "net.blay09.mods.cookingforblockheads.compat.VanillaFoodPantryAddon");
		}

		event.buildSoftDependProxy(Compat.APPLECORE, "net.blay09.mods.cookingforblockheads.compat.AppleCoreAddon");
		event.buildSoftDependProxy(Compat.MINETWEAKER, "net.blay09.mods.cookingforblockheads.compat.MineTweakerAddon");

		CookingRegistry.initFoodRegistry();
	}

	@Mod.EventHandler
	public void imc(FMLInterModComms.IMCEvent event) {
		IMCHandler.handleIMCMessage(event);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModBlocks.registerItemBlocks(event.getRegistry());
		ModItems.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		ModSounds.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		proxy.registerModels();
		ModBlocks.registerModels();
		ModItems.registerModels();
	}

}
