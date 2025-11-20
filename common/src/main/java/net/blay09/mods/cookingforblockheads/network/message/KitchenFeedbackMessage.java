package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.gui.screen.KitchenScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record KitchenFeedbackMessage(Component component) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<KitchenFeedbackMessage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "kitchen_feedback"));

    public static final StreamCodec<RegistryFriendlyByteBuf, KitchenFeedbackMessage> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC,
            KitchenFeedbackMessage::component,
            KitchenFeedbackMessage::new
    );

    public static void handle(Player player, KitchenFeedbackMessage message) {
        if (Minecraft.getInstance().screen instanceof KitchenScreen kitchenScreen) {
            kitchenScreen.displayKitchenFeedback(message.component);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
