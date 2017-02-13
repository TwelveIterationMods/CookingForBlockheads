package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
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

    private static final String FRESH_WATER_ITEM = "freshwaterItem";
    private static final String FRESH_MILK_ITEM = "freshmilkItem";

    public HarvestCraftAddon() {
        super("harvestcraft");

        ItemStack oliveOil = getModItemStack(OLIVE_OIL_ITEM);
        if(!oliveOil.isEmpty()) {
            CookingForBlockheadsAPI.addOvenFuel(oliveOil, 1600);
        }

        for(int i = 0; i < OVEN_RECIPES.length; i += 2) {
            ItemStack sourceItem = getModItemStack(OVEN_RECIPES[i]);
            ItemStack resultItem = getModItemStack(OVEN_RECIPES[i + 1]);
            if(!sourceItem.isEmpty() && !resultItem.isEmpty()) {
                CookingForBlockheadsAPI.addOvenRecipe(sourceItem, resultItem);
            }
        }

        final ItemStack toastItem = getModItemStack(TOAST_ITEM);
        if(!toastItem.isEmpty()) {
            CookingForBlockheadsAPI.addToastHandler(new ItemStack(Items.BREAD), new ToastOutputHandler() {
                @Override
                public ItemStack getToasterOutput(ItemStack itemStack) {
                    return toastItem;
                }
            });
        }

        addNonFoodRecipe(ADDITIONAL_RECIPES);
        addTool(TOOLS);

        CookingForBlockheadsAPI.addWaterItem(getModItemStack(FRESH_WATER_ITEM));
        CookingForBlockheadsAPI.addMilkItem(getModItemStack(FRESH_MILK_ITEM));
    }

    public static boolean isWeirdBrokenRecipe(IRecipe recipe) {
        if(recipe.getRecipeSize() == 2 && recipe instanceof ShapelessOreRecipe) {
            ShapelessOreRecipe oreRecipe = (ShapelessOreRecipe) recipe;
            Object first = oreRecipe.getInput().get(0);
            Object second = oreRecipe.getInput().get(1);
            ItemStack firstItem = ItemStack.EMPTY;
            ItemStack secondItem = ItemStack.EMPTY;
            if (first instanceof ItemStack) {
                firstItem = (ItemStack) first;
            } else if (first instanceof List) {
                List list = (List) first;
                if (list.size() == 1) {
                    firstItem = (ItemStack) list.get(0);
                }
            }
            if (second instanceof ItemStack) {
                secondItem = (ItemStack) second;
            } else if (second instanceof List) {
                List list = (List) second;
                if (list.size() == 1) {
                    secondItem = (ItemStack) list.get(0);
                }
            }
            if (firstItem != null && secondItem != null && ItemStack.areItemStacksEqual(firstItem, secondItem) && !oreRecipe.getRecipeOutput().isEmpty() && oreRecipe.getRecipeOutput().isItemEqual(firstItem)) {
                return true;
            }
        }
        return false;
    }

}
