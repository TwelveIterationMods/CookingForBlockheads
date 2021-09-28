package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

import java.util.List;

public class FoodRecipeWithIngredients {
    private final ItemStack outputItem;
    private final RecipeType recipeType;
    private final RecipeStatus recipeStatus;
    private final int recipeWidth;
    private final List<NonNullList<ItemStack>> craftMatrix;
    private final int availabilityMap;

    public FoodRecipeWithIngredients(ItemStack outputItem, RecipeType recipeType, RecipeStatus recipeStatus, int recipeWidth, List<NonNullList<ItemStack>> craftMatrix, int availabilityMap) {
        this.outputItem = outputItem;
        this.recipeType = recipeType;
        this.recipeStatus = recipeStatus;
        this.recipeWidth = recipeWidth;
        this.craftMatrix = craftMatrix;
        this.availabilityMap = availabilityMap;
    }

    public static FoodRecipeWithIngredients read(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        int recipeWidth = buf.readByte();
        int ingredientCount = buf.readByte();
        List<NonNullList<ItemStack>> craftMatrix = Lists.newArrayListWithCapacity(ingredientCount);
        for (int i = 0; i < ingredientCount; i++) {
            int stackCount = buf.readByte();
            if (stackCount > 0) {
                NonNullList<ItemStack> stackList = NonNullList.create();
                for (int j = 0; j < stackCount; j++) {
                    stackList.add(buf.readItemStack());
                }
                craftMatrix.add(stackList);
            } else {
                craftMatrix.add(null);
            }
        }
        RecipeType recipeType = RecipeType.fromId(buf.readByte());
        RecipeStatus recipeStatus = RecipeStatus.fromId(buf.readByte());
        int availabilityMap = buf.readShort();
        return new FoodRecipeWithIngredients(outputItem, recipeType, recipeStatus, recipeWidth, craftMatrix, availabilityMap);
    }

    public void write(PacketBuffer buf) {
        buf.writeItemStack(outputItem);
        buf.writeByte(recipeWidth);
        buf.writeByte(craftMatrix.size());
        for (List<ItemStack> stackList : craftMatrix) {
            buf.writeByte(stackList.size());
            for (ItemStack stack : stackList) {
                buf.writeItemStack(stack);
            }
        }
        buf.writeByte(recipeType.ordinal());
        buf.writeByte(recipeStatus.ordinal());
        buf.writeShort(availabilityMap);
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public RecipeStatus getRecipeStatus() {
        return recipeStatus;
    }

    public int getRecipeWidth() {
        return recipeWidth;
    }

    public List<NonNullList<ItemStack>> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public static FoodRecipeWithIngredients fromFoodRecipe(FoodRecipe recipe, RecipeStatus status, List<NonNullList<ItemStack>> craftMatrix, int availabilityMap) {
        return new FoodRecipeWithIngredients(recipe.getOutputItem(), recipe.getType(), status, recipe.getRecipeWidth(), craftMatrix, availabilityMap);
    }

    public int getAvailabilityMap() {
        return availabilityMap;
    }
}
