package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RecipesMessage {

    private ItemStack outputItem;
    private List<FoodRecipeWithIngredients> recipeList;

    public RecipesMessage(ItemStack outputItem, List<FoodRecipeWithIngredients> recipeList) {
        this.outputItem = outputItem;
        this.recipeList = recipeList;
    }

    public static RecipesMessage decode(FriendlyByteBuf buf) {
        ItemStack outputItem = buf.readItem();
        int recipeCount = buf.readInt();
        List<FoodRecipeWithIngredients> recipeList = Lists.newArrayListWithCapacity(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipeList.add(FoodRecipeWithIngredients.read(buf));
        }
        return new RecipesMessage(outputItem, recipeList);
    }

    public static void encode(RecipesMessage message, FriendlyByteBuf buf) {
        buf.writeItem(message.outputItem);
        buf.writeInt(message.recipeList.size());
        for (FoodRecipeWithIngredients recipe : message.recipeList) {
            recipe.write(buf);
        }
    }

    public static void handle(Player player, RecipesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof RecipeBookMenu) {
            ((RecipeBookMenu) container).setRecipeList(message.outputItem, message.recipeList);
        }
    }
}
