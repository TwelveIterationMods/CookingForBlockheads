package net.blay09.mods.cookingforblockheads.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OvenCookedEvent extends BalmEvent {

    private final Level level;
    private final BlockPos pos;
    private final ItemStack resultItem;

    public OvenCookedEvent(Level level, BlockPos pos, ItemStack resultItem) {
        this.level = level;
        this.pos = pos;
        this.resultItem = resultItem;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }
}
