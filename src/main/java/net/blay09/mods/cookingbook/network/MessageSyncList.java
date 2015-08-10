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

    public MessageSyncList() {}

    public MessageSyncList(List<ItemStack> sortedRecipes, ArrayListMultimap<String, FoodRecipe> availableRecipes) {
        this.sortedRecipes.addAll(sortedRecipes);
        this.availableRecipes.putAll(availableRecipes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int itemCount = buf.readInt();
        for(int i = 0; i < itemCount; i++) {
            ItemStack itemStack = ByteBufUtils.readItemStack(buf);
            List<FoodRecipe> recipeList = availableRecipes.get(itemStack.toString());
            int recipeCount = buf.readInt();
            for(int j = 0; j < recipeCount; j++) {
                recipeList.add(RemoteCraftingFood.read(buf));
            }
            sortedRecipes.add(itemStack);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sortedRecipes.size());
        for(ItemStack itemStack : sortedRecipes) {
            ByteBufUtils.writeItemStack(buf, itemStack);
            List<FoodRecipe> recipeList = availableRecipes.get(itemStack.toString());
            buf.writeInt(recipeList.size());
            for(FoodRecipe recipe : recipeList) {
                RemoteCraftingFood.write(buf, recipe);
            }
        }
    }

}
