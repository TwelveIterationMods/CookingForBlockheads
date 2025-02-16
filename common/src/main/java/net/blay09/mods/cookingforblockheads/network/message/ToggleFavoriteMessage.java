package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;

public class ToggleFavoriteMessage {

    private final ResourceLocation itemId;
    private final boolean favorite;

    public ToggleFavoriteMessage(ResourceLocation itemId, boolean favorite) {
        this.itemId = itemId;
        this.favorite = favorite;
    }

    public static void encode(ToggleFavoriteMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.itemId);
        buf.writeBoolean(message.favorite);
    }

    public static ToggleFavoriteMessage decode(FriendlyByteBuf buf) {
        final var itemId = buf.readResourceLocation();
        final var favorite = buf.readBoolean();
        return new ToggleFavoriteMessage(itemId, favorite);
    }

    public static void handle(ServerPlayer player, ToggleFavoriteMessage message) {
        final var data = Balm.getHooks().getPersistentData(player);
        final var cfbData = data.getCompound("CookingForBlockheads");
        final var favoriteItems = cfbData.getCompound("FavoriteItemIds");
        if (message.favorite) {
            favoriteItems.putBoolean(message.itemId.toString(), true);
        } else {
            favoriteItems.remove(message.itemId.toString());
        }
        cfbData.put("FavoriteItemIds", favoriteItems);
        data.put("CookingForBlockheads", cfbData);

        final var favoriteItemIds = new HashSet<ResourceLocation>();
        for (final var favoriteItemId : favoriteItems.getAllKeys()) {
            favoriteItemIds.add(new ResourceLocation(favoriteItemId));
        }
        Balm.getNetworking().sendTo(player, new FavoriteListMessage(favoriteItemIds));
    }

}
