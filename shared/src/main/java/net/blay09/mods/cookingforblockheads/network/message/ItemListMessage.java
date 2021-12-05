package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class ItemListMessage {

    private final Collection<FoodRecipeWithStatus> recipeList;
    private final boolean hasOven;

    public ItemListMessage(Collection<FoodRecipeWithStatus> recipeList, boolean hasOven) {
        this.recipeList = recipeList;
        this.hasOven = hasOven;
    }

    public static void encode(ItemListMessage message, FriendlyByteBuf buf) {
        int recipeCount = message.recipeList.size();
        buf.writeInt(recipeCount);
        for (FoodRecipeWithStatus recipe : message.recipeList) {
            writeRecipe(recipe, buf);
        }

        buf.writeBoolean(message.hasOven);
    }

    public static ItemListMessage decode(FriendlyByteBuf buf) {
        int recipeCount = buf.readInt();
        List<FoodRecipeWithStatus> recipeList = Lists.newArrayListWithCapacity(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipeList.add(readRecipe(buf));
        }

        boolean hasOven = buf.readBoolean();
        return new ItemListMessage(recipeList, hasOven);
    }

    public static void handle(Player player, ItemListMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof RecipeBookMenu) {
            ((RecipeBookMenu) container).setItemList(message.recipeList);
            ((RecipeBookMenu) container).setHasOven(message.hasOven);
        }
    }

    private static FoodRecipeWithStatus readRecipe(FriendlyByteBuf buf) {
        ItemStack outputItem = buf.readItem();
        RecipeStatus status = RecipeStatus.fromId(buf.readByte());
        return new FoodRecipeWithStatus(outputItem, status);
    }

    private static void writeRecipe(FoodRecipeWithStatus recipe, FriendlyByteBuf buf) {
        buf.writeItem(recipe.getOutputItem());
        buf.writeByte(recipe.getStatus().ordinal());
    }
}
