package net.blay09.mods.cookingbook.compatibility;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class PamsHarvestcraft {

    private static final String PAMS_MOD_ID = "harvestcraft";

    private static final String[] PAMS_TOOLS = new String[] {
            "item.cuttingboardItem",
            "item.potItem",
            "item.skilletItem",
            "item.saucepanItem",
            "item.bakewareItem",
            "item.mortarandpestleItem",
            "item.mixingbowlItem",
            "item.juicerItem"
    };

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

    public static boolean isToolItem(ItemStack itemStack) {
        return ArrayUtils.contains(PAMS_TOOLS, itemStack.getUnlocalizedName());
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
            if (firstItem != null && secondItem != null && ItemStack.areItemStacksEqual(firstItem, secondItem) && oreRecipe.getRecipeOutput().isItemEqual(firstItem)) {
                return true;
            }
        }
        return false;
    }

    public static void addAdditionalRecipes(List<ItemStack> additionalRecipes) {
        for(String s : ADDITIONAL_RECIPES) {
            ItemStack itemStack = GameRegistry.findItemStack(PAMS_MOD_ID, s, 1);
            if(itemStack != null) {
                additionalRecipes.add(itemStack);
            }
        }
    }

}
