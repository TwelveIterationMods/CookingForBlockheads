package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class SelectedRecipeListMessage {

    private final List<RecipeWithStatus> recipes;

    public SelectedRecipeListMessage(List<RecipeWithStatus> recipes) {
        this.recipes = recipes;
    }

    public static SelectedRecipeListMessage decode(FriendlyByteBuf buf) {
        final var recipeCount = buf.readInt();
        final var recipes = new ArrayList<RecipeWithStatus>(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipes.add(RecipeWithStatus.fromNetwork(buf));
        }
        return new SelectedRecipeListMessage(recipes);
    }

    public static void encode(SelectedRecipeListMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.recipes.size());
        for (RecipeWithStatus recipe : message.recipes) {
            recipe.toNetwork(buf);
        }
    }

    public static void handle(Player player, SelectedRecipeListMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.setRecipesForSelection(message.recipes);
        }
    }
}
