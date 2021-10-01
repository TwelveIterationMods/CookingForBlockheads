package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.HungerSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.NameSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.SaturationSortButton;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.ModNetworking;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.ModBlockEntities;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static void initialize() {
        CookingForBlockheadsAPI.setupAPI(new InternalMethods());

        CookingRegistry.addSortButton(new NameSortButton());
        CookingRegistry.addSortButton(new HungerSortButton());
        CookingRegistry.addSortButton(new SaturationSortButton());

        CookingForBlockheadsConfig.initialize();
        ModNetworking.initialize(Balm.getNetworking());
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModItems.initialize(Balm.getItems());
        ModMenus.initialize(Balm.getMenus());
        ModSounds.initialize(Balm.getSounds());

        Balm.initializeIfLoaded("minecraft", "net.blay09.mods.cookingforblockheads.compat.VanillaAddon");
        Balm.initializeIfLoaded(Compat.HARVESTCRAFT_FOOD_CORE, "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");
        Balm.initializeIfLoaded(Compat.APPLECORE, "net.blay09.mods.cookingforblockheads.compat.AppleCoreAddon");

        Balm.getEvents().onEvent(ServerStartedEvent.class, event -> {
                RecipeManager recipeManager = event.getServer().getRecipeManager();
                CookingRegistry.initFoodRegistry(recipeManager);
        });

        Balm.initialize(MOD_ID);
    }

    /*public CookingForBlockheads() { TODO
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imc);

        MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::addReloadListenersLate);
        MinecraftForge.EVENT_BUS.addListener(this::recipesUpdated);

        MinecraftForge.EVENT_BUS.register(new IMCHandler());
        MinecraftForge.EVENT_BUS.register(new CowJarHandler());

        ForgeMod.enableMilkFluid();
    }

    private void setup(FMLCommonSetupEvent event) {
        CapabilityKitchenConnector.register();
        CapabilityKitchenItemProvider.register();
        CapabilityKitchenSmeltingProvider.register();
    }

    private void imc(InterModEnqueueEvent event) {
        if(Balm.isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }

    private void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new JsonCompatLoader());
    }

    private void addReloadListenersLate(AddReloadListenerEvent event) {
        event.addListener((IResourceManagerReloadListener) resourceManager -> CookingRegistry.initFoodRegistry(event.getDataPackRegistries().getRecipeManager()));
    }

    private void recipesUpdated(RecipesUpdatedEvent event) {
        CookingRegistry.initFoodRegistry(event.getRecipeManager());
    }*/

}
