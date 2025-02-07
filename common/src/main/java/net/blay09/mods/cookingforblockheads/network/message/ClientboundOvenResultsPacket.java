package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.OvenMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ClientboundOvenResultsPacket(NonNullList<ItemStack> itemStacks) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundOvenResultsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "oven_results"));

    public static void encode(RegistryFriendlyByteBuf buf, ClientboundOvenResultsPacket message) {
        buf.writeInt(message.itemStacks.size());
        for (ItemStack itemStack : message.itemStacks) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemStack);
        }
    }

    public static ClientboundOvenResultsPacket decode(RegistryFriendlyByteBuf buf) {
        final var itemStackCount = buf.readInt();
        final var itemStacks = NonNullList.withSize(itemStackCount, ItemStack.EMPTY);
        for (int i = 0; i < itemStackCount; i++) {
            itemStacks.set(i, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
        return new ClientboundOvenResultsPacket(itemStacks);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(Player player, ClientboundOvenResultsPacket packet) {
        if (player.containerMenu instanceof OvenMenu ovenMenu) {
            ovenMenu.setResultItems(packet.itemStacks());
        }
    }
}
