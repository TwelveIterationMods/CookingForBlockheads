package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.client.gui.screen.RecipeBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record MovedToOvenMessage() {

    public static void encode(MovedToOvenMessage message, FriendlyByteBuf buf) {
    }

    public static MovedToOvenMessage decode(FriendlyByteBuf buf) {
        return new MovedToOvenMessage();
    }

    public static void handle(Player player, MovedToOvenMessage message) {
        if (Minecraft.getInstance().screen instanceof RecipeBookScreen recipeBookScreen) {
            recipeBookScreen.displayMovedToOvenHint();
        }
    }

}
