package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.PopulateCookingRegistryEvent;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.capabilities.Capabilities;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public class FarmersDelightAddon {

    private static final ResourceLocation COOKING_RECIPE_TYPE_ID = ResourceLocation.fromNamespaceAndPath(Compat.FARMERS_DELIGHT, "cooking");

    private final RecipeType<?> cookingPotRecipeType;

    public FarmersDelightAddon() {
        cookingPotRecipeType = BuiltInRegistries.RECIPE_TYPE.getOptional(COOKING_RECIPE_TYPE_ID)
                .orElseThrow(() -> new IllegalStateException("Missing Farmer's Delight recipe type: " + COOKING_RECIPE_TYPE_ID));

        CookingForBlockheadsAPI.registerKitchenRecipeHandler(CookingPotRecipe.class, new KitchenCookingPotRecipeHandler());

        CookingForBlockheadsRegistry.registerProcessorRecipeType(cookingPotRecipeType, Component.translatable("tooltip.cookingforblockheads.missing_cooking_pot"));
        Balm.getEvents().onEvent(PopulateCookingRegistryEvent.class, event -> loadCompatRecipes(event.getRecipeManager(), event.getRegistryAccess()));

        final var providers = ((NeoForgeBalmProviders) Balm.getProviders());
        providers.registerFallbackBlockProvider(KitchenItemProcessor.class, (blockEntity, direction) -> {
            final var level = blockEntity.getLevel();
            if (level == null || !Balm.isModLoaded(Compat.FARMERS_DELIGHT)) {
                return null;
            }

            final var blockId = Balm.getRegistries().getKey(blockEntity.getBlockState().getBlock());
            if (!ResourceLocation.fromNamespaceAndPath(Compat.FARMERS_DELIGHT, "cooking_pot").equals(blockId)) {
                return null;
            }

            final var itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), Direction.UP);
            if (itemHandler != null) {
                return new FarmersDelightCookingPotItemProcessor(cookingPotRecipeType, level, itemHandler);
            }

            return null;
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void loadCompatRecipes(RecipeManager recipeManager, RegistryAccess registryAccess) {
        CookingForBlockheadsRegistry.loadRecipesByType(recipeManager, registryAccess, (RecipeType) cookingPotRecipeType);
    }
}
