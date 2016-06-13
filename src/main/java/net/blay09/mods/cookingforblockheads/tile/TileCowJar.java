package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingConfig;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.minecraft.util.ITickable;

public class TileCowJar extends TileMilkJar implements ITickable {

	private static final int UPDATE_INTERVAL = 1000;

	protected boolean isDirty;
	protected int ticksSinceUpdate;

	@Override
	public void update() {
		if(milkAmount < MILK_CAPACITY) {
			milkAmount += CookingConfig.cowJarMilkPerTick;
			isDirty = true;
		}
		ticksSinceUpdate++;
		if(isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
			VanillaPacketHandler.sendTileEntityUpdate(this);
			ticksSinceUpdate = 0;
		}
	}

}
