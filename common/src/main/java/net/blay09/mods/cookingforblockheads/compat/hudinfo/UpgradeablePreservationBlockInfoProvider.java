package net.blay09.mods.cookingforblockheads.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.minecraft.network.chat.Component;

public class UpgradeablePreservationBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof UpgradeablePreservation upgradeable) {
            if (upgradeable.hasPreservationUpgrade()) {
                output.text(Component.translatable("waila.cookingforblockheads.preservation_chamber"));
            }
        }
    }
}
