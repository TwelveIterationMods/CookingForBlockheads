package net.blay09.mods.cookingbook.food;

import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.api.FoodRegistryInitEvent;
import net.blay09.mods.cookingbook.compatibility.PamsHarvestcraft;
import net.blay09.mods.cookingbook.container.InventoryCraftBook;
import net.blay09.mods.cookingbook.food.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FoodRegistry {

    private static final List<IRecipe> recipeList = new ArrayList<>();
    private static final ArrayListMultimap<Item, FoodRecipe> foodItems = ArrayListMultimap.create();

    public static void init() {
        recipeList.clear();
        foodItems.clear();

        FoodRegistryInitEvent init = new FoodRegistryInitEvent();
        MinecraftForge.EVENT_BUS.post(init);

        Collection<ItemStack> nonFoodRecipes = init.getNonFoodRecipes();

        // Crafting Recipes of Food Items
        for(Object obj : CraftingManager.getInstance().getRecipeList()) {
            IRecipe recipe = (IRecipe) obj;
            ItemStack output = recipe.getRecipeOutput();
            if(output != null) {
                if (output.getItem() instanceof ItemFood) {
                    if (PamsHarvestcraft.isWeirdBrokenRecipe(recipe)) {
                        continue;
                    }
                    addFoodRecipe(recipe);
                } else {
                    for (ItemStack itemStack : nonFoodRecipes) {
                        if (areItemStacksEqualForCrafting(recipe.getRecipeOutput(), itemStack)) {
                            addFoodRecipe(recipe);
                            break;
                        }
                    }
                }
            }
        }

        // Smelting Recipes of Food Items
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
                foodItems.put(resultStack.getItem(), new SmeltingFood(resultStack, sourceStack));
            } else {
                for(ItemStack itemStack : nonFoodRecipes) {
                    if (areItemStacksEqualForCrafting(resultStack, itemStack)) {
                        foodItems.put(resultStack.getItem(), new SmeltingFood(resultStack, sourceStack));
                        break;
                    }
                }
            }
        }
    }

    public static boolean areItemStacksEqualForCrafting(ItemStack first, ItemStack second) {
        if(first == null || second == null) {
            return false;
        }
        if(first.getHasSubtypes()) {
            return first.getItem() == second.getItem() && (first.getItemDamage() == second.getItemDamage() || (first.getItemDamage() == OreDictionary.WILDCARD_VALUE || second.getItemDamage() == OreDictionary.WILDCARD_VALUE));
        } else {
            return first.getItem() == second.getItem();
        }
    }

    public static void addFoodRecipe(IRecipe recipe) {
        ItemStack output = recipe.getRecipeOutput();
        if(output != null) {
            recipeList.add(recipe);
            if (recipe instanceof ShapedRecipes) {
                foodItems.put(output.getItem(), new ShapedCraftingFood((ShapedRecipes) recipe));
            } else if (recipe instanceof ShapelessRecipes) {
                foodItems.put(output.getItem(), new ShapelessCraftingFood((ShapelessRecipes) recipe));
            } else if (recipe instanceof ShapelessOreRecipe) {
                foodItems.put(output.getItem(), new ShapelessOreCraftingFood((ShapelessOreRecipe) recipe));
            } else if (recipe instanceof ShapedOreRecipe) {
                foodItems.put(output.getItem(), new ShapedOreCraftingFood((ShapedOreRecipe) recipe));
            }
        }
    }

    public static Collection<FoodRecipe> getFoodRecipes() {
        return foodItems.values();
    }

    public static boolean isAvailableFor(FoodIngredient[] craftMatrix, IInventory[] inventories) {
        int[][] usedStackSize = new int[inventories.length][];
        for(int i = 0; i < usedStackSize.length; i++) {
            usedStackSize[i] = new int[inventories[i].getSizeInventory()];
        }
        boolean[] itemFound = new boolean[craftMatrix.length];
        for(int i = 0; i < craftMatrix.length; i++) {
            if(craftMatrix[i] == null || craftMatrix[i].isToolItem()) {
                itemFound[i] = true;
                continue;
            }
            for(int j = 0; j < inventories.length; j++) {
                for (int k = 0; k < inventories[j].getSizeInventory(); k++) {
                    ItemStack itemStack = inventories[j].getStackInSlot(k);
                    if (itemStack != null && craftMatrix[i].isValidItem(itemStack) && itemStack.stackSize - usedStackSize[j][k] > 0) {
                        usedStackSize[j][k]++;
                        itemFound[i] = true;
                        break;
                    }
                }
            }
        }
        for(int i = 0; i < itemFound.length; i++) {
            if(!itemFound[i]) {
                return false;
            }
        }
        return true;
    }

    public static IRecipe findMatchingRecipe(InventoryCraftBook craftBook, World worldObj) {
        for(IRecipe recipe : recipeList) {
            if(recipe.matches(craftBook, worldObj)) {
                return recipe;
            }
        }
        return null;
    }
}
