package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.List;

public class MessageRecipes implements IMessage {

    private ItemStack outputItem;
    private List<FoodRecipeWithIngredients> recipeList;

    public MessageRecipes() {
    }

    public MessageRecipes(ItemStack outputItem, List<FoodRecipeWithIngredients> recipeList) {
        this.outputItem = outputItem;
        this.recipeList = recipeList;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        outputItem = ByteBufUtils.readItemStack(buf);
        int recipeCount = buf.readInt();
        recipeList = Lists.newArrayListWithCapacity(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipeList.add(FoodRecipeWithIngredients.read(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, outputItem);
        buf.writeInt(recipeList.size());
        for (FoodRecipeWithIngredients recipe : recipeList) {
            recipe.write(buf);
        }
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public List<FoodRecipeWithIngredients> getRecipeList() {
        return recipeList;
    }

}