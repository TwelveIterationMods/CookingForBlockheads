package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastErrorHandler;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.VanillaAddon;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.Constants;
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

@Mod(modid = CookingForBlockheads.MOD_ID, acceptedMinecraftVersions = "[1.11]")
public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
	private static final Logger logger = LogManager.getLogger();

	public static final CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.recipeBook, 1, 1);
		}
	};

	@Mod.Instance
    public static CookingForBlockheads instance;

	@SidedProxy(clientSide = "net.blay09.mods.cookingforblockheads.client.ClientProxy", serverSide = "net.blay09.mods.cookingforblockheads.CommonProxy")
    public static CommonProxy proxy;

	private final NonNullList<ItemStack> nonFoodRecipes = NonNullList.create();
	public final CowJarHandler cowJarHandler = new CowJarHandler();

	private Configuration config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		CookingConfig.load(config);
		CookingForBlockheadsAPI.setupAPI(new InternalMethods());

		ModBlocks.load();
		ModItems.load();

		proxy.preInit(event);

		if(config.hasChanged()) {
			config.save();
		}

		MinecraftForge.EVENT_BUS.register(this);

		if(CookingConfig.cowJarEnabled) {
			MinecraftForge.EVENT_BUS.register(cowJarHandler);
		}
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingForBlockheads.instance, new GuiHandler());

		FMLInterModComms.sendFunctionMessage(Compat.THEONEPROBE, "getTheOneProbe", "net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon");
		FMLInterModComms.sendMessage(Compat.WAILA, "register", "net.blay09.mods.cookingforblockheads.compat.WailaProvider.register");

		ModRecipes.load(config);

		proxy.init(event);
	}

	@Mod.EventHandler
	public void imc(FMLInterModComms.IMCEvent event) {
		for(FMLInterModComms.IMCMessage message : event.getMessages()) {
			switch(message.key) {
				case "RegisterTool":
					if(message.getMessageType() == ItemStack.class) {
						CookingForBlockheadsAPI.addToolItem(message.getItemStackValue());
					} else {
						logger.error("IMC API Error: RegisterTool expected message of type ItemStack");
					}
					break;
				case "RegisterWaterItem":
					if(message.getMessageType() == ItemStack.class) {
						CookingForBlockheadsAPI.addWaterItem(message.getItemStackValue());
					} else {
						logger.error("IMC API Error: RegisterWaterItem expected message of type ItemStack");
					}
					break;
				case "RegisterMilkItem":
					if(message.getMessageType() == ItemStack.class) {
						CookingForBlockheadsAPI.addMilkItem(message.getItemStackValue());
					} else {
						logger.error("IMC API Error: RegisterMilkItem expected message of type ItemStack");
					}
					break;
				case "RegisterToast":
					if(message.getMessageType() == NBTTagCompound.class) {
						ItemStack inputItem = new ItemStack(message.getNBTValue().getCompoundTag("Input"));
						final ItemStack outputItem = new ItemStack(message.getNBTValue().getCompoundTag("Output"));
						if(!inputItem.isEmpty() && !outputItem.isEmpty()) {
							CookingForBlockheadsAPI.addToastHandler(inputItem, new ToastOutputHandler() {
								@Override
								public ItemStack getToasterOutput(ItemStack itemStack) {
									return outputItem;
								}
							});
						} else {
							logger.error("IMC API Error: RegisterToast expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
						}
					} else {
						logger.error("IMC API Error: RegisterToast expected message of type NBT");
					}
					break;
				case "RegisterToastError":
					if(message.getMessageType() == NBTTagCompound.class) {
						ItemStack inputItem = new ItemStack(message.getNBTValue().getCompoundTag("Input"));
						final String langKey = message.getNBTValue().getString("Message");
						if(!inputItem.isEmpty() && !langKey.isEmpty()) {
							CookingForBlockheadsAPI.addToastHandler(inputItem, new ToastErrorHandler() {
								@Override
								public ITextComponent getToasterHint(EntityPlayer player, ItemStack itemStack) {
									return new TextComponentTranslation(langKey);
								}
							});
						} else {
							logger.error("IMC API Error: RegisterToastError expected message of type NBT with structure {Input : ItemStack, Message : String}");
						}
					} else {
						logger.error("IMC API Error: RegisterToastError expected message of type NBT");
					}
					break;
				case "RegisterOvenFuel":
					if(message.getMessageType() == NBTTagCompound.class) {
						ItemStack inputItem = new ItemStack(message.getNBTValue().getCompoundTag("Input"));
						if(!inputItem.isEmpty() && message.getNBTValue().hasKey("FuelValue", Constants.NBT.TAG_ANY_NUMERIC)) {
							CookingForBlockheadsAPI.addOvenFuel(inputItem, message.getNBTValue().getInteger("FuelValue"));
						} else {
							logger.error("IMC API Error: RegisterOvenFuel expected message of type NBT with structure {Input : ItemStack, FuelValue : numeric}");
						}
					} else {
						logger.error("IMC API Error: RegisterOvenFuel expected message of type NBT");
					}
					break;
				case "RegisterOvenRecipe":
					if(message.getMessageType() == NBTTagCompound.class) {
						ItemStack inputItem = new ItemStack(message.getNBTValue().getCompoundTag("Input"));
						ItemStack outputItem = new ItemStack(message.getNBTValue().getCompoundTag("Output"));
						if(!inputItem.isEmpty() && !outputItem.isEmpty()) {
							CookingForBlockheadsAPI.addOvenRecipe(inputItem, outputItem);
						} else {
							logger.error("IMC API Error: RegisterOvenRecipe expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
						}
					} else {
						logger.error("IMC API Error: RegisterOvenRecipe expected message of type NBT");
					}
					break;
				case "RegisterNonFoodRecipe":
					if(message.getMessageType() == ItemStack.class) {
						nonFoodRecipes.add(message.getItemStackValue());
					} else {
						logger.error("IMC API Error: RegisterNonFoodRecipe expected message of type ItemStack");
					}
					break;
				case "RegisterCowClass":
					if(message.getMessageType() == String.class) {
						try {
							Class<?> clazz = Class.forName(message.getStringValue());
							cowJarHandler.registerCowClass(clazz);
						} catch (ClassNotFoundException e) {
							logger.error("Could not register cow class " + message.getStringValue() + ": " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						logger.error("IMC API Error: RegisterCowClass expected message of type String");
					}
					break;
			}
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(config.getBoolean("Vanilla Minecraft", "modules", true, "Sink Support, Ingredient Recipes")) {
			new VanillaAddon();
		}

		if(config.getBoolean("Pam's HarvestCraft", "modules", true, "Tool Support, Oven Recipes, Oven Fuel, Ingredient Recipes, Toast")) {
			event.buildSoftDependProxy(Compat.PAMS_HARVESTCRAFT, "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");
		}

		if(config.getBoolean("More Foods", "modules", true, "Tool Support, Ingredient Recipes, Toast")) {
			event.buildSoftDependProxy(Compat.MORE_FOOD, "net.blay09.mods.cookingforblockheads.compat.MoreFoodsAddon");
		}

		if(config.getBoolean("Extra Food", "modules", true, "Tool Support, Ingredient Recipes, Toast")) {
			event.buildSoftDependProxy(Compat.EXTRA_FOOD, "net.blay09.mods.cookingforblockheads.compat.ExtraFoodAddon");
		}

		if(config.getBoolean("Food Expansion", "modules", true, "Ingredient Recipes")) {
			event.buildSoftDependProxy(Compat.FOOD_EXPANSION, "net.blay09.mods.cookingforblockheads.compat.FoodExpansionAddon");
		}

		event.buildSoftDependProxy(Compat.APPLECORE, "net.blay09.mods.cookingforblockheads.compat.AppleCoreAddon");
		event.buildSoftDependProxy(Compat.MINETWEAKER, "net.blay09.mods.cookingforblockheads.compat.MineTweakerAddon");

		CookingRegistry.initFoodRegistry();
	}

	@SubscribeEvent
	public void onFoodRegistryInit(FoodRegistryInitEvent event) {
		for(ItemStack itemStack : nonFoodRecipes) {
			event.registerNonFoodRecipe(itemStack);
		}
	}

}
