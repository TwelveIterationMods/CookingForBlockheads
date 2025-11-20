package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public record FavoriteListMessage(Set<Identifier> favoriteItemIds) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<FavoriteListMessage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "favorite_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FavoriteListMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(it -> new HashSet<>(), Identifier.STREAM_CODEC),
            FavoriteListMessage::favoriteItemIds,
            FavoriteListMessage::new
    );

    public static void handle(Player player, FavoriteListMessage message) {
        CookingForBlockheadsClient.setFavoriteItems(message.favoriteItemIds);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
