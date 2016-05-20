package net.blay09.mods.cookingforblockheads.addon;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class HarvestCraftAddon {

    private static final String MOD_ID = "harvestcraft";

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

    private static final String[] TOOLS = new String[] {
            "cuttingboardItem",
            "potItem",
            "skilletItem",
            "saucepanItem",
            "bakewareItem",
            "mortarandpestleItem",
            "mixingbowlItem",
            "juicerItem"
    };

    private static final String TOAST_ITEM = "toastItem";

    public HarvestCraftAddon() {
        Item oliveOil = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, "oliveoilItem"));
        if(oliveOil != null) {
            CookingForBlockheadsAPI.addOvenFuel(new ItemStack(oliveOil), 1600);
        }

        for(int i = 0; i < OVEN_RECIPES.length; i += 2) {
            Item sourceItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, OVEN_RECIPES[i]));
            Item resultItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, OVEN_RECIPES[i + 1]));
            if(sourceItem != null && resultItem != null) {
                CookingForBlockheadsAPI.addOvenRecipe(new ItemStack(sourceItem), new ItemStack(resultItem));
            }
        }

        for(String toolName : TOOLS) {
            Item toolItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, toolName));
            if(toolItem != null) {
                CookingForBlockheadsAPI.addToolItem(new ItemStack(toolItem));
            }
        }

        final Item toastItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, TOAST_ITEM));
        if(toastItem != null) {
            CookingForBlockheadsAPI.addToastHandler(new ItemStack(Items.BREAD), new ToastHandler() {
                @Override
                public ItemStack getToasterOutput(ItemStack itemStack) {
                    return new ItemStack(toastItem);
                }
            });
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onFoodRegistryInit(FoodRegistryInitEvent event) {
        for(String s : ADDITIONAL_RECIPES) {
            Item item = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, s));
            if(item != null) {
                event.registerNonFoodRecipe(new ItemStack(item));
            }
        }
    }

    public static boolean isWeirdBrokenRecipe(IRecipe recipe) {
        if(recipe.getRecipeSize() == 2 && recipe instanceof ShapelessOreRecipe) {
            ShapelessOreRecipe oreRecipe = (ShapelessOreRecipe) recipe;
            Object first = oreRecipe.getInput().get(0);
            Object second = oreRecipe.getInput().get(1);
            ItemStack firstItem = null;
            ItemStack secondItem = null;
            if (first instanceof ItemStack) {
                firstItem = (ItemStack) first;
            } else if (first instanceof ArrayList) {
                List list = (List) first;
                if (list.size() == 1) {
                    firstItem = (ItemStack) list.get(0);
                }
            }
            if (second instanceof ItemStack) {
                secondItem = (ItemStack) second;
            } else if (second instanceof ArrayList) {
                List list = (List) second;
                if (list.size() == 1) {
                    secondItem = (ItemStack) list.get(0);
                }
            }
            if (firstItem != null && secondItem != null && ItemStack.areItemStacksEqual(firstItem, secondItem) && oreRecipe.getRecipeOutput() != null && oreRecipe.getRecipeOutput().isItemEqual(firstItem)) {
                return true;
            }
        }
        return false;
    }

}
