package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class FavoriteListMessage {

    private final Set<ResourceLocation> favoriteItemIds;

    public FavoriteListMessage(Set<ResourceLocation> favoriteItemIds) {
        this.favoriteItemIds = favoriteItemIds;
    }

    public static void encode(FavoriteListMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.favoriteItemIds.size());
        for (final var itemId : message.favoriteItemIds) {
            buf.writeResourceLocation(itemId);
        }
    }

    public static FavoriteListMessage decode(FriendlyByteBuf buf) {
        final var count = buf.readVarInt();
        final var favoriteItemIds = new HashSet<ResourceLocation>();
        for (int i = 0; i < count; i++) {
            favoriteItemIds.add(buf.readResourceLocation());
        }
        return new FavoriteListMessage(favoriteItemIds);
    }

    public static void handle(Player player, FavoriteListMessage message) {
        CookingForBlockheadsClient.setFavoriteItems(message.favoriteItemIds);
    }

}
