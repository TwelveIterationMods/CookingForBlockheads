package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class RequestSelectionRecipesMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestSelectionRecipesMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
            "request_selection_recipes"));

    private final ItemStack outputItem;
    private final NonNullList<ItemStack> lockedInputs;

    public RequestSelectionRecipesMessage(ItemStack outputItem, NonNullList<ItemStack> lockedInputs) {
        this.outputItem = outputItem;
        this.lockedInputs = lockedInputs;
    }

    public static RequestSelectionRecipesMessage decode(RegistryFriendlyByteBuf buf) {
        ItemStack outputItem = ItemStack.STREAM_CODEC.decode(buf);
        final var lockedInputsCount = buf.readByte();
        NonNullList<ItemStack> lockedInputs = NonNullList.createWithCapacity(lockedInputsCount);
        for (int i = 0; i < lockedInputsCount; i++) {
            lockedInputs.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
        return new RequestSelectionRecipesMessage(outputItem, lockedInputs);
    }

    public static void encode(RegistryFriendlyByteBuf buf, RequestSelectionRecipesMessage message) {
        ItemStack.STREAM_CODEC.encode(buf, message.outputItem);
        buf.writeByte(message.lockedInputs.size());
        for (ItemStack itemstack : message.lockedInputs) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemstack);
        }
    }

    public static void handle(ServerPlayer player, RequestSelectionRecipesMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.handleRequestSelectionRecipes(message.outputItem, message.lockedInputs);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
