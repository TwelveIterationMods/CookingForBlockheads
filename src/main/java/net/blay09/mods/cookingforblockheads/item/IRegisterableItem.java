package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

public interface IRegisterableItem {

    String getIdentifier();

    default ResourceLocation createRegistryName() {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, getIdentifier());
    }

    default void addDefaultTooltipDescription(List<String> tooltip) {
        for (String s : I18n.format("tooltip." + createRegistryName() + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

}
