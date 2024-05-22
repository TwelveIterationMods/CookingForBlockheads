package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public class SelectionRecipesListMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SelectionRecipesListMessage> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(CookingForBlockheads.MOD_ID,
            "selection_recipes_list"));

    private final List<RecipeWithStatus> recipes;

    public SelectionRecipesListMessage(List<RecipeWithStatus> recipes) {
        this.recipes = recipes;
    }

    public static SelectionRecipesListMessage decode(RegistryFriendlyByteBuf buf) {
        final var recipeCount = buf.readInt();
        final var recipes = new ArrayList<RecipeWithStatus>(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipes.add(RecipeWithStatus.fromNetwork(buf));
        }
        return new SelectionRecipesListMessage(recipes);
    }

    public static void encode(RegistryFriendlyByteBuf buf, SelectionRecipesListMessage message) {
        buf.writeInt(message.recipes.size());
        for (RecipeWithStatus recipe : message.recipes) {
            recipe.toNetwork(buf);
        }
    }

    public static void handle(Player player, SelectionRecipesListMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.setRecipesForSelection(message.recipes);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
