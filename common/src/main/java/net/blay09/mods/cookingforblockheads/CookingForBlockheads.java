package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.FoodStatsProvider;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.client.gui.HungerSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.NameSortButton;
import net.blay09.mods.cookingforblockheads.client.gui.SaturationSortButton;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.component.ModDataComponents;
import net.blay09.mods.cookingforblockheads.crafting.KitchenShapedRecipeHandler;
import net.blay09.mods.cookingforblockheads.crafting.KitchenShapelessRecipeHandler;
import net.blay09.mods.cookingforblockheads.crafting.KitchenSmeltingRecipeHandler;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.network.ModNetworking;
import net.blay09.mods.cookingforblockheads.network.message.FavoriteListMessage;
import net.blay09.mods.cookingforblockheads.recipe.ModRecipes;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.blay09.mods.cookingforblockheads.sound.ModSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CookingForBlockheads {

    public static final String MOD_ID = "cookingforblockheads";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static void initialize() {
        CookingForBlockheadsAPI.setFoodStatsProvider(new FoodStatsProvider() {
            @Override
            public float getSaturationModifier(ItemStack itemStack, Player entityPlayer) {
                return Optional.ofNullable(itemStack.get(DataComponents.FOOD)).map(FoodProperties::saturation).orElse(0f);
            }

            @Override
            public int getNutrition(ItemStack itemStack, Player entityPlayer) {
                return Optional.ofNullable(itemStack.get(DataComponents.FOOD)).map(FoodProperties::nutrition).orElse(0);
            }
        });

        Balm.getModSupport().milkFluid().enable();

        CookingForBlockheadsAPI.addSortButton(new NameSortButton());
        CookingForBlockheadsAPI.addSortButton(new HungerSortButton());
        CookingForBlockheadsAPI.addSortButton(new SaturationSortButton());

        CookingForBlockheadsAPI.registerKitchenRecipeHandler(ShapedRecipe.class, new KitchenShapedRecipeHandler());
        CookingForBlockheadsAPI.registerKitchenRecipeHandler(ShapelessRecipe.class, new KitchenShapelessRecipeHandler());
        CookingForBlockheadsAPI.registerKitchenRecipeHandler(SmeltingRecipe.class, new KitchenSmeltingRecipeHandler());

        CookingForBlockheadsConfig.initialize();
        Balm.dataComponentTypes(MOD_ID, ModDataComponents::initialize);
        ModNetworking.initialize(Balm.getNetworking());
        Balm.blocks(MOD_ID, ModBlocks::initialize);
        Balm.blockEntityTypes(MOD_ID, ModBlockEntities::initialize);
        Balm.items(MOD_ID, ModItems::initialize);
        Balm.creativeModeTabs(MOD_ID, ModItems::initialize);
        Balm.recipeTypes(MOD_ID, ModRecipes::initialize);
        ModMenus.initialize();
        ModSounds.initialize(Balm.registrar(Registries.SOUND_EVENT, MOD_ID));
        ModCapabilities.initialize(Balm.getCapabilities());

        Balm.initializeIfLoaded(Compat.HARVESTCRAFT_FOOD_CORE, "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");

        CookingForBlockheadsRegistry.initialize(Balm.getEvents());

        Balm.getEvents().onEvent(PlayerLoginEvent.class, event -> {
            final var data = Balm.getHooks().getPersistentData(event.getPlayer());
            final var favoriteItemIds = data.getCompound("CookingForBlockheads")
                    .flatMap(it -> it.getCompound("FavoriteItemIds"))
                    .map(it -> it.keySet().stream().map(ResourceLocation::parse).collect(Collectors.toSet())).orElse(Set.of());
            Balm.getNetworking().sendTo(event.getPlayer(), new FavoriteListMessage(favoriteItemIds));
        });

        Balm.getEvents().onEvent(LivingDamageEvent.class, CowJarHandler::onLivingDamage);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
