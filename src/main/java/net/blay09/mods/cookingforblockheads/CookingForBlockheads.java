package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.ToastErrorHandler;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.compat.VanillaAddon;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

import java.util.List;

@Mod(modid = CookingForBlockheads.MOD_ID,
		acceptedMinecraftVersions = "[1.10]",
		updateJSON = "http://balyware.com/new/forge_update.php?modid=" + CookingForBlockheads.MOD_ID)
public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
	private static final Logger logger = LogManager.getLogger();

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

	private final List<ItemStack> nonFoodRecipes = Lists.newArrayList();

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
			MinecraftForge.EVENT_BUS.register(new CowJarHandler());
		}
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingForBlockheads.instance, new GuiHandler());

		FMLInterModComms.sendMessage("Waila", "register", "net.blay09.mods.cookingforblockheads.compat.WailaProvider.register");

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
						ItemStack inputItem = ItemStack.loadItemStackFromNBT(message.getNBTValue().getCompoundTag("Input"));
						final ItemStack outputItem = ItemStack.loadItemStackFromNBT(message.getNBTValue().getCompoundTag("Output"));
						//noinspection ConstantConditions /// Missing @Nullable
						if(inputItem != null && outputItem != null) {
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
						ItemStack inputItem = ItemStack.loadItemStackFromNBT(message.getNBTValue().getCompoundTag("Input"));
						final String langKey = message.getNBTValue().getString("Message");
						//noinspection ConstantConditions /// Missing @Nullable
						if(inputItem != null && !langKey.isEmpty()) {
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
						ItemStack inputItem = ItemStack.loadItemStackFromNBT(message.getNBTValue().getCompoundTag("Input"));
						//noinspection ConstantConditions /// Missing @Nullable
						if(inputItem != null && message.getNBTValue().hasKey("FuelValue", Constants.NBT.TAG_ANY_NUMERIC)) {
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
						ItemStack inputItem = ItemStack.loadItemStackFromNBT(message.getNBTValue().getCompoundTag("Input"));
						ItemStack outputItem = ItemStack.loadItemStackFromNBT(message.getNBTValue().getCompoundTag("Output"));
						//noinspection ConstantConditions /// Missing @Nullable
						if(inputItem != null && outputItem != null) {
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
			}
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(config.getBoolean("Vanilla Minecraft", "modules", true, "Sink Support, Ingredient Recipes")) {
			new VanillaAddon();
		}

		if(config.getBoolean("Pam's HarvestCraft", "modules", true, "Tool Support, Oven Recipes, Oven Fuel, Ingredient Recipes, Toast")) {
			event.buildSoftDependProxy("harvestcraft", "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");
		}

		if(config.getBoolean("More Foods", "modules", true, "Tool Support, Ingredient Recipes, Toast")) {
			event.buildSoftDependProxy("morefood", "net.blay09.mods.cookingforblockheads.compat.MoreFoodsAddon");
		}

		if(config.getBoolean("Extra Food", "modules", true, "Tool Support, Ingredient Recipes, Toast")) {
			event.buildSoftDependProxy("ExtraFood", "net.blay09.mods.cookingforblockheads.compat.ExtraFoodAddon");
		}

		if(config.getBoolean("Food Expansion", "modules", true, "Ingredient Recipes")) {
			event.buildSoftDependProxy("fe", "net.blay09.mods.cookingforblockheads.compat.FoodExpansionAddon");
		}

		if(config.getBoolean("AppleCore", "modules", true, "Dynamic Food Values")) {
			event.buildSoftDependProxy("AppleCore", "net.blay09.mods.cookingforblockheads.compat.AppleCoreAddon");
		}

		event.buildSoftDependProxy("MineTweaker3", "net.blay09.mods.cookingforblockheads.compat.MineTweakerAddon");

		ModRecipes.load(config);

		CookingRegistry.initFoodRegistry();
	}

	@SubscribeEvent
	public void onFoodRegistryInit(FoodRegistryInitEvent event) {
		for(ItemStack itemStack : nonFoodRecipes) {
			event.registerNonFoodRecipe(itemStack);
		}
	}

}
