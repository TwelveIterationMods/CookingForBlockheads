package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(id("request_craftables"), RequestAvailableCraftablesMessage.class, RequestAvailableCraftablesMessage::encode, RequestAvailableCraftablesMessage::decode, RequestAvailableCraftablesMessage::handle);
        networking.registerServerboundPacket(id("request_recipes"), RequestSelectionRecipesMessage.class, RequestSelectionRecipesMessage::encode, RequestSelectionRecipesMessage::decode, RequestSelectionRecipesMessage::handle);
        networking.registerServerboundPacket(id("craft_recipe"), CraftRecipeMessage.class, CraftRecipeMessage::encode, CraftRecipeMessage::decode, CraftRecipeMessage::handle);

        networking.registerClientboundPacket(id("craftables"), AvailableCraftablesListMessage.class, AvailableCraftablesListMessage::encode, AvailableCraftablesListMessage::decode, AvailableCraftablesListMessage::handle);
        networking.registerClientboundPacket(id("recipes"), SelectionRecipesListMessage.class, SelectionRecipesListMessage::encode, SelectionRecipesListMessage::decode, SelectionRecipesListMessage::handle);
        networking.registerClientboundPacket(id("synced_effect"), SyncedEffectMessage.class, SyncedEffectMessage::encode, SyncedEffectMessage::decode, SyncedEffectMessage::handle);
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
