package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class RequestRecipesMessage {

    private ItemStack outputItem;
    private boolean forceNoFilter;

    public RequestRecipesMessage(ItemStack outputItem, boolean forceNoFilter) {
        this.outputItem = outputItem;
        this.forceNoFilter = forceNoFilter;
    }

    public static RequestRecipesMessage decode(FriendlyByteBuf buf) {
        ItemStack outputItem = buf.readItem();
        boolean forceNoFilter = buf.readBoolean();
        return new RequestRecipesMessage(outputItem, forceNoFilter);
    }

    public static void encode(RequestRecipesMessage message, FriendlyByteBuf buf) {
        buf.writeItem(message.outputItem);
        buf.writeBoolean(message.forceNoFilter);
    }

    public static void handle(ServerPlayer player, RequestRecipesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof RecipeBookMenu) {
            ((RecipeBookMenu) container).findAndSendRecipes(message.outputItem, message.forceNoFilter);
        }
    }
}
