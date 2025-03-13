package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class KitchenFloorBlock extends Block {

    public static final MapCodec<KitchenFloorBlock> CODEC = simpleCodec(KitchenFloorBlock::new);

    public KitchenFloorBlock(Properties properties) {
        super(properties.sound(SoundType.STONE).strength(0.8f));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
