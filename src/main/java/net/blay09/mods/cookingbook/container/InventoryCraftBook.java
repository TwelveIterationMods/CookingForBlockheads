package net.blay09.mods.cookingbook.container;

import cpw.mods.fml.common.FMLCommonHandler;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenItemProvider;
import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.blay09.mods.cookingbook.registry.food.FoodIngredient;
import net.blay09.mods.cookingbook.registry.food.FoodRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.ArrayList;
import java.util.List;

public class InventoryCraftBook extends InventoryCrafting {

    private final int[] sourceInventories = new int[9];
    private final int[] sourceInventorySlots = new int[9];
    private final List<IKitchenItemProvider> sourceProviders = new ArrayList<>();
    private IRecipe currentRecipe;

    private List<IInventory> inventories;
    private List<IKitchenItemProvider> itemProviders;

    public InventoryCraftBook(Container container) {
        super(container, 3, 3);
    }

    public IRecipe prepareRecipe(EntityPlayer player, FoodRecipe recipe) {
        List<FoodIngredient> ingredients = recipe.getCraftMatrix();
        int[][] usedStackSize = new int[inventories.size()][];
        for(int i = 0; i < usedStackSize.length; i++) {
            usedStackSize[i] = new int[inventories.get(i).getSizeInventory()];
        }
        for(int i = 0; i < getSizeInventory(); i++) {
            setInventorySlotContents(i, null);
            sourceInventories[i] = -1;
            sourceInventorySlots[i] = -1;
        }
        ingredientLoop:for(int i = 0; i < ingredients.size(); i++) {
            if(ingredients.get(i) != null) {
                sourceProviders.clear();
                for(IKitchenItemProvider itemProvider : itemProviders) {
                    itemProvider.clearCraftingBuffer();
                    for(ItemStack providedStack : itemProvider.getProvidedItemStacks()) {
                        if(ingredients.get(i).isValidItem(providedStack)) {
                            ItemStack itemStack = providedStack.copy();
                            if(itemProvider.addToCraftingBuffer(itemStack)) {
                                sourceProviders.add(itemProvider);
                                setInventorySlotContents(i, itemStack);
                                continue ingredientLoop;
                            }
                        }
                    }
                }
                for(int j = 0; j < inventories.size(); j++) {
                    for (int k = 0; k < inventories.get(j).getSizeInventory(); k++) {
                        ItemStack itemStack = inventories.get(j).getStackInSlot(k);
                        if (itemStack != null && ingredients.get(i).isValidItem(itemStack) && itemStack.stackSize - usedStackSize[j][k] > 0) {
                            usedStackSize[j][k]++;
                            setInventorySlotContents(i, itemStack);
                            sourceInventories[i] = j;
                            sourceInventorySlots[i] = k;
                            continue ingredientLoop;
                        }
                    }
                }
            }
            currentRecipe = null;
            return null;
        }
        currentRecipe = CookingRegistry.findMatchingFoodRecipe(this, player.worldObj);
        return currentRecipe;
    }

    public ItemStack craft(EntityPlayer player, FoodRecipe recipe) {
        prepareRecipe(player, recipe);
        if(currentRecipe == null) {
            return null;
        }
        if(currentRecipe.matches(this, player.worldObj)) {
            ItemStack craftingResult = currentRecipe.getCraftingResult(this);
            if(craftingResult != null) {
                // Fire FML Events
                FMLCommonHandler.instance().firePlayerCraftingEvent(player, craftingResult, this);
                craftingResult.onCrafting(player.worldObj, player, 1);
                // Handle Vanilla Achievements
                if(craftingResult.getItem() == Items.bread) {
                    player.addStat(AchievementList.makeBread, 1);
                } else if(craftingResult.getItem() == Items.cake) {
                    player.addStat(AchievementList.bakeCake, 1);
                }
                // Kill ingredients
                for(int i = 0; i < getSizeInventory(); i++) {
                    ItemStack itemStack = getStackInSlot(i);
                    if(itemStack != null) {
                        decrStackSize(i, 1);
                        if(itemStack.getItem().hasContainerItem(itemStack) && sourceInventories[i] != -1) {
                            // Fire PlayerDestroyItem event
                            ItemStack containerItem = itemStack.getItem().getContainerItem(itemStack);
                            if(containerItem != null && containerItem.isItemStackDamageable() && itemStack.getItemDamage() > itemStack.getMaxDamage()) {
                                MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, containerItem));
                                continue;
                            }
                            // Put container item back into crafting grid or drop it
                            if(!itemStack.getItem().doesContainerItemLeaveCraftingGrid(itemStack) || !player.inventory.addItemStackToInventory(containerItem)) {
                                if(getStackInSlot(i) == null) {
                                    setInventorySlotContents(i, containerItem);
                                } else {
                                    player.dropPlayerItemWithRandomChoice(containerItem, false);
                                }
                            }
                        }

                        if (sourceInventories[i] != -1 && sourceInventorySlots[i] != -1 && getStackInSlot(i) == null) {
                            inventories.get(sourceInventories[i]).setInventorySlotContents(sourceInventorySlots[i], null);
                        }
                    }
                }
                for(IKitchenItemProvider itemProvider : sourceProviders) {
                    itemProvider.craftingComplete();
                }
            }
            return craftingResult;
        }
        return null;
    }

    public boolean canMouseItemHold(EntityPlayer player, FoodRecipe recipe) {
        ItemStack mouseItem = player.inventory.getItemStack();
        if(mouseItem == null) {
            return true;
        }
        IRecipe craftingRecipe = CookingRegistry.findMatchingFoodRecipe(this, player.worldObj);
        if(craftingRecipe == null) {
            return false;
        }
        ItemStack craftingResult = craftingRecipe.getCraftingResult(this);
        if(!craftingResult.isItemEqual(mouseItem)) {
            return false;
        }
        if(mouseItem.stackSize + craftingResult.stackSize > mouseItem.getMaxStackSize()) {
            return false;
        }
        return true;
    }

    public boolean matches(World worldObj) {
        return currentRecipe != null && currentRecipe.matches(this, worldObj);
    }

    /**
     * SERVER ONLY
     * @param inventories
     */
    public void setInventories(List<IInventory> inventories) {
        this.inventories = inventories;
    }

    /**
     * SERVER ONLY
     * @param itemProviders
     */
    public void setItemProviders(List<IKitchenItemProvider> itemProviders) {
        this.itemProviders = itemProviders;
    }
}
