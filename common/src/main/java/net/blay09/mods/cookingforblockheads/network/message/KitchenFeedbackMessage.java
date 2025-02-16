package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.gui.screen.KitchenScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record KitchenFeedbackMessage(Component component) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<KitchenFeedbackMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "kitchen_feedback"));

    public static void encode(RegistryFriendlyByteBuf buf, KitchenFeedbackMessage message) {
        ComponentSerialization.STREAM_CODEC.encode(buf, message.component);
    }

    public static KitchenFeedbackMessage decode(RegistryFriendlyByteBuf buf) {
        final var component = ComponentSerialization.STREAM_CODEC.decode(buf);
        return new KitchenFeedbackMessage(component);
    }

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
