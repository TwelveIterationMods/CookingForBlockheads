package net.blay09.mods.cookingforblockheads.compat.json;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public class JsonCompatData {

    @SerializedName("modid")
    private String modId;

    private Map<String, List<Identifier>> foods;
    private List<Identifier> tools;
    private List<Identifier> water;
    private List<Identifier> milk;

    @SerializedName("oven_fuel")
    private List<OvenFuelData> ovenFuels;

    @SerializedName("oven_recipes")
    private List<OvenRecipeData> ovenRecipes;

    @SerializedName("toaster")
    private List<ToasterRecipeData> toasterRecipes;

    private List<Identifier> kitchenItemProviders;
    private List<Identifier> kitchenConnectors;

    public String getModId() {
        return modId;
    }

    public Map<String, List<Identifier>> getFoods() {
        return foods;
    }

    public List<Identifier> getTools() {
        return tools;
    }

    public List<Identifier> getWater() {
        return water;
    }

    public List<Identifier> getMilk() {
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

    public List<Identifier> getKitchenItemProviders() {
        return kitchenItemProviders;
    }

    public List<Identifier> getKitchenConnectors() {
        return kitchenConnectors;
    }
}
