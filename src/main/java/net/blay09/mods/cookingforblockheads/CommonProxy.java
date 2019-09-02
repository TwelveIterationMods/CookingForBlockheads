package net.blay09.mods.cookingforblockheads;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Collections;
import java.util.List;

public class CommonProxy {

    public void init(FMLInitializationEvent event) {
    }

    public void registerModels() {

    }

    public List<String> getItemTooltip(ItemStack itemStack, PlayerEntity player) {
        return Collections.emptyList();
    }
}
