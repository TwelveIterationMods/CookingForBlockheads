package net.blay09.mods.cookingforblockheads.menu.inventory;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InventoryCraftBook extends CraftingContainer implements RecipeHolder {

    private Recipe<?> recipeUsed;

    public InventoryCraftBook(AbstractContainerMenu menu) {
        super(menu, 3, 3);
    }

    public ItemStack tryCraft(ItemStack outputItem, NonNullList<ItemStack> craftMatrix, Player player, KitchenMultiBlock multiBlock) {
        boolean requireContainer = CookingRegistry.doesItemRequireBucketForCrafting(outputItem);

        // Reset the simulation before we start
        List<IKitchenItemProvider> inventories = CookingRegistry.getItemProviders(multiBlock, player.getInventory());
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
                    SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed((it, count) -> it.sameItemStackIgnoreDurability(ingredient) && count > 0, 1, inventories, requireContainer, true);
                    if (sourceItem != null) {
                        sourceItems[i] = sourceItem;
                        continue matrixLoop;
                    }
                }
            }
        }

        // Populate the crafting grid
        for (int i = 0; i < sourceItems.length; i++) {
            setItem(i, sourceItems[i] != null ? sourceItems[i].getSourceStack() : ItemStack.EMPTY);
        }

        // Find the matching recipe and make sure it matches what the client expects
        Recipe craftRecipe = CookingRegistry.findFoodRecipe(this, player.level, RecipeType.CRAFTING, outputItem.getItem());
        if (craftRecipe == null || craftRecipe.getResultItem().isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Make sure the recipe actually matches to prevent illegal crafting
        if (!craftRecipe.matches(this, player.level)) {
            return ItemStack.EMPTY;
        }

        // Get the final result and remove ingredients
        ItemStack result = craftRecipe.assemble(this);
        if (!result.isEmpty()) {
            fireEventsAndHandleAchievements(player, result);
            for (int i = 0; i < getContainerSize(); i++) {
                ItemStack itemStack = getItem(i);
                if (!itemStack.isEmpty()) {
                    if (sourceItems[i] != null) {
                        // Eat the ingredients
                        IKitchenItemProvider sourceProvider = sourceItems[i].getSourceProvider();
                        if (sourceProvider == null) {
                            continue;
                        }

                        ItemStack containerItem = Balm.getHooks().getCraftingRemainingItem(itemStack);
                        if (sourceItems[i].getSourceSlot() != -1) {
                            sourceProvider.resetSimulation();
                            sourceProvider.consumeSourceItem(sourceItems[i], 1, inventories, requireContainer);
                        }

                        // Return container items (like empty buckets)
                        if (!containerItem.isEmpty()) {
                            ItemStack restStack = sourceProvider.returnItemStack(containerItem, sourceItems[i]);
                            if (!restStack.isEmpty()) {
                                if (!player.getInventory().add(restStack)) {
                                    ItemEntity itemEntity = new ItemEntity(player.level, player.getX() + 0.5f, player.getY() + 0.5f, player.getZ() + 0.5f, restStack);
                                    float motion = 0.05F;
                                    itemEntity.setDeltaMovement(player.level.random.nextGaussian() * motion, player.level.random.nextGaussian() * motion + 0.2, player.level.random.nextGaussian() * motion);
                                    player.level.addFreshEntity(itemEntity);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void fireEventsAndHandleAchievements(Player player, ItemStack result) {
        result.onCraftedBy(player.level, player, 1);
        Balm.getHooks().firePlayerCraftingEvent(player, result, this);

        awardUsedRecipes(player);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return recipeUsed;
    }
}
