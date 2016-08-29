package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithStatus;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Collection;

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
            recipeList.add(FoodRecipeWithStatus.read(buf));
        }
        hasOven = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        int recipeCount = recipeList.size();
        buf.writeInt(recipeCount);
        for (FoodRecipeWithStatus recipe : recipeList) {
            recipe.write(buf);
        }
        buf.writeBoolean(hasOven);
    }

    public Collection<FoodRecipeWithStatus> getRecipeList() {
        return recipeList;
    }

    public boolean getHasOven() {
        return hasOven;
    }
}
