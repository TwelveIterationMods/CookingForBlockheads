package net.blay09.mods.cookingforblockheads.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.cookingforblockheads.block.entity.ChickenSinkBlockEntity;
import net.blay09.mods.cookingforblockheads.rules.CookingForBlockheadsRules;
import net.minecraft.network.chat.Component;

public class ChickenSinkBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof ChickenSinkBlockEntity chickenSink) {
            if (chickenSink.getChickenType() == null) {
                if (!chickenSink.getIncubatingEgg().isEmpty()) {
                    output.text(Component.translatable("waila.cookingforblockheads.chicken_sink_incubating"));
                    output.progress(chickenSink.getIncubationProgress());
                } else {
                    output.text(Component.translatable("waila.cookingforblockheads.chicken_sink_empty"));
                }
            } else if (chickenSink.getChickenAge() < 0) {
                output.text(Component.translatable("waila.cookingforblockheads.chicken_sink_growing"));
                output.progress(chickenSink.getGrowthProgress());
            }
        }
    }
}
