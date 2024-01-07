package net.blay09.mods.cookingforblockheads.menu.inventory;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class InventoryCraftBook extends TransientCraftingContainer implements RecipeCraftingHolder { // TODO replace with CrFB one

    private RecipeHolder<?> recipeUsed;

    public InventoryCraftBook(AbstractContainerMenu menu) {
        super(menu, 3, 3);
    }

    public ItemStack tryCraft(ItemStack outputItem, NonNullList<ItemStack> craftMatrix, Player player, KitchenMultiBlock multiBlock) {
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
                IngredientPredicate ingredientPredicate = IngredientPredicateWithCacheImpl.of((it, count) -> ItemStack.isSameItem(it, ingredient) && count > 0, ingredient);
                for (int j = 0; j < inventories.size(); j++) {
                    IKitchenItemProvider itemProvider = inventories.get(j);
                    SourceItem sourceItem = itemProvider.findSourceAndMarkAsUsed(ingredientPredicate, 1, inventories, requireContainer, true);
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
        Level level = player.level();
        CraftingRecipe craftingRecipe = CookingRegistry.findFoodRecipe(this, level, RecipeType.CRAFTING, outputItem.getItem());
        if (craftingRecipe == null || craftingRecipe.getResultItem(level.registryAccess()).isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Make sure the recipe actually matches to prevent illegal crafting
        if (!craftingRecipe.matches(this, level)) {
            return ItemStack.EMPTY;
        }

        // Get the final result and remove ingredients
        ItemStack result = craftingRecipe.assemble(this, level.registryAccess());
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
                                    ItemEntity itemEntity = new ItemEntity(level, player.getX() + 0.5f, player.getY() + 0.5f, player.getZ() + 0.5f, restStack);
                                    float motion = 0.05F;
                                    itemEntity.setDeltaMovement(level.random.nextGaussian() * motion, level.random.nextGaussian() * motion + 0.2, level.random.nextGaussian() * motion);
                                    level.addFreshEntity(itemEntity);
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
        result.onCraftedBy(player.level(), player, 1);
        Balm.getHooks().firePlayerCraftingEvent(player, result, this);

        awardUsedRecipes(player, getItems());
    }



    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return recipeUsed;
    }
}
