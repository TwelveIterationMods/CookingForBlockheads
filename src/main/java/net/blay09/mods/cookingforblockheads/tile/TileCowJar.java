package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.minecraft.tileentity.ITickableTileEntity;

public class TileCowJar extends TileMilkJar implements ITickableTileEntity {

    private static final int UPDATE_INTERVAL = 20;

    protected boolean isDirty;
    protected int ticksSinceUpdate;

    @Override
    public void tick() {
        if (milkAmount < MILK_CAPACITY) {
            milkAmount += CookingForBlockheadsConfig.COMMON.cowJarMilkPerTick.get();
            isDirty = true;
        }
        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            ticksSinceUpdate = 0;
        }
    }

}
