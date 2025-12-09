package net.blay09.mods.cookingforblockheads.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;

public class CowJarBlockInfoProvider extends MilkJarBlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof CowJarBlockEntity cowJar) {
            if (cowJar.getCustomName() != null) {
                output.text(cowJar.getCustomName());
            }
        }
    }
}
