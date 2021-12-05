package net.blay09.mods.cookingforblockheads.compat.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JsonCompatLoader implements ResourceManagerReloadListener {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();

    public static final NonNullList<ItemStack> nonFoodRecipes = NonNullList.create();
    public static final Set<ResourceLocation> kitchenItemProviders = new HashSet<>();
    public static final Set<ResourceLocation> kitchenConnectors = new HashSet<>();

    static {
        Balm.getEvents().onEvent(FoodRegistryInitEvent.class, JsonCompatLoader::onCookingRegistry);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            for (ResourceLocation resourceLocation : resourceManager.listResources("cookingforblockheads/compat", it -> it.endsWith(".json"))) {
                try (Resource resource = resourceManager.getResource(resourceLocation)) {
                    InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                    load(gson.fromJson(reader, JsonCompatData.class));
                } catch (Exception e) {
                    CookingForBlockheads.logger.error("Parsing error loading CookingForBlockheads data files at {}", resourceLocation, e);
                }
            }

            for (Block kitchenFloor : ModBlocks.kitchenFloors) {
                KitchenMultiBlock.registerConnectorBlock(kitchenFloor);
            }
        } catch (Exception e) {
            CookingForBlockheads.logger.error("Error loading CookingForBlockheads data files", e);
        }
    }

    public static void onCookingRegistry(FoodRegistryInitEvent event) {
        for (ItemStack itemStack : nonFoodRecipes) {
            event.registerNonFoodRecipe(itemStack);
        }
    }

    private static void load(JsonCompatData data) {
        String modId = data.getModId();
        if (!modId.equals("minecraft") && !Balm.isModLoaded(modId)) {
            return;
        }

        if (data.getFoods() != null) {
            for (List<ResourceLocation> list : data.getFoods().values()) {
                for (ResourceLocation registryName : list) {
                    findItemStack(registryName).ifPresent(nonFoodRecipes::add);
                }
            }
        }

        if (data.getTools() != null) {
            for (ResourceLocation registryName : data.getTools()) {
                findItemStack(registryName).ifPresent(CookingForBlockheadsAPI::addToolItem);
            }
        }

        if (data.getWater() != null) {
            for (ResourceLocation registryName : data.getWater()) {
                findItemStack(registryName).ifPresent(CookingForBlockheadsAPI::addWaterItem);
            }
        }

        if (data.getMilk() != null) {
            for (ResourceLocation registryName : data.getMilk()) {
                findItemStack(registryName).ifPresent(CookingForBlockheadsAPI::addMilkItem);
            }
        }

        if (data.getOvenFuels() != null) {
            for (OvenFuelData ovenFuel : data.getOvenFuels()) {
                findItemStack(ovenFuel.getItem()).ifPresent(itemStack -> CookingForBlockheadsAPI.addOvenFuel(itemStack, ovenFuel.getValue()));
            }
        }

        if (data.getOvenRecipes() != null) {
            for (OvenRecipeData ovenRecipe : data.getOvenRecipes()) {
                ItemStack input = findItemStack(ovenRecipe.getInput()).orElse(ItemStack.EMPTY);
                ItemStack output = findItemStack(ovenRecipe.getOutput()).orElse(ItemStack.EMPTY);
                if (!input.isEmpty() && !output.isEmpty()) {
                    CookingForBlockheadsAPI.addOvenRecipe(input, output);
                }
            }
        }

        if (data.getToasterRecipes() != null) {
            for (ToasterRecipeData toasterRecipe : data.getToasterRecipes()) {
                ItemStack input = findItemStack(toasterRecipe.getInput()).orElse(ItemStack.EMPTY);
                ItemStack output = findItemStack(toasterRecipe.getOutput()).orElse(ItemStack.EMPTY);
                if (!input.isEmpty() && !output.isEmpty()) {
                    CookingForBlockheadsAPI.addToasterHandler(input, itemStack -> output);
                }
            }
        }

        if (data.getKitchenItemProviders() != null) {
            kitchenItemProviders.addAll(data.getKitchenItemProviders());
        }

        if (data.getKitchenConnectors() != null) {
            for (ResourceLocation kitchenConnector : data.getKitchenConnectors()) {
                kitchenConnectors.add(kitchenConnector);

                Block connectorBlock = Balm.getRegistries().getBlock(kitchenConnector);
                if (connectorBlock != null && connectorBlock != Blocks.AIR) {
                    KitchenMultiBlock.registerConnectorBlock(connectorBlock);
                }
            }
        }
    }

    private static Optional<ItemStack> findItemStack(ResourceLocation registryName) {
        Item item = Balm.getRegistries().getItem(registryName);
        return item == null || item == Items.AIR ? Optional.empty() : Optional.of(new ItemStack(item));
    }

}
