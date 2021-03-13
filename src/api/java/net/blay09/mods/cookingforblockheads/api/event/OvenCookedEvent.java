package net.blay09.mods.cookingforblockheads.api.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class OvenCookedEvent extends Event {

    private final World world;
    private final BlockPos pos;
    private final ItemStack resultItem;

    public OvenCookedEvent(World world, BlockPos pos, ItemStack resultItem) {
        this.world = world;
        this.pos = pos;
        this.resultItem = resultItem;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }
}
