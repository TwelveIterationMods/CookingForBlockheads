package net.blay09.mods.cookingforblockheads.compat.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.compat.CompatCapabilityLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber
public class JsonCompatLoader implements IResourceManagerReloadListener {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();

    private static final NonNullList<ItemStack> nonFoodRecipes = NonNullList.create();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        try {
            for (ResourceLocation resourceLocation : resourceManager.getAllResourceLocations("cookingforblockheads/compat", it -> it.endsWith(".json"))) {
                try (IResource resource = resourceManager.getResource(resourceLocation)) {
                    InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                    load(gson.fromJson(reader, JsonCompatData.class));
                } catch (Exception e) {
                    CookingForBlockheads.logger.error("Parsing error loading CookingForBlockheads data files at {}", resourceLocation, e);
                }
            }

            KitchenMultiBlock.registerConnectorBlock(ModBlocks.kitchenFloor);
        } catch (Exception e) {
            CookingForBlockheads.logger.error("Error loading CookingForBlockheads data files", e);
        }
    }

    @SubscribeEvent
    public static void onCookingRegistry(FoodRegistryInitEvent event) {
        for (ItemStack itemStack : nonFoodRecipes) {
            event.registerNonFoodRecipe(itemStack);
        }
    }

    private static void load(JsonCompatData data) {
        String modId = data.getModId();
        if (!modId.equals("minecraft") && !ModList.get().isLoaded(modId)) {
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
            for (ResourceLocation kitchenItemProvider : data.getKitchenItemProviders()) {
                CompatCapabilityLoader.addKitchenItemProvider(kitchenItemProvider);
            }
        }

        if (data.getKitchenConnectors() != null) {
            for (ResourceLocation kitchenConnector : data.getKitchenConnectors()) {
                CompatCapabilityLoader.addKitchenConnector(kitchenConnector);

                Block connectorBlock = ForgeRegistries.BLOCKS.getValue(kitchenConnector);
                if (connectorBlock != null) {
                    KitchenMultiBlock.registerConnectorBlock(connectorBlock);
                }
            }
        }
    }

    private static Optional<ItemStack> findItemStack(ResourceLocation registryName) {
        Item item = ForgeRegistries.ITEMS.getValue(registryName);
        return item == null || item == Items.AIR ? Optional.empty() : Optional.of(new ItemStack(item));
    }

}
