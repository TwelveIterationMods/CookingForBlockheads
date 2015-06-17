package net.blay09.mods.cookingbook.network;

import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.blay09.mods.cookingbook.food.recipe.RemoteCraftingFood;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MessageSyncList implements IMessage {

    public final List<ItemStack> sortedRecipes = new ArrayList<ItemStack>();
    public final ArrayListMultimap<String, FoodRecipe> availableRecipes = ArrayListMultimap.create();
    private int currentRecipeIdx;

    public MessageSyncList() {}

    public MessageSyncList(List<ItemStack> sortedRecipes, ArrayListMultimap<String, FoodRecipe> availableRecipes, int currentRecipeIdx) {
        this.sortedRecipes.addAll(sortedRecipes);
        this.availableRecipes.putAll(availableRecipes);
        this.currentRecipeIdx = currentRecipeIdx;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        currentRecipeIdx = buf.readByte();
        int itemCount = buf.readInt();
        for(int i = 0; i < itemCount; i++) {
            ItemStack itemStack = ByteBufUtils.readItemStack(buf);
            List<FoodRecipe> recipeList = availableRecipes.get(itemStack.toString());
            int recipeCount = buf.readInt();
            for(int j = 0; j < recipeCount; j++) {
                ItemStack outputItem = ByteBufUtils.readItemStack(buf);
                FoodIngredient[] craftMatrix = new FoodIngredient[buf.readByte()];
                for(int k = 0; k < craftMatrix.length; k++) {
                    ItemStack[] itemStacks = new ItemStack[buf.readByte()];
                    for(int l = 0; l < itemStacks.length; l++) {
                        itemStacks[l] = ByteBufUtils.readItemStack(buf);
                    }
                    boolean isOptional = buf.readBoolean();
                    craftMatrix[k] = new FoodIngredient(itemStacks, isOptional);
                }
                boolean isSmeltingRecipe = buf.readBoolean();
                recipeList.add(new RemoteCraftingFood(outputItem, craftMatrix, isSmeltingRecipe));
            }
            sortedRecipes.add(itemStack);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(currentRecipeIdx);
        buf.writeInt(sortedRecipes.size());
        for(ItemStack itemStack : sortedRecipes) {
            ByteBufUtils.writeItemStack(buf, itemStack);
            List<FoodRecipe> recipeList = availableRecipes.get(itemStack.toString());
            buf.writeInt(recipeList.size());
            for(FoodRecipe recipe : recipeList) {
                ByteBufUtils.writeItemStack(buf, itemStack);
                buf.writeByte(recipe.getCraftMatrix().length);
                for(FoodIngredient ingredient : recipe.getCraftMatrix()) {
                    buf.writeByte(ingredient.getItemStacks().length);
                    for(ItemStack ingredientStack : ingredient.getItemStacks()) {
                        ByteBufUtils.writeItemStack(buf, ingredientStack);
                    }
                    buf.writeBoolean(ingredient.isToolItem());
                }
                buf.writeBoolean(recipe.isSmeltingRecipe());
            }
        }
    }

    public int getCurrentRecipeIndex() {
        return currentRecipeIdx;
    }
}
