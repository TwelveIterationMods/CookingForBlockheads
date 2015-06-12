package net.blay09.mods.cookingbook.container;

import cpw.mods.fml.common.FMLCommonHandler;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
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

public class InventoryCraftBook extends InventoryCrafting {

    private final IInventory sourceInventory;
    private final int[] sourceInventorySlots = new int[9];
    private IRecipe currentRecipe;

    public InventoryCraftBook(Container container, IInventory sourceInventory) {
        super(container, 3, 3);
        this.sourceInventory = sourceInventory;
    }

    public IRecipe prepareRecipe(EntityPlayer player, IInventory inventory, FoodRecipe recipe) {
        FoodIngredient[] ingredients = recipe.getCraftMatrix();
        int[] usedStackSize = new int[inventory.getSizeInventory()];
        for(int i = 0; i < getSizeInventory(); i++) {
            setInventorySlotContents(i, null);
            sourceInventorySlots[i] = -1;
        }
        for(int i = 0; i < ingredients.length; i++) {
            if(ingredients[i] != null) {
                for(int j = 0; j < inventory.getSizeInventory(); j++) {
                    ItemStack itemStack = inventory.getStackInSlot(j);
                    if(itemStack != null && ingredients[i].isValidItem(itemStack) && itemStack.stackSize - usedStackSize[j] > 0) {
                        usedStackSize[j]++;
                        setInventorySlotContents(i, itemStack);
                        sourceInventorySlots[i] = j;
                        break;
                    }
                }
            }
        }
        currentRecipe = FoodRegistry.findMatchingRecipe(this, player.worldObj);
        return currentRecipe;
    }

    public ItemStack craft(EntityPlayer player, IInventory sourceInventory, FoodRecipe recipe) {
        prepareRecipe(player, sourceInventory, recipe);
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
                        if(itemStack.getItem().hasContainerItem(itemStack)) {
                            // Fire PlayerDestroyItem event
                            ItemStack containerItem = itemStack.getItem().getContainerItem(itemStack);
                            if(containerItem != null && containerItem.isItemStackDamageable() && itemStack.getMetadata() > itemStack.getMaxDurability()) {
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

                        if(sourceInventorySlots[i] != -1 && getStackInSlot(i) == null) {
                            sourceInventory.setInventorySlotContents(sourceInventorySlots[i], null);
                        }
                    }
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
        IRecipe craftingRecipe = FoodRegistry.findMatchingRecipe(this, player.worldObj);
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
}
