package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.callback.LivingEntityCallback;
import net.blay09.mods.balm.platform.event.callback.ServerPlayerCallback;
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
import net.minecraft.resources.Identifier;
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

    public static void initialize(BalmRegistrars registrars) {
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

        Balm.modSupport().milkFluid().enable();

        CookingForBlockheadsAPI.addSortButton(new NameSortButton());
        CookingForBlockheadsAPI.addSortButton(new HungerSortButton());
        CookingForBlockheadsAPI.addSortButton(new SaturationSortButton());

        CookingForBlockheadsAPI.registerKitchenRecipeHandler(ShapedRecipe.class, new KitchenShapedRecipeHandler());
        CookingForBlockheadsAPI.registerKitchenRecipeHandler(ShapelessRecipe.class, new KitchenShapelessRecipeHandler());
        CookingForBlockheadsAPI.registerKitchenRecipeHandler(SmeltingRecipe.class, new KitchenSmeltingRecipeHandler());

        CookingForBlockheadsConfig.initialize();
        registrars.dataComponentTypes(ModDataComponents::initialize);
        ModNetworking.initialize(Balm.networking());
        registrars.blocks(ModBlocks::initialize);
        registrars.blockEntityTypes( ModBlockEntities::initialize);
        registrars.items(ModItems::initialize);
        registrars.creativeModeTabs(ModItems::initialize);
        registrars.recipeTypes(ModRecipes::initialize);
        registrars.menuTypes(ModMenus::initialize);
        registrars.registrar(Registries.SOUND_EVENT, ModSounds::initialize);
        ModCapabilities.initialize(Balm.capabilities());

        Balm.initializeIfLoaded(Compat.HARVESTCRAFT_FOOD_CORE, "net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon");

        CookingForBlockheadsRegistry.initialize();

        ServerPlayerCallback.Login.EVENT.register(player -> {
            final var data = Balm.hooks().getPersistentData(player);
            final var favoriteItemIds = data.getCompound("CookingForBlockheads")
                    .flatMap(it -> it.getCompound("FavoriteItemIds"))
                    .map(it -> it.keySet().stream().map(Identifier::parse).collect(Collectors.toSet())).orElse(Set.of());
            Balm.networking().sendTo(player, new FavoriteListMessage(favoriteItemIds));
        });

        LivingEntityCallback.Damage.EVENT.register(CowJarHandler::onLivingDamage);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
