package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.addon.HarvestCraftAddon;
import net.blay09.mods.cookingforblockheads.api.SinkHandler;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.blay09.mods.cookingforblockheads.container.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.container.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.blay09.mods.cookingforblockheads.registry.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CookingRegistry {

    private static final AtomicInteger id = new AtomicInteger();
    private static final List<IRecipe> recipeList = Lists.newArrayList();
    private static final ArrayListMultimap<Item, FoodRecipe> foodItems = ArrayListMultimap.create();
    private static final List<ItemStack> tools = Lists.newArrayList();
    private static final Map<ItemStack, Integer> ovenFuelItems = Maps.newHashMap();
    private static final Map<ItemStack, ItemStack> ovenRecipes = Maps.newHashMap();
    private static final Map<ItemStack, SinkHandler> sinkHandlers = Maps.newHashMap();
    private static final Map<ItemStack, ToastHandler> toastHandlers = Maps.newHashMap();

    public static void initFoodRegistry() {
        recipeList.clear();
        foodItems.clear();
        id.set(0);

        FoodRegistryInitEvent init = new FoodRegistryInitEvent();
        MinecraftForge.EVENT_BUS.post(init);

        Collection<ItemStack> nonFoodRecipes = init.getNonFoodRecipes();

        // Crafting Recipes of Food Items
        for(Object obj : CraftingManager.getInstance().getRecipeList()) {
            IRecipe recipe = (IRecipe) obj;
            ItemStack output = recipe.getRecipeOutput();
            if(output != null) {
                if (output.getItem() instanceof ItemFood) {
                    if (HarvestCraftAddon.isWeirdBrokenRecipe(recipe)) {
                        continue;
                    }
                    addFoodRecipe(recipe);
                } else {
                    for (ItemStack itemStack : nonFoodRecipes) {
                        if (ItemUtils.areItemStacksEqualWithWildcard(recipe.getRecipeOutput(), itemStack)) {
                            addFoodRecipe(recipe);
                            break;
                        }
                    }
                }
            }
        }

        // Smelting Recipes of Food Items
        for(Object obj : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
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
                foodItems.put(resultStack.getItem(), new SmeltingFood(id.incrementAndGet(), resultStack, sourceStack));
            } else {
                for(ItemStack itemStack : nonFoodRecipes) {
                    if (ItemUtils.areItemStacksEqualWithWildcard(resultStack, itemStack)) {
                        foodItems.put(resultStack.getItem(), new SmeltingFood(id.incrementAndGet(), resultStack, sourceStack));
                        break;
                    }
                }
            }
        }
    }

    public static void addFoodRecipe(IRecipe recipe) {
        ItemStack output = recipe.getRecipeOutput();
        if(output != null) {
            recipeList.add(recipe);
            if (recipe instanceof ShapedRecipes) {
                foodItems.put(output.getItem(), new ShapedCraftingFood(id.incrementAndGet(), (ShapedRecipes) recipe));
            } else if (recipe instanceof ShapelessRecipes) {
                foodItems.put(output.getItem(), new ShapelessCraftingFood(id.incrementAndGet(), (ShapelessRecipes) recipe));
            } else if (recipe instanceof ShapelessOreRecipe) {
                foodItems.put(output.getItem(), new ShapelessOreCraftingFood(id.incrementAndGet(), (ShapelessOreRecipe) recipe));
            } else if (recipe instanceof ShapedOreRecipe) {
                foodItems.put(output.getItem(), new ShapedOreCraftingFood(id.incrementAndGet(), (ShapedOreRecipe) recipe));
            }
        }
    }

    public static Collection<FoodRecipe> getFoodRecipes() {
        return foodItems.values();
    }

    public static void addToolItem(ItemStack toolItem) {
        tools.add(toolItem);
    }

    public static boolean isToolItem(ItemStack itemStack) {
        if(itemStack == null) {
            return false;
        }
        for(ItemStack toolItem : tools) {
            if(ItemUtils.areItemStacksEqualWithWildcard(toolItem, itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static void addOvenFuel(ItemStack itemStack, int fuelTime) {
        ovenFuelItems.put(itemStack, fuelTime);
    }

    public static int getOvenFuelTime(ItemStack itemStack) {
        for(Map.Entry<ItemStack, Integer> entry : ovenFuelItems.entrySet()) {
            if(ItemUtils.areItemStacksEqualWithWildcard(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public static void addSmeltingItem(ItemStack source, ItemStack result) {
        ovenRecipes.put(source, result);
    }

    public static ItemStack getSmeltingResult(ItemStack itemStack) {
        for(Map.Entry<ItemStack, ItemStack> entry : ovenRecipes.entrySet()) {
            if(ItemUtils.areItemStacksEqualWithWildcard(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static void addToastHandler(ItemStack itemStack, ToastHandler toastHandler) {
        toastHandlers.put(itemStack, toastHandler);
    }

    public static ItemStack getToastOutput(ItemStack itemStack) {
        for(Map.Entry<ItemStack, ToastHandler> entry : toastHandlers.entrySet()) {
            if(ItemUtils.areItemStacksEqualWithWildcard(entry.getKey(), itemStack)) {
                return entry.getValue().getToasterOutput(itemStack);
            }
        }
        return null;
    }

    public static void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        sinkHandlers.put(itemStack, sinkHandler);
    }

    public static ItemStack getSinkOutput(ItemStack itemStack) {
        if(itemStack == null) {
            return null;
        }
        for(Map.Entry<ItemStack, SinkHandler> entry : sinkHandlers.entrySet()) {
            if(ItemUtils.areItemStacksEqualWithWildcard(entry.getKey(), itemStack)) {
                return entry.getValue().getSinkOutput(itemStack);
            }
        }
        return null;
    }

    public static Collection<FoodRecipeWithStatus> findAvailableRecipes(InventoryPlayer inventory, KitchenMultiBlock multiBlock) {
        List<FoodRecipeWithStatus> result = Lists.newArrayList();
        for(FoodRecipe recipe : getFoodRecipes()) {
            RecipeStatus recipeStatus = getRecipeStatus(recipe, inventory, multiBlock);
            if(recipeStatus != RecipeStatus.MISSING_INGREDIENTS) {
                result.add(new FoodRecipeWithStatus(recipe.getId(), recipe.getOutputItem(), recipe.getCraftMatrix(), recipe.getType(), recipeStatus));
            }
        }
        return result;
    }

    public static RecipeStatus getRecipeStatus(FoodRecipe recipe, InventoryPlayer inventory, KitchenMultiBlock multiBlock) {
        List<? extends IKitchenItemProvider> inventories = multiBlock != null ? multiBlock.getSourceInventories(inventory) : Collections.singletonList(new KitchenItemProvider(new InvWrapper(inventory)));
        for(IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
        }
        List<FoodIngredient> craftMatrix = recipe.getCraftMatrix();
        boolean[] itemFound = new boolean[craftMatrix.size()];
        matrixLoop:for(int i = 0; i < craftMatrix.size(); i++) {
            FoodIngredient ingredient = craftMatrix.get(i);
            if(ingredient == null) {
                itemFound[i] = true;
                continue;
            }
            for (IKitchenItemProvider itemProvider : inventories) {
                for (int j = 0; j < itemProvider.getSlots(); j++) {
                    ItemStack itemStack = itemProvider.getStackInSlot(j);
                    if (itemStack != null && ingredient.isValidItem(itemStack) && itemProvider.useItemStack(j, 1, true) != null) {
                        itemFound[i] = true;
                        continue matrixLoop;
                    }
                }
            }
        }
        boolean missingTools = false;
        for(int i = 0; i < itemFound.length; i++) {
            if(!itemFound[i]) {
                if(craftMatrix.get(i).isToolItem()) {
                    missingTools = true;
                    continue;
                }
                return RecipeStatus.MISSING_INGREDIENTS;
            }
        }
        return missingTools ? RecipeStatus.MISSING_TOOLS : RecipeStatus.AVAILABLE;
    }

    public static IRecipe findFoodRecipe(InventoryCraftBook craftMatrix, World world) {
        for(IRecipe recipe : recipeList) {
            if(recipe.matches(craftMatrix, world)) {
                return recipe;
            }
        }
        return null;
    }
}
