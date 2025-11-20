package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.OvenMenu;
import net.blay09.mods.cookingforblockheads.util.ListUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ClientboundOvenResultsPacket(List<ItemStack> itemStacks) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundOvenResultsPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "oven_results"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOvenResultsPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_LIST_STREAM_CODEC,
            ClientboundOvenResultsPacket::itemStacks,
            ClientboundOvenResultsPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(Player player, ClientboundOvenResultsPacket packet) {
        if (player.containerMenu instanceof OvenMenu ovenMenu) {
            ovenMenu.setResultItems(ListUtils.nonNullListOf(packet.itemStacks(), ItemStack.EMPTY));
        }
    }
}
