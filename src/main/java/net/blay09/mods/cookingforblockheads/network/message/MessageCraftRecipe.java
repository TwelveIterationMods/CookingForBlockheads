package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.container.RecipeBookContainer;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCraftRecipe {

    private final ItemStack outputItem;
    private final RecipeType recipeType;
    private final NonNullList<ItemStack> craftMatrix;
    private final boolean stack;

    public MessageCraftRecipe(ItemStack outputItem, RecipeType recipeType, NonNullList<ItemStack> craftMatrix, boolean stack) {
        this.outputItem = outputItem;
        this.recipeType = recipeType;
        this.craftMatrix = craftMatrix;
        this.stack = stack;
    }

    public static void encode(MessageCraftRecipe message, PacketBuffer buf) {
        buf.writeItemStack(message.outputItem);
        buf.writeByte(message.recipeType.ordinal());
        buf.writeByte(message.craftMatrix.size());
        for (ItemStack itemstack : message.craftMatrix) {
            buf.writeItemStack(itemstack);
        }

        buf.writeBoolean(message.stack);
    }

    public static MessageCraftRecipe decode(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        RecipeType recipeType = RecipeType.fromId(buf.readByte());
        int ingredientCount = buf.readByte();
        NonNullList<ItemStack> craftMatrix = NonNullList.create();
        for (int i = 0; i < ingredientCount; i++) {
            craftMatrix.add(buf.readItemStack());
        }
        boolean stack = buf.readBoolean();
        return new MessageCraftRecipe(outputItem, recipeType, craftMatrix, stack);
    }

    public static void handle(MessageCraftRecipe message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player == null) {
                return;
            }

            Container container = player.openContainer;
            if (container instanceof RecipeBookContainer) {
                ((RecipeBookContainer) container).tryCraft(message.outputItem, message.recipeType, message.craftMatrix, message.stack);
            }
        });
    }

}
