package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;

public record SelectionRecipesListMessage(List<RecipeWithStatus> recipes) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SelectionRecipesListMessage> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
            "selection_recipes_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectionRecipesListMessage> STREAM_CODEC = StreamCodec.composite(
            RecipeWithStatus.LIST_STREAM_CODEC,
            SelectionRecipesListMessage::recipes,
            SelectionRecipesListMessage::new
    );

    public static void handle(Player player, SelectionRecipesListMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.setRecipesForSelection(message.recipes);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
