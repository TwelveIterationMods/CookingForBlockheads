package net.blay09.mods.cookingforblockheads.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.entity.SinkBlockEntity;
import net.minecraft.network.chat.Component;

public class SinkBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof SinkBlockEntity sink) {
            if (sink.hasSaltFilter()) {
                output.text(Component.translatable("waila.cookingforblockheads.salt_filter"));
            }
            if (CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                output.text(Component.translatable("waila.cookingforblockheads.water_stored", sink.getFluidTank().getAmount(), sink.getFluidTank().getCapacity()));
            }
        }
    }
}
