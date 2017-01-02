package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;

import java.util.Collection;

import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import com.google.common.collect.Lists;

public class MessageItemList implements IMessage {

    private Collection<FoodRecipeWithStatus> recipeList;
    private boolean hasOven;

    public MessageItemList() {}

    public MessageItemList(Collection<FoodRecipeWithStatus> recipeList, boolean hasOven) {
        this.recipeList = recipeList;
        this.hasOven = hasOven;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int recipeCount = buf.readInt();
        recipeList = Lists.newArrayListWithCapacity(recipeCount);
        for(int i = 0; i < recipeCount; i++) {
            recipeList.add(readRecipe(buf));
        }
        hasOven = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        int recipeCount = recipeList.size();
        buf.writeInt(recipeCount);
        for (FoodRecipeWithStatus recipe : recipeList) {
            writeRecipe(recipe, buf);
        }
        buf.writeBoolean(hasOven);
    }

    public Collection<FoodRecipeWithStatus> getRecipeList() {
        return recipeList;
    }

    public boolean getHasOven() {
        return hasOven;
    }
    
    private FoodRecipeWithStatus readRecipe(ByteBuf buf) {
        ItemStack outputItem = ByteBufUtils.readItemStack(buf);
        RecipeStatus status = RecipeStatus.fromId(buf.readByte());
        return new FoodRecipeWithStatus(outputItem, status);
    }

    private void writeRecipe(FoodRecipeWithStatus recipe, ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, recipe.getOutputItem());
        buf.writeByte(recipe.getStatus().ordinal());
    }
}
