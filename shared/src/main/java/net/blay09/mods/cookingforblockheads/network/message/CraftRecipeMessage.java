package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CraftRecipeMessage {

    private final ResourceLocation recipeId;
    private final NonNullList<ItemStack> lockedInputs;
    private final boolean craftFullStack;
    private final boolean addToInventory;

    public CraftRecipeMessage(ResourceLocation recipeId, @Nullable NonNullList<ItemStack> lockedInputs, boolean craftFullStack, boolean addToInventory) {
        this.recipeId = recipeId;
        this.lockedInputs = lockedInputs;
        this.craftFullStack = craftFullStack;
        this.addToInventory = addToInventory;
    }

    public static void encode(CraftRecipeMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.recipeId);
        if (message.lockedInputs != null) {
            buf.writeByte(message.lockedInputs.size());
            for (ItemStack itemstack : message.lockedInputs) {
                buf.writeItem(itemstack);
            }
        } else {
            buf.writeByte(0);
        }

        buf.writeBoolean(message.craftFullStack);
        buf.writeBoolean(message.addToInventory);
    }

    public static CraftRecipeMessage decode(FriendlyByteBuf buf) {
        final var recipeId = buf.readResourceLocation();
        final var lockedInputsCount = buf.readByte();
        NonNullList<ItemStack> lockedInputs = NonNullList.createWithCapacity(lockedInputsCount);
        for (int i = 0; i < lockedInputsCount; i++) {
            lockedInputs.add(buf.readItem());
        }
        final var craftFullStack = buf.readBoolean();
        final var addToInventory = buf.readBoolean();
        return new CraftRecipeMessage(recipeId, lockedInputs, craftFullStack, addToInventory);
    }

    public static void handle(ServerPlayer player, CraftRecipeMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.craft(message.recipeId, message.lockedInputs, message.craftFullStack, message.addToInventory);
        }
    }

}
