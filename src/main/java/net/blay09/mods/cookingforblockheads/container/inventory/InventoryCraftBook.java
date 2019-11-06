package net.blay09.mods.cookingforblockheads.container.inventory;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class InventoryCraftBook extends CraftingInventory implements IRecipeHolder {

    private IRecipe<?> recipeUsed;

    public InventoryCraftBook(Container container) {
        super(container, 3, 3);
    }

    public ItemStack tryCraft(ItemStack outputItem, NonNullList<ItemStack> craftMatrix, PlayerEntity player, KitchenMultiBlock multiBlock) {
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
                    SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed((it, count) -> it.isItemEqualIgnoreDurability(ingredient) && count > 0, 1, inventories, requireContainer, true);
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

        // Make sure the recipe actually matches to prevent illegal crafting
        if (!craftRecipe.matches(this, player.world)) {
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
                        if (sourceProvider == null) {
                            continue;
                        }

                        ItemStack containerItem = ForgeHooks.getContainerItem(itemStack);
                        if (sourceItems[i].getSourceSlot() != -1) {
                            sourceProvider.resetSimulation();
                            sourceProvider.consumeSourceItem(sourceItems[i], 1, inventories, requireContainer);
                        }

                        // Return container items (like empty buckets)
                        if (!containerItem.isEmpty()) {
                            ItemStack restStack = sourceProvider.returnItemStack(containerItem, sourceItems[i]);
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

    private void fireEventsAndHandleAchievements(PlayerEntity player, ItemStack result) {
        result.onCrafting(player.world, player, 1);
        BasicEventHooks.firePlayerCraftingEvent(player, result, this);

        this.onCrafting(player);
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return recipeUsed;
    }
}
