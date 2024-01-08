package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RequestAvailableCraftablesMessage {

    public static RequestAvailableCraftablesMessage decode(FriendlyByteBuf buf) {
        return new RequestAvailableCraftablesMessage();
    }

    public static void encode(RequestAvailableCraftablesMessage message, FriendlyByteBuf buf) {
    }

    public static void handle(ServerPlayer player, RequestAvailableCraftablesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.handleRequestCraftables();
        }
    }
}
