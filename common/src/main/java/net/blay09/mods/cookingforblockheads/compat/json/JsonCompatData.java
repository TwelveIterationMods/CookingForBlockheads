package net.blay09.mods.cookingforblockheads.compat.json;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class JsonCompatData {

    @SerializedName("modid")
    private String modId;

    private Map<String, List<ResourceLocation>> foods;
    private List<ResourceLocation> tools;
    private List<ResourceLocation> water;
    private List<ResourceLocation> milk;

    @SerializedName("oven_fuel")
    private List<OvenFuelData> ovenFuels;

    @SerializedName("oven_recipes")
    private List<OvenRecipeData> ovenRecipes;

    @SerializedName("toaster")
    private List<ToasterRecipeData> toasterRecipes;

    private List<ResourceLocation> kitchenItemProviders;
    private List<ResourceLocation> kitchenConnectors;

    public String getModId() {
        return modId;
    }

    public Map<String, List<ResourceLocation>> getFoods() {
        return foods;
    }

    public List<ResourceLocation> getTools() {
        return tools;
    }

    public List<ResourceLocation> getWater() {
        return water;
    }

    public List<ResourceLocation> getMilk() {
        return milk;
    }

    public List<OvenFuelData> getOvenFuels() {
        return ovenFuels;
    }

    public List<OvenRecipeData> getOvenRecipes() {
        return ovenRecipes;
    }

    public List<ToasterRecipeData> getToasterRecipes() {
        return toasterRecipes;
    }

    public List<ResourceLocation> getKitchenItemProviders() {
        return kitchenItemProviders;
    }

    public List<ResourceLocation> getKitchenConnectors() {
        return kitchenConnectors;
    }
}
