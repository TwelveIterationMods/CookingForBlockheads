package net.blay09.mods.cookingbook.addon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.api.CookingAPI;
import net.blay09.mods.cookingbook.api.FoodRegistryInitEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class HarvestCraftAddon {

    private static final String[] ADDITIONAL_RECIPES = new String[] {
            "flourItem",
            "doughItem",
            "cornmealItem",
            "freshwaterItem",
            "pastaItem",
            "vanillaItem",
            "butterItem",
            "heavycreamItem",
            "saltItem",
            "freshmilkItem",
            "mayoItem",
            "cocoapowderItem",
            "ketchupItem",
            "vinegarItem",
            "mustardItem",
            "blackpepperItem",
            "groundcinnamonItem",
            "groundnutmegItem",
            "saladdressingItem",
            "batterItem",
            "oliveoilItem"
    };

    private static final String[] OVEN_RECIPES = new String[] {
            "turkeyrawItem", "turkeycookedItem",
            "rabbitrawItem", "rabbitcookedItem",
            "venisonrawItem", "venisoncookedItem"
    };

    public HarvestCraftAddon() {
        CookingAPI.addOvenFuel(GameRegistry.findItemStack("harvestcraft", "oliveoilItem", 1), 1600);

        for(int i = 0; i < OVEN_RECIPES.length; i += 2) {
            ItemStack source = GameRegistry.findItemStack("harvestcraft", OVEN_RECIPES[i], 1);
            ItemStack result = GameRegistry.findItemStack("harvestcraft", OVEN_RECIPES[i + 1], 1);
            if(source != null && result != null) {
                CookingAPI.addOvenRecipe(source, result);
            }
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onFoodRegistryInit(FoodRegistryInitEvent event) {
        event.registerNonFoodRecipe(new ItemStack(Items.cake));
        event.registerNonFoodRecipe(new ItemStack(Items.sugar));
        for(String s : ADDITIONAL_RECIPES) {
            ItemStack itemStack = GameRegistry.findItemStack("harvestcraft", s, 1);
            if(itemStack != null) {
                event.registerNonFoodRecipe(itemStack);
            }
        }
    }

}
