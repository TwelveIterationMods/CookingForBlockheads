package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.Preferences;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record ClientboundSetPreferencesPayload(Preferences preferences) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundSetPreferencesPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "clientbound_set_preferences"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPreferencesPayload> STREAM_CODEC = StreamCodec.composite(
            Preferences.STREAM_CODEC,
            ClientboundSetPreferencesPayload::preferences,
            ClientboundSetPreferencesPayload::new
    );

    public static void handle(Player player, ClientboundSetPreferencesPayload message) {
        CookingForBlockheadsClient.setPreferences(message.preferences);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
