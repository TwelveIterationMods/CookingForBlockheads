package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public class KitchenFloorBlock extends Block {

    public static final MapCodec<KitchenFloorBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color")
                    .forGetter(KitchenFloorBlock::getColor),
            propertiesCodec()).apply(it, KitchenFloorBlock::new));

    private final DyeColor color;

    public KitchenFloorBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public DyeColor getColor() {
        return color;
    }
}
