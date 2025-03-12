package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.crafting.CraftableWithStatus;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;

public record AvailableCraftablesListMessage(List<CraftableWithStatus> craftables) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AvailableCraftablesListMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
            "available_craftables_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AvailableCraftablesListMessage> STREAM_CODEC = StreamCodec.composite(
            CraftableWithStatus.LIST_STREAM_CODEC,
            AvailableCraftablesListMessage::craftables,
            AvailableCraftablesListMessage::new
    );

    public static void handle(Player player, AvailableCraftablesListMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.setCraftables(message.craftables);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
