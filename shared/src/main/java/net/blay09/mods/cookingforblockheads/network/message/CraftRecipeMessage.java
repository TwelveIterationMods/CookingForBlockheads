package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CraftRecipeMessage {

    private final ItemStack outputItem;
    private final FoodRecipeType recipeType;
    private final NonNullList<ItemStack> craftMatrix;
    private final boolean stack;

    public CraftRecipeMessage(ItemStack outputItem, FoodRecipeType recipeType, NonNullList<ItemStack> craftMatrix, boolean stack) {
        this.outputItem = outputItem;
        this.recipeType = recipeType;
        this.craftMatrix = craftMatrix;
        this.stack = stack;
    }

    public static void encode(CraftRecipeMessage message, FriendlyByteBuf buf) {
        buf.writeItem(message.outputItem);
        buf.writeByte(message.recipeType.ordinal());
        buf.writeByte(message.craftMatrix.size());
        for (ItemStack itemstack : message.craftMatrix) {
            buf.writeItem(itemstack);
        }

        buf.writeBoolean(message.stack);
    }

    public static CraftRecipeMessage decode(FriendlyByteBuf buf) {
        ItemStack outputItem = buf.readItem();
        FoodRecipeType recipeType = FoodRecipeType.fromId(buf.readByte());
        int ingredientCount = buf.readByte();
        NonNullList<ItemStack> craftMatrix = NonNullList.create();
        for (int i = 0; i < ingredientCount; i++) {
            craftMatrix.add(buf.readItem());
        }
        boolean stack = buf.readBoolean();
        return new CraftRecipeMessage(outputItem, recipeType, craftMatrix, stack);
    }

    public static void handle(ServerPlayer player, CraftRecipeMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof RecipeBookMenu) {
            ((RecipeBookMenu) container).tryCraft(message.outputItem, message.recipeType, message.craftMatrix, message.stack);
        }
    }

}
