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
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.jetbrains.annotations.Nullable;

public class CraftRecipeMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CraftRecipeMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
            "craft_recipe"));

    private final RecipeDisplayId recipeDisplayId;
    private final NonNullList<ItemStack> lockedInputs;
    private final boolean craftFullStack;
    private final boolean addToInventory;

    public CraftRecipeMessage(RecipeDisplayId recipeDisplayId, @Nullable NonNullList<ItemStack> lockedInputs, boolean craftFullStack, boolean addToInventory) {
        this.recipeDisplayId = recipeDisplayId;
        this.lockedInputs = lockedInputs;
        this.craftFullStack = craftFullStack;
        this.addToInventory = addToInventory;
    }

    public static void encode(RegistryFriendlyByteBuf buf, CraftRecipeMessage message) {
        RecipeDisplayId.STREAM_CODEC.encode(buf, message.recipeDisplayId);
        if (message.lockedInputs != null) {
            buf.writeByte(message.lockedInputs.size());
            for (ItemStack itemstack : message.lockedInputs) {
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemstack);
            }
        } else {
            buf.writeByte(0);
        }

        buf.writeBoolean(message.craftFullStack);
        buf.writeBoolean(message.addToInventory);
    }

    public static CraftRecipeMessage decode(RegistryFriendlyByteBuf buf) {
        final var recipeDisplayId = RecipeDisplayId.STREAM_CODEC.decode(buf);
        final var lockedInputsCount = buf.readByte();
        NonNullList<ItemStack> lockedInputs = NonNullList.createWithCapacity(lockedInputsCount);
        for (int i = 0; i < lockedInputsCount; i++) {
            lockedInputs.add( ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
        final var craftFullStack = buf.readBoolean();
        final var addToInventory = buf.readBoolean();
        return new CraftRecipeMessage(recipeDisplayId, lockedInputs, craftFullStack, addToInventory);
    }

    public static void handle(ServerPlayer player, CraftRecipeMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.craft(message.recipeDisplayId, message.lockedInputs, message.craftFullStack, message.addToInventory);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
