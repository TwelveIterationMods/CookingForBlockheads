package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RequestAvailableCraftablesMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestAvailableCraftablesMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID, "request_available_craftables"));

    public static RequestAvailableCraftablesMessage decode(FriendlyByteBuf buf) {
        return new RequestAvailableCraftablesMessage();
    }

    public static void encode(FriendlyByteBuf buf, RequestAvailableCraftablesMessage message) {
    }

    public static void handle(ServerPlayer player, RequestAvailableCraftablesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.handleRequestCraftables();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
