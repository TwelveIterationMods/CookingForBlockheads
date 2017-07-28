package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

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
            "oliveoilItem",
            "hotsauceitem",
            "sweetandsoursauceitem",
            "fivespiceitem",
            "hoisinsauceitem",
            "noodlesitem",
            "sesameoilitem",
            "garammasalaitem",
            "soysauceitem",
            "currypowderitem",
            "bubblywateritem",
            "carrotcakeItem",
            "holidaycakeItem",
            "pumpkincheesecakeItem",
            "pavlovaItem",
            "lamingtonItem",
            "cheesecakeItem",
            "cherrycheesecakeItem",
            "pineappleupsidedowncakeItem",
            "chocolatesprinklecakeItem",
            "redvelvetcakeItem"
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
            CookingForBlockheadsAPI.addToastHandler(new ItemStack(Items.BREAD), (ToastOutputHandler) itemStack -> toastItem);
        }

        addNonFoodRecipe(ADDITIONAL_RECIPES);
        addTool(TOOLS);

        CookingForBlockheadsAPI.addWaterItem(getModItemStack(FRESH_WATER_ITEM));
        CookingForBlockheadsAPI.addMilkItem(getModItemStack(FRESH_MILK_ITEM));
    }

    public static boolean isWeirdConversionRecipe(IRecipe recipe) {
        if(recipe.getIngredients().size() == 2 && recipe.getRecipeOutput().getCount() == 2) {
            Ingredient first = recipe.getIngredients().get(0);
            Ingredient second = recipe.getIngredients().get(1);
            if(first.apply(recipe.getRecipeOutput()) && second.apply(recipe.getRecipeOutput())) {
                return true;
            }
        }
        return false;
    }

}
