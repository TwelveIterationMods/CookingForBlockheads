package net.blay09.mods.cookingforblockheads.compat.json;

import com.google.gson.*;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber
public class JsonCompatLoader implements IResourceManagerReloadListener {

    private static final Gson gson = new Gson();
    private static final NonNullList<ItemStack> nonFoodRecipes = NonNullList.create();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        for (ResourceLocation resourceLocation : resourceManager.getAllResourceLocations("cookingforblockheads_compat", it -> it.endsWith(".json"))) {
            try (IResource resource = resourceManager.getResource(resourceLocation)) {
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                load(gson.fromJson(reader, JsonCompatData.class));
            } catch (IOException e) {
                CookingForBlockheads.logger.error("Parsing error loading CookingForBlockheads Data File at {}", resourceLocation, e);
            }
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

        for (List<ResourceLocation> list : data.getFoods().values()) {
            for (ResourceLocation registryName : list) {
                findItemStack(registryName).ifPresent(nonFoodRecipes::add);
            }
        }

        for (ResourceLocation registryName : data.getTools()) {
            findItemStack(registryName).ifPresent(CookingForBlockheadsAPI::addToolItem);
        }

        for (ResourceLocation registryName : data.getWater()) {
            findItemStack(registryName).ifPresent(CookingForBlockheadsAPI::addWaterItem);
        }

        for (ResourceLocation registryName : data.getMilk()) {
            findItemStack(registryName).ifPresent(CookingForBlockheadsAPI::addMilkItem);
        }

        for (OvenFuelData ovenFuel : data.getOvenFuels()) {
            findItemStack(ovenFuel.getItem()).ifPresent(itemStack -> CookingForBlockheadsAPI.addOvenFuel(itemStack, ovenFuel.getValue()));
        }

        for (OvenRecipeData ovenRecipe : data.getOvenRecipes()) {
            ItemStack input = findItemStack(ovenRecipe.getInput()).orElse(ItemStack.EMPTY);
            ItemStack output = findItemStack(ovenRecipe.getOutput()).orElse(ItemStack.EMPTY);
            if (!input.isEmpty() && !output.isEmpty()) {
                CookingForBlockheadsAPI.addOvenRecipe(input, output);
            }
        }

        for (ToasterRecipeData toasterRecipe : data.getToasterRecipes()) {
            ItemStack input = findItemStack(toasterRecipe.getInput()).orElse(ItemStack.EMPTY);
            ItemStack output = findItemStack(toasterRecipe.getOutput()).orElse(ItemStack.EMPTY);
            if (!input.isEmpty() && !output.isEmpty()) {
                CookingForBlockheadsAPI.addToasterHandler(input, itemStack -> output);
            }
        }

        for (ResourceLocation kitchenItemProvider : data.getKitchenItemProviders()) {
            CompatCapabilityLoader.addKitchenItemProvider(kitchenItemProvider);
        }

        for (ResourceLocation kitchenConnector : data.getKitchenConnectors()) {
            CompatCapabilityLoader.addKitchenConnector(kitchenConnector);

            Block connectorBlock = ForgeRegistries.BLOCKS.getValue(kitchenConnector);
            if (connectorBlock != null) {
                KitchenMultiBlock.registerConnectorBlock(connectorBlock);
            }
        }
    }

    private static Optional<ItemStack> findItemStack(ResourceLocation registryName) {
        Item item = ForgeRegistries.ITEMS.getValue(registryName);
        return item == null || item == Items.AIR ? Optional.empty() : Optional.of(new ItemStack(item));
    }

}
