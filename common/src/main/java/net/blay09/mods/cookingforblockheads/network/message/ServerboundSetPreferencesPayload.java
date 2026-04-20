package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.Preferences;
import net.blay09.mods.cookingforblockheads.platform.attachment.ModDataAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundSetPreferencesPayload(Preferences preferences) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundSetPreferencesPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "serverbound_set_preferences"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetPreferencesPayload> STREAM_CODEC = StreamCodec.composite(
            Preferences.STREAM_CODEC,
            ServerboundSetPreferencesPayload::preferences,
            ServerboundSetPreferencesPayload::new
    );

    public static void handle(ServerPlayer player, ServerboundSetPreferencesPayload message) {
        ModDataAttachments.preferences.update(player, message.preferences);
        Balm.networking().sendTo(player, new ClientboundSetPreferencesPayload(message.preferences));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
