package net.blay09.mods.cookingforblockheads;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class CommonProxy {

    public List<ITextComponent> getItemTooltip(ItemStack itemStack, PlayerEntity player) {
        return Collections.emptyList();
    }
}
