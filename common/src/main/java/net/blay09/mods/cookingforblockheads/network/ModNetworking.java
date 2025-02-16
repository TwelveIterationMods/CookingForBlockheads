package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(id("craft_recipe"), CraftRecipeMessage.class, CraftRecipeMessage::encode, CraftRecipeMessage::decode, CraftRecipeMessage::handle);
        networking.registerServerboundPacket(id("request_recipes"), RequestRecipesMessage.class, RequestRecipesMessage::encode, RequestRecipesMessage::decode, RequestRecipesMessage::handle);
        networking.registerServerboundPacket(id("toggle_favorite"), ToggleFavoriteMessage.class, ToggleFavoriteMessage::encode, ToggleFavoriteMessage::decode, ToggleFavoriteMessage::handle);

        networking.registerClientboundPacket(id("item_list"), ItemListMessage.class, ItemListMessage::encode, ItemListMessage::decode, ItemListMessage::handle);
        networking.registerClientboundPacket(id("synced_effect"), SyncedEffectMessage.class, SyncedEffectMessage::encode, SyncedEffectMessage::decode, SyncedEffectMessage::handle);
        networking.registerClientboundPacket(id("recipes"), RecipesMessage.class, RecipesMessage::encode, RecipesMessage::decode, RecipesMessage::handle);
        networking.registerClientboundPacket(id("moved_to_oven"), MovedToOvenMessage.class, MovedToOvenMessage::encode, MovedToOvenMessage::decode, MovedToOvenMessage::handle);
        networking.registerClientboundPacket(id("favorite_list"), FavoriteListMessage.class, FavoriteListMessage::encode, FavoriteListMessage::decode, FavoriteListMessage::handle);
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
