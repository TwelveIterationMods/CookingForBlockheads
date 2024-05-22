package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(RequestAvailableCraftablesMessage.TYPE, RequestAvailableCraftablesMessage.class, RequestAvailableCraftablesMessage::encode, RequestAvailableCraftablesMessage::decode, RequestAvailableCraftablesMessage::handle);
        networking.registerServerboundPacket(RequestSelectionRecipesMessage.TYPE, RequestSelectionRecipesMessage.class, RequestSelectionRecipesMessage::encode, RequestSelectionRecipesMessage::decode, RequestSelectionRecipesMessage::handle);
        networking.registerServerboundPacket(CraftRecipeMessage.TYPE, CraftRecipeMessage.class, CraftRecipeMessage::encode, CraftRecipeMessage::decode, CraftRecipeMessage::handle);

        networking.registerClientboundPacket(AvailableCraftablesListMessage.TYPE, AvailableCraftablesListMessage.class, AvailableCraftablesListMessage::encode, AvailableCraftablesListMessage::decode, AvailableCraftablesListMessage::handle);
        networking.registerClientboundPacket(SelectionRecipesListMessage.TYPE, SelectionRecipesListMessage.class, SelectionRecipesListMessage::encode, SelectionRecipesListMessage::decode, SelectionRecipesListMessage::handle);
        networking.registerClientboundPacket(SyncedEffectMessage.TYPE, SyncedEffectMessage.class, SyncedEffectMessage::encode, SyncedEffectMessage::decode, SyncedEffectMessage::handle);
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
