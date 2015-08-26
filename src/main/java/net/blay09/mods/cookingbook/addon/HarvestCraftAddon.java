package net.blay09.mods.cookingbook.addon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
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

    public HarvestCraftAddon() {
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
