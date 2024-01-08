package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(id("request_available_recipes"), RequestAvailableCraftablesMessage.class, RequestAvailableCraftablesMessage::encode, RequestAvailableCraftablesMessage::decode, RequestAvailableCraftablesMessage::handle);
        networking.registerServerboundPacket(id("request_selected_recipes"), RequestSelectedRecipesMessage.class, RequestSelectedRecipesMessage::encode, RequestSelectedRecipesMessage::decode, RequestSelectedRecipesMessage::handle);
        networking.registerServerboundPacket(id("craft_recipe"), CraftRecipeMessage.class, CraftRecipeMessage::encode, CraftRecipeMessage::decode, CraftRecipeMessage::handle);

        networking.registerClientboundPacket(id("available_recipes"), AvailableRecipeListMessage.class, AvailableRecipeListMessage::encode, AvailableRecipeListMessage::decode, AvailableRecipeListMessage::handle);
        networking.registerClientboundPacket(id("selected_recipes"), SelectedRecipeListMessage.class, SelectedRecipeListMessage::encode, SelectedRecipeListMessage::decode, SelectedRecipeListMessage::handle);
        networking.registerClientboundPacket(id("synced_effect"), SyncedEffectMessage.class, SyncedEffectMessage::encode, SyncedEffectMessage::decode, SyncedEffectMessage::handle);
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
