package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KitchenFloorBlock extends Block {

    public static final String name = "kitchen_floor";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public KitchenFloorBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(0.8f));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:multiblock_kitchen", ChatFormatting.YELLOW));

        for (String s : I18n.get("tooltip.cookingforblockheads:kitchen_floor.description").split("\\\\n")) {
            tooltip.add(TextUtils.coloredTextComponent(s, ChatFormatting.GRAY));
        }
    }

}
