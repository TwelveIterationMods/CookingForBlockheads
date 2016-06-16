package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class HarvestCraftAddon extends SimpleAddon {

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

    private static final String OLIVE_OIL_ITEM = "oliveoilItem";
    private static final String TOAST_ITEM = "toastItem";

    public HarvestCraftAddon() {
        super("harvestcraft");

        ItemStack oliveOil = getModItemStack(OLIVE_OIL_ITEM);
        if(oliveOil != null) {
            CookingForBlockheadsAPI.addOvenFuel(oliveOil, 1600);
        }

        for(int i = 0; i < OVEN_RECIPES.length; i += 2) {
            ItemStack sourceItem = getModItemStack(OVEN_RECIPES[i]);
            ItemStack resultItem = getModItemStack(OVEN_RECIPES[i + 1]);
            if(sourceItem != null && resultItem != null) {
                CookingForBlockheadsAPI.addOvenRecipe(sourceItem, resultItem);
            }
        }

        final ItemStack toastItem = getModItemStack(TOAST_ITEM);
        if(toastItem != null) {
            CookingForBlockheadsAPI.addToastHandler(new ItemStack(Items.BREAD), new ToastHandler() {
                @Override
                public ItemStack getToasterOutput(ItemStack itemStack) {
                    return toastItem;
                }
            });
        }

        addNonFoodRecipe(ADDITIONAL_RECIPES);
        addTool(TOOLS);
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
