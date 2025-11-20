package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.cookingforblockheads.network.message.*;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(RequestAvailableCraftablesMessage.TYPE, RequestAvailableCraftablesMessage.class, RequestAvailableCraftablesMessage.STREAM_CODEC, RequestAvailableCraftablesMessage::handle);
        networking.registerServerboundPacket(RequestSelectionRecipesMessage.TYPE, RequestSelectionRecipesMessage.class, RequestSelectionRecipesMessage.STREAM_CODEC, RequestSelectionRecipesMessage::handle);
        networking.registerServerboundPacket(CraftRecipeMessage.TYPE, CraftRecipeMessage.class, CraftRecipeMessage.STREAM_CODEC, CraftRecipeMessage::handle);
        networking.registerServerboundPacket(ToggleFavoriteMessage.TYPE, ToggleFavoriteMessage.class, ToggleFavoriteMessage.STREAM_CODEC, ToggleFavoriteMessage::handle);

        networking.registerClientboundPacket(AvailableCraftablesListMessage.TYPE, AvailableCraftablesListMessage.class, AvailableCraftablesListMessage.STREAM_CODEC, AvailableCraftablesListMessage::handle);
        networking.registerClientboundPacket(SelectionRecipesListMessage.TYPE, SelectionRecipesListMessage.class, SelectionRecipesListMessage.STREAM_CODEC, SelectionRecipesListMessage::handle);
        networking.registerClientboundPacket(SyncedEffectMessage.TYPE, SyncedEffectMessage.class, SyncedEffectMessage.STREAM_CODEC, SyncedEffectMessage::handle);
        networking.registerClientboundPacket(ClientboundOvenResultsPacket.TYPE, ClientboundOvenResultsPacket.class, ClientboundOvenResultsPacket.STREAM_CODEC, ClientboundOvenResultsPacket::handle);
        networking.registerClientboundPacket(KitchenFeedbackMessage.TYPE, KitchenFeedbackMessage.class, KitchenFeedbackMessage.STREAM_CODEC, KitchenFeedbackMessage::handle);
        networking.registerClientboundPacket(FavoriteListMessage.TYPE, FavoriteListMessage.class, FavoriteListMessage.STREAM_CODEC, FavoriteListMessage::handle);
    }

}
