package net.blay09.mods.cookingforblockheads.network.message;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.blay09.mods.cookingforblockheads.util.ListUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CraftRecipeMessage(RecipeDisplayId recipeDisplayId, List<ItemStack> lockedInputs, boolean craftFullStack,
                                 boolean addToInventory) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CraftRecipeMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            CookingForBlockheads.MOD_ID,
            "craft_recipe"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CraftRecipeMessage> STREAM_CODEC = StreamCodec.composite(
            RecipeDisplayId.STREAM_CODEC,
            CraftRecipeMessage::recipeDisplayId,
            ItemStack.OPTIONAL_LIST_STREAM_CODEC,
            CraftRecipeMessage::lockedInputs,
            ByteBufCodecs.BOOL,
            CraftRecipeMessage::craftFullStack,
            ByteBufCodecs.BOOL,
            CraftRecipeMessage::addToInventory,
            CraftRecipeMessage::new
    );

    public static void handle(ServerPlayer player, CraftRecipeMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof KitchenMenu kitchenMenu) {
            kitchenMenu.craft(message.recipeDisplayId,
                    ListUtils.nonNullListOf(message.lockedInputs, ItemStack.EMPTY),
                    message.craftFullStack,
                    message.addToInventory);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
