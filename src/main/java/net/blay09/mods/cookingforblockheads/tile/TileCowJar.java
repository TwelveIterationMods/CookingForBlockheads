package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.ModConfig;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.minecraft.util.ITickable;

public class TileCowJar extends TileMilkJar implements ITickable {

    private static final int UPDATE_INTERVAL = 20;

    protected boolean isDirty;
    protected int ticksSinceUpdate;

    @Override
    public void update() {
        if (milkAmount < MILK_CAPACITY) {
            milkAmount += ModConfig.general.cowJarMilkPerTick;
            isDirty = true;
        }
        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            ticksSinceUpdate = 0;
        }
    }

}
