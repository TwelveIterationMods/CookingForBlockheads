package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RequestAvailableRecipesMessage {

    public static RequestAvailableRecipesMessage decode(FriendlyByteBuf buf) {
        return new RequestAvailableRecipesMessage();
    }

    public static void encode(RequestAvailableRecipesMessage message, FriendlyByteBuf buf) {
    }

    public static void handle(ServerPlayer player, RequestAvailableRecipesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.handleRequestAvailableRecipes();
        }
    }
}
