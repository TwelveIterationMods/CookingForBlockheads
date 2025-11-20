package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record RequestSelectionRecipesMessage(ItemStack outputItem, List<ItemStack> lockedInputs) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestSelectionRecipesMessage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
            "request_selection_recipes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestSelectionRecipesMessage> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            RequestSelectionRecipesMessage::outputItem,
            ItemStack.OPTIONAL_LIST_STREAM_CODEC,
            RequestSelectionRecipesMessage::lockedInputs,
            RequestSelectionRecipesMessage::new
    );

    public static void handle(ServerPlayer player, RequestSelectionRecipesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.handleRequestSelectionRecipes(message.outputItem, message.lockedInputs);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
