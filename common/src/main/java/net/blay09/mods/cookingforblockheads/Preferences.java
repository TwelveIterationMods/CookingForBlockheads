package net.blay09.mods.cookingforblockheads;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record Preferences(Identifier kitchenSortOrder) {

    public static final Preferences DEFAULT = new Preferences(CookingForBlockheads.id("name"));

    public static final Codec<Preferences> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("kitchenSortOrder").forGetter(Preferences::kitchenSortOrder)
    ).apply(instance, Preferences::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Preferences> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            Preferences::kitchenSortOrder,
            Preferences::new
    );

    public Preferences withKitchenSortOrder(Identifier kitchenSortOrder) {
        return new Preferences(kitchenSortOrder);
    }
}
