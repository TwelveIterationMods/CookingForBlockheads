package net.blay09.mods.cookingforblockheads.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.network.chat.Component;

public class OvenBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof OvenBlockEntity oven) {
            if (oven.hasPowerUpgrade()) {
                output.text(Component.translatable("waila.cookingforblockheads.heating_unit"));
            }
        }
    }
}
