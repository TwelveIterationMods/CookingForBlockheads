package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockKitchenFloor extends Block implements IRegisterableBlock {

    public BlockKitchenFloor() {
        super(Material.ROCK);

        setSoundType(SoundType.STONE);
        setCreativeTab(CookingForBlockheads.creativeTab);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":multiblock_kitchen"));

        addDefaultTooltipDescription(tooltip);
    }

    @Override
    public String getIdentifier() {
        return "kitchen_floor";
    }

}
