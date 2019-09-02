package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.container.RecipeBookContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class MessageItemList {

    private final Collection<FoodRecipeWithStatus> recipeList;
    private final boolean hasOven;

    public MessageItemList(Collection<FoodRecipeWithStatus> recipeList, boolean hasOven) {
        this.recipeList = recipeList;
        this.hasOven = hasOven;
    }

    public static void encode(MessageItemList message, PacketBuffer buf) {
        int recipeCount = message.recipeList.size();
        buf.writeInt(recipeCount);
        for (FoodRecipeWithStatus recipe : message.recipeList) {
            writeRecipe(recipe, buf);
        }

        buf.writeBoolean(message.hasOven);
    }

    public static MessageItemList decode(PacketBuffer buf) {
        int recipeCount = buf.readInt();
        List<FoodRecipeWithStatus> recipeList = Lists.newArrayListWithCapacity(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipeList.add(readRecipe(buf));
        }

        boolean hasOven = buf.readBoolean();
        return new MessageItemList(recipeList, hasOven);
    }

    public static void handle(MessageItemList message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Container container = Minecraft.getInstance().player.openContainer;
            if (container instanceof RecipeBookContainer) {
                ((RecipeBookContainer) container).setItemList(message.recipeList);
                ((RecipeBookContainer) container).setHasOven(message.hasOven);
            }
        });
    }

    private static FoodRecipeWithStatus readRecipe(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        RecipeStatus status = RecipeStatus.fromId(buf.readByte());
        return new FoodRecipeWithStatus(outputItem, status);
    }

    private static void writeRecipe(FoodRecipeWithStatus recipe, PacketBuffer buf) {
        buf.writeItemStack(recipe.getOutputItem());
        buf.writeByte(recipe.getStatus().ordinal());
    }
}
