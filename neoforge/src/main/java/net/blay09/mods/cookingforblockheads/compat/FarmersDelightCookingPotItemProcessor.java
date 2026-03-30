package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.IngredientToken;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenOperation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;
import java.util.Optional;

public class FarmersDelightCookingPotItemProcessor implements KitchenItemProcessor {

    private final RecipeType<?> recipeType;
    private final Level level;
    private final IItemHandler inputHandler;

    public FarmersDelightCookingPotItemProcessor(RecipeType<?> recipeType, Level level, IItemHandler inputHandler) {
        this.recipeType = recipeType;
        this.level = level;
        this.inputHandler = inputHandler;
    }

    @Override
    public boolean canProcess(RecipeType<?> recipeType) {
        return this.recipeType == recipeType;
    }

    @Override
    public KitchenOperation processRecipe(Recipe<?> recipe, List<IngredientToken> ingredientTokens) {
        if (recipe instanceof CookingPotRecipe cookingPotRecipe && isObstructed(cookingPotRecipe, ingredientTokens)) {
            return ObstructedCookingPotOperation.INSTANCE;
        }

        if (!canInsertAll(ingredientTokens)) {
            return ObstructedCookingPotOperation.INSTANCE;
        }

        for (int i = 0; i < ingredientTokens.size(); i++) {
            final var ingredientToken = ingredientTokens.get(i);
            final var itemStack = ingredientToken.consume();
            final var slot = KitchenCookingPotRecipeHandler.mapIngredientToSlot(i);
            final var restStack = inputHandler.insertItem(slot, itemStack, false);
            if (!restStack.isEmpty()) {
                ingredientToken.restore(restStack);
                return ObstructedCookingPotOperation.INSTANCE;
            }
        }

        return CookingPotOperation.INSTANCE;
    }

    private boolean isObstructed(CookingPotRecipe recipe, List<IngredientToken> ingredientTokens) {
        final var simulatedInventory = new ItemStackHandler(6);
        for (int slot = 0; slot < 6; slot++) {
            simulatedInventory.setStackInSlot(slot, inputHandler.getStackInSlot(slot).copy());
        }

        for (int i = 0; i < ingredientTokens.size(); i++) {
            final var slot = KitchenCookingPotRecipeHandler.mapIngredientToSlot(i);
            final var ingredientStack = ingredientTokens.get(i).peek().copyWithCount(1);
            if (!simulatedInventory.insertItem(slot, ingredientStack, false).isEmpty()) {
                return true;
            }
        }

        return !recipe.matches(new RecipeWrapper(simulatedInventory), level);
    }

    private boolean canInsertAll(List<IngredientToken> ingredientTokens) {
        for (int i = 0; i < ingredientTokens.size(); i++) {
            final var ingredientStack = ingredientTokens.get(i).peek().copyWithCount(1);
            final var slot = KitchenCookingPotRecipeHandler.mapIngredientToSlot(i);
            if (!inputHandler.insertItem(slot, ingredientStack, true).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private static class CookingPotOperation implements KitchenOperation {
        private static final KitchenOperation INSTANCE = new CookingPotOperation();

        @Override
        public Optional<Component> getFeedback() {
            return Optional.of(Component.translatable("gui.cookingforblockheads.moved_to_cooking_pot").withStyle(ChatFormatting.YELLOW));
        }
    }

    private static class ObstructedCookingPotOperation implements KitchenOperation {
        private static final KitchenOperation INSTANCE = new ObstructedCookingPotOperation();

        @Override
        public Optional<Component> getFeedback() {
            return Optional.of(Component.translatable("gui.cookingforblockheads.cooking_pot_obstructed").withStyle(ChatFormatting.RED));
        }
    }
}
