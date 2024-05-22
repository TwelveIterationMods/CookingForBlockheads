package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BaseEntityBlock;

public class DyedConnectorBlock extends ConnectorBlock {
    public static final MapCodec<DyedConnectorBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color").forGetter(
                    DyedConnectorBlock::getColor),
            propertiesCodec()).apply(it, DyedConnectorBlock::new));

    private final DyeColor color;

    protected DyedConnectorBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
