package net.blay09.mods.cookingforblockheads.crafting;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record IngredientAmount(ItemStack itemStack, int amount) {
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientAmount> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            IngredientAmount::itemStack,
            ByteBufCodecs.INT,
            IngredientAmount::amount,
            IngredientAmount::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<IngredientAmount>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new));
}
