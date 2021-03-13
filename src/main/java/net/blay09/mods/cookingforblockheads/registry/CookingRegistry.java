package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.*;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingforblockheads.compat.HarvestCraftAddon;
import net.blay09.mods.cookingforblockheads.container.inventory.InventoryCraftBook;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.blay09.mods.cookingforblockheads.registry.recipe.GeneralFoodRecipe;
import net.blay09.mods.cookingforblockheads.registry.recipe.SmeltingFood;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.*;

public class CookingRegistry {

    private static final List<IRecipe<IInventory>> recipeList = Lists.newArrayList();
    private static final ArrayListMultimap<ResourceLocation, FoodRecipe> foodItems = ArrayListMultimap.create();
    private static final NonNullList<ItemStack> tools = NonNullList.create();
    private static final Map<ItemStack, Integer> ovenFuelItems = Maps.newHashMap();
    private static final Map<ItemStack, ItemStack> ovenRecipes = Maps.newHashMap();
    private static final Map<ItemStack, SinkHandler> sinkHandlers = Maps.newHashMap();
    private static final Map<ItemStack, ToasterHandler> toastHandlers = Maps.newHashMap();
    private static final NonNullList<ItemStack> waterItems = NonNullList.create();
    private static final NonNullList<ItemStack> milkItems = NonNullList.create();
    private static final List<ISortButton> customSortButtons = Lists.newArrayList();

    private static Collection<ItemStack> nonFoodRecipes = Collections.emptyList();

