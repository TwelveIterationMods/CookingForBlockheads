package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.container.RecipeBookContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRequestRecipes {

    private ItemStack outputItem;
    private boolean forceNoFilter;

    public MessageRequestRecipes(ItemStack outputItem, boolean forceNoFilter) {
        this.outputItem = outputItem;
        this.forceNoFilter = forceNoFilter;
    }

    public static MessageRequestRecipes decode(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        boolean forceNoFilter = buf.readBoolean();
        return new MessageRequestRecipes(outputItem, forceNoFilter);
    }

    public static void encode(MessageRequestRecipes message, PacketBuffer buf) {
        buf.writeItemStack(message.outputItem);
        buf.writeBoolean(message.forceNoFilter);
    }

    public static void handle(MessageRequestRecipes message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player == null) {
                return;
            }

            Container container = player.openContainer;
            if (container instanceof RecipeBookContainer) {
                ((RecipeBookContainer) container).findAndSendRecipes(message.outputItem, message.forceNoFilter);
            }
        });
        context.setPacketHandled(true);
    }
}
