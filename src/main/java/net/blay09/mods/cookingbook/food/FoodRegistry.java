package net.blay09.mods.cookingbook.food;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.food.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FoodRegistry {

    private static final ArrayListMultimap<ItemFood, IFoodRecipe> foodItems = ArrayListMultimap.create();

    public static void init() {
        for(Object obj : CraftingManager.getInstance().getRecipeList()) {
            IRecipe recipe = (IRecipe) obj;
            ItemStack output = recipe.getRecipeOutput();
            if(output != null && output.getItem() instanceof ItemFood) {
                if(PamsHarvestcraft.isWeirdBrokenRecipe(recipe)) {
                    continue;
                }
                if(recipe instanceof ShapedRecipes) {
                    foodItems.put((ItemFood) output.getItem(), new ShapedCraftingFood((ShapedRecipes) recipe));
                } else if(recipe instanceof ShapelessRecipes) {
                    foodItems.put((ItemFood) output.getItem(), new ShapelessCraftingFood((ShapelessRecipes) recipe));
                } else if(recipe instanceof ShapelessOreRecipe) {
                    foodItems.put((ItemFood) output.getItem(), new ShapelessOreCraftingFood((ShapelessOreRecipe) recipe));
                } else if(recipe instanceof ShapedOreRecipe) {
                    foodItems.put((ItemFood) output.getItem(), new ShapedOreCraftingFood((ShapedOreRecipe) recipe));
                }
            }
        }
        for(Object obj : FurnaceRecipes.smelting().getSmeltingList().entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            ItemStack sourceStack = null;
            if(entry.getKey() instanceof Item) {
                sourceStack = new ItemStack((Item) entry.getKey());
            } else if(entry.getKey() instanceof Block) {
                sourceStack = new ItemStack((Block) entry.getKey());
            } else if(entry.getKey() instanceof ItemStack) {
                sourceStack = (ItemStack) entry.getKey();
            }
            ItemStack resultStack = (ItemStack) entry.getValue();
            if(resultStack.getItem() instanceof ItemFood) {
                foodItems.put((ItemFood) resultStack.getItem(), new SmeltingFood(resultStack, sourceStack));
            }
        }
    }

    public static Collection<IFoodRecipe> getFoodRecipes() {
        return foodItems.values();
    }

    public static boolean isAvailableFor(IFoodIngredient[] craftMatrix, IInventory inventory) {
        List<IFoodIngredient> ingredients = new ArrayList<IFoodIngredient>();
        for (IFoodIngredient craftMatrixItem : craftMatrix) {
            if (craftMatrixItem != null && !craftMatrixItem.isOptional()) {
                boolean found = false;
                for (IFoodIngredient stackIngredient : ingredients) {
                    if (stackIngredient.equals(craftMatrixItem)) {
                        stackIngredient.increaseStackSize(craftMatrixItem.getStackSize());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ingredients.add(craftMatrixItem.copy());
                }
            }
        }

        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if(itemStack != null) {
                for (IFoodIngredient ingredient : ingredients) {
                    if (ingredient.isValidItem(itemStack)) {
                        ingredient.increaseStackSize(-itemStack.stackSize);
                    }
                }
            }
        }

        for (IFoodIngredient ingredient : ingredients) {
            if(ingredient.getStackSize() > 0) {
                return false;
            }
        }
        return true;
    }
}