    public static void initFoodRegistry(RecipeManager recipeManager) {
        recipeList.clear();
        foodItems.clear();

        FoodRegistryInitEvent init = new FoodRegistryInitEvent();
        MinecraftForge.EVENT_BUS.post(init);

        nonFoodRecipes = init.getNonFoodRecipes();

        // Crafting Recipes of Food Items
        for (IRecipe recipe : recipeManager.getRecipes()) {
            // Skip smoking and campfire cooking to prevent duplicate recipes
            if (recipe.getType() == IRecipeType.SMOKING || recipe.getType() == IRecipeType.CAMPFIRE_COOKING) {
                continue;
            }

            ItemStack output = recipe.getRecipeOutput();

            //noinspection ConstantConditions
            if (output == null) {
                CookingForBlockheads.logger.warn("Recipe " + recipe.getId() + " returned a null ItemStack in getRecipeOutput - this is bad! The developer of " + recipe.getId().getNamespace() + " should return an empty ItemStack instead to avoid problems.");
                continue;
            }

            if (!output.isEmpty()) {
                if (output.getItem().isFood()) {
                    if (!HarvestCraftAddon.isWeirdConversionRecipe(recipe)) {
                        addFoodRecipe(recipe);
                    }
                } else {
                    // TODO Make nonFoodRecipes a map to improve lookup performance
                    for (ItemStack itemStack : nonFoodRecipes) {
                        if (recipe.getRecipeOutput().isItemEqual(itemStack)) {
                            addFoodRecipe(recipe);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean isNonFoodRecipe(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        for (ItemStack nonFoodStack : nonFoodRecipes) {
            if (itemStack.isItemEqual(nonFoodStack)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void addFoodRecipe(IRecipe<? extends IInventory> recipe) {
        ItemStack output = recipe.getRecipeOutput();
        if (!output.isEmpty() && !recipe.getIngredients().isEmpty()) {
            FoodRecipe foodRecipe;
            if (recipe instanceof AbstractCookingRecipe) {
                foodRecipe = new SmeltingFood(recipe);
            } else if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
                // Limit to ShapedRecipe and ShapelessRecipe, to not also get custom mod recipes that may require
                // special inventories
                foodRecipe = new GeneralFoodRecipe(recipe);
            } else {
                return;
            }

            recipeList.add((IRecipe<IInventory>) recipe);
            foodItems.put(output.getItem().getRegistryName(), foodRecipe);
        }
    }

    public static Multimap<ResourceLocation, FoodRecipe> getFoodRecipes() {
        return foodItems;
    }

    public static Collection<FoodRecipe> getFoodRecipes(ItemStack outputItem) {
        return foodItems.get(outputItem.getItem().getRegistryName());
    }

    public static Collection<FoodRecipe> getFoodRecipes(ResourceLocation outputItem) {
        return foodItems.get(outputItem);
    }

    public static void addToolItem(ItemStack toolItem) {
        tools.add(toolItem);
    }

    public static boolean isToolItem(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        for (ItemStack toolItem : tools) {
            if (toolItem.isItemEqualIgnoreDurability(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isToolItem(Ingredient ingredient) {
        for (ItemStack itemStack : ingredient.getMatchingStacks()) {
            if (isToolItem(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static void addOvenFuel(ItemStack itemStack, int fuelTime) {
        ovenFuelItems.put(itemStack, fuelTime);
    }

    public static int getOvenFuelTime(ItemStack itemStack) {
        for (Map.Entry<ItemStack, Integer> entry : ovenFuelItems.entrySet()) {
            if (entry.getKey().isItemEqual(itemStack)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public static void addSmeltingItem(ItemStack source, ItemStack result) {
        ovenRecipes.put(source, result);
    }

    public static ItemStack getSmeltingResult(ItemStack itemStack) {
        for (Map.Entry<ItemStack, ItemStack> entry : ovenRecipes.entrySet()) {
            if (entry.getKey().isItemEqual(itemStack)) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    public static void addToasterHandler(ItemStack itemStack, ToasterHandler toastHandler) {
        toastHandlers.put(itemStack, toastHandler);
    }

    @Nullable
    public static ToasterHandler getToasterHandler(ItemStack itemStack) {
        for (Map.Entry<ItemStack, ToasterHandler> entry : toastHandlers.entrySet()) {
            if (entry.getKey().isItemEqual(itemStack)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        sinkHandlers.put(itemStack, sinkHandler);
    }

    public static ItemStack getSinkOutput(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        for (Map.Entry<ItemStack, SinkHandler> entry : sinkHandlers.entrySet()) {
            if (entry.getKey().isItemEqual(itemStack)) {
                return entry.getValue().getSinkOutput(itemStack);
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    private static SourceItem findAnyItemStack(ItemStack checkStack, List<IKitchenItemProvider> inventories, boolean requireBucket) {
        if (checkStack.isEmpty()) {
            return null;
        }

        for (int i = 0; i < inventories.size(); i++) {
            IKitchenItemProvider itemProvider = inventories.get(i);
            IngredientPredicate predicate = (it, count) -> it.isItemEqualIgnoreDurability(checkStack) && count > 0;
            SourceItem found = itemProvider.findSource(predicate, 1, inventories, requireBucket, true);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    public static List<SourceItem> findSourceCandidates(FoodIngredient ingredient, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean isNoFilter) {
        List<SourceItem> sourceList = new ArrayList<>();

        ItemStack[] variants = ingredient.getItemStacks();
        for (ItemStack checkStack : variants) {
            SourceItem sourceItem = CookingRegistry.findAnyItemStack(checkStack, inventories, requireBucket);
            ItemStack foundStack = sourceItem != null ? sourceItem.getSourceStack() : ItemStack.EMPTY;
            if (foundStack.isEmpty()) {
                if (isNoFilter || ingredient.isToolItem()) {
                    sourceItem = new SourceItem(null, -1, checkStack);
                }
            }

            if (sourceItem != null) {
                sourceList.add(sourceItem);
            }
        }

        SourceItem sourceItem = !sourceList.isEmpty() ? sourceList.get(0) : null;
        if (sourceItem != null && sourceItem.getSourceProvider() != null) {
            sourceItem.getSourceProvider().markAsUsed(sourceItem, 1, inventories, requireBucket);
        }

        return sourceList;
    }

    public static boolean consumeBucket(List<IKitchenItemProvider> inventories, boolean simulate) {
        ItemStack bucketStack = new ItemStack(Items.BUCKET);
        for (int i = 0; i < inventories.size(); i++) {
            IKitchenItemProvider itemProvider = inventories.get(i);
            IngredientPredicate predicate = (it, count) -> it.isItemEqual(bucketStack) && count > 0;
            SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed(predicate, 1, inventories, false, simulate);
            if (sourceItem != null) {
                return true;
            }
        }

        return false;
    }

    public static RecipeStatus getRecipeStatus(FoodRecipe recipe, List<IKitchenItemProvider> inventories, boolean hasOven) {
        boolean requireBucket = doesItemRequireBucketForCrafting(recipe.getOutputItem());
        for (IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
        }

        List<FoodIngredient> craftMatrix = recipe.getCraftMatrix();
        boolean missingTools = false;
        for (FoodIngredient ingredient : craftMatrix) {
            if (ingredient != null) {
                List<SourceItem> sourceList = findSourceCandidates(ingredient, inventories, requireBucket, false);
                if (sourceList.isEmpty()) {
                    return RecipeStatus.MISSING_INGREDIENTS;
                }

                if (sourceList.stream().allMatch(it -> it.getSourceProvider() == null)) {
                    missingTools = true;
                }
            }
        }

        // Do not mark smeltable recipes as available unless an oven is present.
        if (recipe.getType() == RecipeType.SMELTING && !hasOven) {
            return RecipeStatus.MISSING_TOOLS;
        }

        return missingTools ? RecipeStatus.MISSING_TOOLS : RecipeStatus.AVAILABLE;
    }

    public static List<IKitchenItemProvider> getItemProviders(@Nullable KitchenMultiBlock multiBlock, PlayerInventory inventory) {
        return multiBlock != null ? multiBlock.getItemProviders(inventory) : Lists.newArrayList(new KitchenItemProvider(new InvWrapper(inventory)));
    }

    @Nullable
    public static IRecipe findFoodRecipe(InventoryCraftBook craftMatrix, World world, IRecipeType<?> recipeType, Item expectedItem) {
        for (IRecipe<IInventory> recipe : recipeList) {
            if (recipe.getType() == recipeType && recipe.matches(craftMatrix, world) && recipe.getRecipeOutput().getItem() == expectedItem) {
                return recipe;
            }
        }

        return null;
    }

    public static void addWaterItem(ItemStack waterItem) {
        waterItems.add(waterItem);
    }

    public static void addMilkItem(ItemStack milkItem) {
        milkItems.add(milkItem);
    }

    public static void addSortButton(ISortButton button) {
        customSortButtons.add(button);
    }

    public static NonNullList<ItemStack> getWaterItems() {
        return waterItems;
    }

    public static NonNullList<ItemStack> getMilkItems() {
        return milkItems;
    }

    public static List<ISortButton> getSortButtons() {
        return customSortButtons;
    }

    public static boolean doesItemRequireBucketForCrafting(ItemStack outputItem) {
        ItemStack containerItem = ForgeHooks.getContainerItem(outputItem);
        if (!containerItem.isEmpty() && containerItem.getItem() == Items.BUCKET) {
            return true;
        }
        ResourceLocation registryName = outputItem.getItem().getRegistryName();
        return registryName != null && registryName.getPath().contains("bucket");
    }
}
