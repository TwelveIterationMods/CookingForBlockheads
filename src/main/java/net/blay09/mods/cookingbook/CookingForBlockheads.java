package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.blay09.mods.cookingbook.api.CookingAPI;
import net.blay09.mods.cookingbook.block.*;
import net.blay09.mods.cookingbook.item.ItemRecipeBook;
import net.blay09.mods.cookingbook.item.ItemToast;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Mod(modid = CookingForBlockheads.MOD_ID)
public class CookingForBlockheads {

    public static final String MOD_ID = "cookingbook";

	public static CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public Item getTabIconItem() {
			return itemRecipeBook;
		}
	};

	public static Item itemRecipeBook = new ItemRecipeBook();
	public static Item itemToast = new ItemToast();
	public static Block blockCookingTable = new BlockCookingTable();
	public static Block blockCookingOven = new BlockCookingOven();
	public static Block blockFridge = new BlockFridge();
	public static Block blockSink = new BlockSink();
	public static Block blockToolRack = new BlockToolRack();
	public static Block blockToaster = new BlockToaster();

	@Mod.Instance
    public static CookingForBlockheads instance;

	@SidedProxy(clientSide = "net.blay09.mods.cookingbook.client.ClientProxy", serverSide = "net.blay09.mods.cookingbook.CommonProxy")
    public static CommonProxy proxy;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CookingConfig.load(event.getSuggestedConfigurationFile());
		CookingAPI.setupAPI(new InternalMethods());

		proxy.preInit(event);
	}

    @EventHandler
    public void init(FMLInitializationEvent event) {
		proxy.init(event);
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		proxy.serverStarted(event);
	}
}
