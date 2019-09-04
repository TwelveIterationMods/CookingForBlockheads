package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.ClientProxy;
import net.blay09.mods.cookingforblockheads.client.gui.HungerSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.NameSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.SaturationSortButton;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.VanillaAddon;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(CookingForBlockheads.MOD_ID)
public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static final NonNullList<ItemStack> extraItemGroupItems = NonNullList.create();
    public static final ItemGroup itemGroup = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.recipeBook);
        }

        @Override
        public void fill(NonNullList<ItemStack> list) {
            super.fill(list);

            list.addAll(extraItemGroupItems);
        }
    };

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public CookingForBlockheads() {
        DeferredWorkQueue.runLater(NetworkHandler::init);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CookingForBlockheadsConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CookingForBlockheadsConfig.clientSpec);

        CookingForBlockheadsAPI.setupAPI(new InternalMethods());

        CookingRegistry.addSortButton(new NameSortButton());
        CookingRegistry.addSortButton(new HungerSortButton());
        CookingRegistry.addSortButton(new SaturationSortButton());

        MinecraftForge.EVENT_BUS.register(new IMCHandler());
        MinecraftForge.EVENT_BUS.register(new CowJarHandler());

        KitchenMultiBlock.registerConnectorBlock(ModBlocks.kitchenFloor);
    }

    private void setup(FMLCommonSetupEvent event) {
        CapabilityKitchenConnector.register();
        CapabilityKitchenItemProvider.register();
        CapabilityKitchenSmeltingProvider.register();
    }

    public void postInit() {
        new VanillaAddon();

        if (ModList.get().isLoaded(Compat.PAMS_HARVESTCRAFT)) {
            try {
                Class.forName("net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon").newInstance();
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (ModList.get().isLoaded(Compat.APPLECORE)) {
            try {
                Class.forName("net.blay09.mods.cookingforblockheads.compat.AppleCoreAddon").newInstance();
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /* TODO if (!JsonCompatLoader.loadCompat()) {
            logger.error("Failed to load Cooking for Blockheads compatibility! Things may not work as expected.");
        }*/

        // TODO Call on server start + after connect CookingRegistry.initFoodRegistry();
    }

    @SubscribeEvent
    public static void enqueueIMC(InterModEnqueueEvent event) {
        // TODO FMLInterModComms.sendFunctionMessage(Compat.THEONEPROBE, "getTheOneProbe", "net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ModTileEntities.registerTileEntities(event.getRegistry());
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

}
