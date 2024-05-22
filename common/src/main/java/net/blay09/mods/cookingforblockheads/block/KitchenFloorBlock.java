package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KitchenFloorBlock extends Block {

    public static final MapCodec<KitchenFloorBlock> CODEC = simpleCodec(KitchenFloorBlock::new);

    public KitchenFloorBlock(Properties properties) {
        super(properties.sound(SoundType.STONE).strength(0.8f));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.multiblock_kitchen").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.kitchen_floor.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
