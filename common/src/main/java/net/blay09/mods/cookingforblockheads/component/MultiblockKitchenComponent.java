package net.blay09.mods.cookingforblockheads.component;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;

public record MultiblockKitchenComponent(List<Component> components) implements TooltipProvider {
    public static final Codec<MultiblockKitchenComponent> CODEC = ComponentSerialization.CODEC.listOf()
            .xmap(MultiblockKitchenComponent::new, MultiblockKitchenComponent::components);

    public MultiblockKitchenComponent(Component component) {
        this(List.of(component));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag flag, DataComponentGetter components) {
        tooltip.accept(Component.translatable("tooltip.cookingforblockheads.multiblock_kitchen").withStyle(ChatFormatting.YELLOW));
        this.components.forEach(component -> tooltip.accept(component.copy().withStyle(ChatFormatting.GRAY)));
    }
}
