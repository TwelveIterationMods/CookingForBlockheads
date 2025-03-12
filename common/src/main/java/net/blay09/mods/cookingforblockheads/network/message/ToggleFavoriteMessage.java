package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;

public record ToggleFavoriteMessage(ResourceLocation itemId, boolean favorite) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleFavoriteMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "toggle_favorite"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleFavoriteMessage> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ToggleFavoriteMessage::itemId,
            ByteBufCodecs.BOOL,
            ToggleFavoriteMessage::favorite,
            ToggleFavoriteMessage::new
    );

    public static void handle(ServerPlayer player, ToggleFavoriteMessage message) {
        final var data = Balm.getHooks().getPersistentData(player);
        final var cfbData = data.getCompoundOrEmpty("CookingForBlockheads");
        final var favoriteItems = cfbData.getCompoundOrEmpty("FavoriteItemIds");
        if (message.favorite) {
            favoriteItems.putBoolean(message.itemId.toString(), true);
        } else {
            favoriteItems.remove(message.itemId.toString());
        }
        cfbData.put("FavoriteItemIds", favoriteItems);
        data.put("CookingForBlockheads", cfbData);

        final var favoriteItemIds = new HashSet<ResourceLocation>();
        for (final var favoriteItemId : favoriteItems.keySet()) {
            favoriteItemIds.add(ResourceLocation.parse(favoriteItemId));
        }
        Balm.getNetworking().sendTo(player, new FavoriteListMessage(favoriteItemIds));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
