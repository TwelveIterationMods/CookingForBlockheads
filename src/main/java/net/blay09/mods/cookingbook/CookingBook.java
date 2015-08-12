package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.blay09.mods.cookingbook.block.BlockCookingOven;
import net.blay09.mods.cookingbook.block.BlockCookingTable;
import net.blay09.mods.cookingbook.item.ItemRecipeBook;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = CookingBook.MOD_ID)
public class CookingBook {

    public static final String MOD_ID = "cookingbook";

	public static boolean enableCraftingBook;
	public static boolean enableCookingTable;
	public static boolean enableCookingOven;
	public static Item itemRecipeBook = new ItemRecipeBook();
	public static Block blockCookingTable = new BlockCookingTable();
	public static Block blockCookingOven = new BlockCookingOven();

	@Mod.Instance
    public static CookingBook instance;

	@SidedProxy(clientSide = "net.blay09.mods.cookingbook.client.ClientProxy", serverSide = "net.blay09.mods.cookingbook.CommonProxy")
    public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		enableCraftingBook = config.getBoolean("enableCraftingBook", Configuration.CATEGORY_GENERAL, true, "If set to false, the Tier II recipe book (the crafting book) will not be craftable or usable.");
		enableCookingTable = config.getBoolean("enableCookingTable", Configuration.CATEGORY_GENERAL, true, "If set to false, the cooking table will not be craftable.");
		enableCookingOven = config.getBoolean("enableCookingOven", Configuration.CATEGORY_GENERAL, true, "If set to false, the cooking oven will not be craftable.");
		config.save();
	}

    @EventHandler
    public void init(FMLInitializationEvent event) {
		proxy.init(event);
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
