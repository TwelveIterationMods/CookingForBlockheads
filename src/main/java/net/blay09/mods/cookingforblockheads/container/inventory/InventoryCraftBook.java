package net.blay09.mods.cookingforblockheads.container.inventory;

import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class InventoryCraftBook extends InventoryCrafting {

    public InventoryCraftBook(Container container) {
        super(container, 3, 3);
    }

    public ItemStack tryCraft(ItemStack outputItem, NonNullList<ItemStack> craftMatrix, EntityPlayer player, KitchenMultiBlock multiBlock) {
        boolean requireContainer = CookingRegistry.doesItemRequireBucketForCrafting(outputItem);

        // Reset the simulation before we start
        List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.inventory);
        for (IKitchenItemProvider itemProvider : inventories) {
            itemProvider.resetSimulation();
        }

        SourceItem[] sourceItems = new SourceItem[9];

        // Find matching items from source inventories
        matrixLoop:
        for (int i = 0; i < craftMatrix.size(); i++) {
            ItemStack ingredient = craftMatrix.get(i);
            if (!ingredient.isEmpty()) {
                for (int j = 0; j < inventories.size(); j++) {
                    IKitchenItemProvider itemProvider = inventories.get(j);
                    SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed((it, count) -> ItemUtils.areItemStacksEqualWithWildcardIgnoreDurability(it, ingredient), 1, inventories, requireContainer, true);
                    if (sourceItem != null) {
                        sourceItems[i] = sourceItem;
                        continue matrixLoop;
                    }
                }
            }
        }

        // Populate the crafting grid
        for (int i = 0; i < sourceItems.length; i++) {
            setInventorySlotContents(i, sourceItems[i] != null ? sourceItems[i].getSourceStack() : ItemStack.EMPTY);
        }

        // Find the matching recipe and make sure it matches what the client expects
        IRecipe craftRecipe = CookingRegistry.findFoodRecipe(this, player.world);
        if (craftRecipe == null || craftRecipe.getRecipeOutput().isEmpty() || craftRecipe.getRecipeOutput().getItem() != outputItem.getItem()) {
            return ItemStack.EMPTY;
        }

        // Get the final result and remove ingredients
        ItemStack result = craftRecipe.getCraftingResult(this);
        if (!result.isEmpty()) {
            fireEventsAndHandleAchievements(player, result);
            for (int i = 0; i < getSizeInventory(); i++) {
                ItemStack itemStack = getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    if (sourceItems[i] != null) {
                        // Eat the ingredients
                        IKitchenItemProvider sourceProvider = sourceItems[i].getSourceProvider();
                        if (sourceItems[i].getSourceSlot() != -1) {
                            sourceProvider.resetSimulation();
                            sourceProvider.consumeSourceItem(sourceItems[i], 1, inventories, requireContainer);
                        }

                        // Return container items (like empty buckets)
                        ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
                        if (!containerItem.isEmpty()) {
                            ItemStack restStack = sourceProvider.returnItemStack(containerItem);
                            if (!restStack.isEmpty()) {
                                ItemHandlerHelper.giveItemToPlayer(player, restStack);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void fireEventsAndHandleAchievements(EntityPlayer player, ItemStack result) {
        FMLCommonHandler.instance().firePlayerCraftingEvent(player, result, this);
        result.onCrafting(player.world, player, 1);
    }


}
