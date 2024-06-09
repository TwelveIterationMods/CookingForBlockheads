package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.cookingforblockheads.network.message.*;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(RequestAvailableCraftablesMessage.TYPE, RequestAvailableCraftablesMessage.class, RequestAvailableCraftablesMessage::encode, RequestAvailableCraftablesMessage::decode, RequestAvailableCraftablesMessage::handle);
        networking.registerServerboundPacket(RequestSelectionRecipesMessage.TYPE, RequestSelectionRecipesMessage.class, RequestSelectionRecipesMessage::encode, RequestSelectionRecipesMessage::decode, RequestSelectionRecipesMessage::handle);
        networking.registerServerboundPacket(CraftRecipeMessage.TYPE, CraftRecipeMessage.class, CraftRecipeMessage::encode, CraftRecipeMessage::decode, CraftRecipeMessage::handle);

        networking.registerClientboundPacket(AvailableCraftablesListMessage.TYPE, AvailableCraftablesListMessage.class, AvailableCraftablesListMessage::encode, AvailableCraftablesListMessage::decode, AvailableCraftablesListMessage::handle);
        networking.registerClientboundPacket(SelectionRecipesListMessage.TYPE, SelectionRecipesListMessage.class, SelectionRecipesListMessage::encode, SelectionRecipesListMessage::decode, SelectionRecipesListMessage::handle);
        networking.registerClientboundPacket(SyncedEffectMessage.TYPE, SyncedEffectMessage.class, SyncedEffectMessage::encode, SyncedEffectMessage::decode, SyncedEffectMessage::handle);
    }

}
