package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = CookingBook.MOD_ID)
public class CookingBook {

    public static final String MOD_ID = "cookingbook";

	public static boolean enableCraftingBook;
	public static Item itemRecipeBook = new ItemRecipeBook();

	@Mod.Instance
    public static CookingBook instance;

    @SidedProxy(clientSide = "net.blay09.mods.cookingbook.client.ClientProxy", serverSide = "net.blay09.mods.cookingbook.CommonProxy")
    public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		enableCraftingBook = config.getBoolean("enableCraftingBook", Configuration.CATEGORY_GENERAL, true, "If set to false, the Tier II recipe book (the crafting book) will not be craftable or usable.");
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
