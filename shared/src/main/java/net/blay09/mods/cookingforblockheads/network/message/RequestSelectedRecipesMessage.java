package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class RequestSelectedRecipesMessage {

    private final ItemStack outputItem;
    private final NonNullList<ItemStack> lockedInputs;

    public RequestSelectedRecipesMessage(ItemStack outputItem, NonNullList<ItemStack> lockedInputs) {
        this.outputItem = outputItem;
        this.lockedInputs = lockedInputs;
    }

    public static RequestSelectedRecipesMessage decode(FriendlyByteBuf buf) {
        ItemStack outputItem = buf.readItem();
        final var lockedInputsCount = buf.readByte();
        NonNullList<ItemStack> lockedInputs = NonNullList.createWithCapacity(lockedInputsCount);
        for (int i = 0; i < lockedInputsCount; i++) {
            lockedInputs.add(buf.readItem());
        }
        return new RequestSelectedRecipesMessage(outputItem, lockedInputs);
    }

    public static void encode(RequestSelectedRecipesMessage message, FriendlyByteBuf buf) {
        buf.writeItem(message.outputItem);
        buf.writeByte(message.lockedInputs.size());
        for (ItemStack itemstack : message.lockedInputs) {
            buf.writeItem(itemstack);
        }
    }

    public static void handle(ServerPlayer player, RequestSelectedRecipesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.handleRequestSelectedRecipes(message.outputItem, message.lockedInputs);
        }
    }
}
