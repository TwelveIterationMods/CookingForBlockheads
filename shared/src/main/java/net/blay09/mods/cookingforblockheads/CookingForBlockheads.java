package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.HungerSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.NameSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.SaturationSortButton;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.crafting.KitchenShapedRecipeHandler;
import net.blay09.mods.cookingforblockheads.crafting.KitchenShapelessRecipeHandler;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.ModNetworking;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.sound.ModSounds;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static void initialize() {
        Balm.getRegistries().enableMilkFluid();

        CookingForBlockheadsAPI.addSortButton(new NameSortButton());
        CookingForBlockheadsAPI.addSortButton(new HungerSortButton());
        CookingForBlockheadsAPI.addSortButton(new SaturationSortButton());

        CookingForBlockheadsAPI.registerKitchenRecipeHandler(ShapedRecipe.class, new KitchenShapedRecipeHandler());
        CookingForBlockheadsAPI.registerKitchenRecipeHandler(ShapelessRecipe.class, new KitchenShapelessRecipeHandler());

        CookingForBlockheadsConfig.initialize();
        ModNetworking.initialize(Balm.getNetworking());
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModItems.initialize(Balm.getItems());
        ModRecipes.initialize(Balm.getRecipes());
        ModMenus.initialize(Balm.getMenus());
        ModSounds.initialize(Balm.getSounds());

        Balm.initializeIfLoaded("minecraft", "net.blay09.mods.cookingforblockheads.compat.VanillaAddon");
        Balm.initializeIfLoaded(Compat.HARVESTCRAFT_FOOD_CORE, "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");

        CookingForBlockheadsRegistry.initialize(Balm.getEvents());

        Balm.getEvents().onEvent(LivingDamageEvent.class, CowJarHandler::onLivingDamage);
    }

}
