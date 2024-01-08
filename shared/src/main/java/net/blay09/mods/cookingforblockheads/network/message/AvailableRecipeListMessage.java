package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class AvailableRecipeListMessage {

    private final List<RecipeWithStatus> recipes;

    public AvailableRecipeListMessage(List<RecipeWithStatus> recipes) {
        this.recipes = recipes;
    }

    public static void encode(AvailableRecipeListMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.recipes.size());
        for (RecipeWithStatus recipe : message.recipes) {
            recipe.toNetwork(buf);
        }
    }

    public static AvailableRecipeListMessage decode(FriendlyByteBuf buf) {
        final var recipeCount = buf.readInt();
        final var recipes = new ArrayList<RecipeWithStatus>(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipes.add(RecipeWithStatus.fromNetwork(buf));
        }
        return new AvailableRecipeListMessage(recipes);
    }

    public static void handle(Player player, AvailableRecipeListMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.setAvailableRecipes(message.recipes);
        }
    }

}
